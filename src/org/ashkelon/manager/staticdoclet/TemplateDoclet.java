package org.ashkelon.manager.staticdoclet;

import java.io.*;
import java.util.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import org.ashkelon.*;
import org.ashkelon.util.Logger;
import org.ashkelon.util.Options;
import org.ashkelon.pages.HtmlGenerator;
import org.ashkelon.pages.Java2HtmlGenerator;
import org.ashkelon.manager.jsp.JSPServletLoader;
import org.ashkelon.manager.jsp.Request;
import org.ashkelon.manager.jsp.Response;
import org.ashkelon.manager.jsp.MockServletContext;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.Project;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

public class TemplateDoclet extends Doclet
{
   public static boolean start(RootDoc doc)
   {
      return new TemplateDoclet(doc).doProcess();
   }
   
   private Servlet _apiJSPServlet, 
         _pkgJSPServlet, _clsJSPServlet, _memberJSPServlet,
         _clsSourceJSPServlet;
   private HtmlGenerator _srcHtmlGenerator;
   private FileNaming _naming;

   private ServletContext _context;
   private Logger _log = Logger.getInstance();

   private RootDoc _rootDoc;
   
   public TemplateDoclet(RootDoc doc) { _rootDoc = doc; }
   
   public boolean doProcess()
   {
      processOptions();
      
      try
      {
         setupContext();

         String apifilename =
               Options.getStringCommandLineOption("-api", _rootDoc.options());
         
         API api = API.unmarshal(apifilename, "");
         _log.debug("api unmarshalled; name is: "+api.getName());
      
         return processAPI(api);
      }
      catch (Exception ex)
      {
         _log.error(ex.getMessage());
         ex.printStackTrace();
      }
      return false;
   }

   private void setupContext()
         throws Exception
   {
      _context = new MockServletContext();
      _context.setAttribute("link-strategy", new StaticLinkStrategy());
      _context.setAttribute("static-context", new Boolean(true));
      _context.setAttribute("webapp-path", _webappPath);

      _srcHtmlGenerator = new Java2HtmlGenerator();
      _srcHtmlGenerator.initialize(null);
      _naming = new FileNaming(_outputDir);

      JSPServletLoader loader = new JSPServletLoader(_context);
      ((MockServletContext) _context).setJSPServletLoader(loader);

      _apiJSPServlet = loader.loadJSPServlet("api.jsp");
      _pkgJSPServlet = loader.loadJSPServlet("package.jsp");
      _clsJSPServlet = loader.loadJSPServlet("class.jsp");
      _memberJSPServlet = loader.loadJSPServlet("member.jsp");
      _clsSourceJSPServlet = loader.loadJSPServlet("cls_source.jsp");
   }

   private boolean processAPI(API api) throws Exception
   {
      Iterator pkgIterator = api.getPackagenames().iterator();
      PackageDoc pkgDoc;
      JPackage pkg = null;
      List jPackages = new ArrayList();

      while (pkgIterator.hasNext())
      {
         String pkgName = (String) pkgIterator.next();
         _log.brief("Processing package: "+pkgName);
         pkgDoc = _rootDoc.packageNamed(pkgName);

         pkg = new JPackage(pkgDoc, true, api);
         processPkgPage(pkg);
         jPackages.add(pkg);

         ClassType cls = null;
         for (int j=0; j<pkg.getClasses().size(); j++)
         {
            cls = (ClassType) pkg.getClasses().get(j);
            processClassPage(cls);

            File sourceFile = pkgDoc.findClass(cls.getName()).position().file();
            processClassSourcePage(sourceFile, cls);

            List constructors = cls.getConstructors();
            if (constructors.size() > 0)
               processConstructors(constructors);

            List fields = cls.getFields();
            for (int k=0; k<fields.size(); k++)
               processField((Member) fields.get(k));

            Iterator itr = cls.getGroupedMethods().iterator();
            while (itr.hasNext())
               processMethod((List) itr.next());
         }
      }

      api.setPackages(jPackages);
      processAPIPage(api);
      
      // last step: copy images/ folder and *.js, *.css to webapp_path
      Copy copyTask = new Copy();
      copyTask.setProject(new Project());
      copyTask.setTodir(_outputDir);
      FileSet fileSet = new FileSet();
      fileSet.setDir(_webappPath);
      fileSet.setIncludes("images,*.js,*.css");
      copyTask.addFileset(fileSet);
      copyTask.execute();

      return true;
   }

   
   private void processAPIPage(API api)
         throws IOException, ServletException
   {
      Request request = new Request(_context);
      request.setAttribute("api", api);
      request.setAttribute("cmd", "api");
      request.setRequestURI(_naming.apiURI(api));
      process(_apiJSPServlet, request, _naming.apiFilename(api));
   }
   private void processPkgPage(JPackage pkg)
         throws IOException, ServletException
   {
      _log.traceln(pkg.getName() + ": "+pkg.getSummaryDescription());
      Request request = new Request(_context);
      request.setAttribute("pkg", pkg);
      request.setAttribute("cmd", "pkg");
      request.setRequestURI(_naming.pkgURI(pkg));
      process(_pkgJSPServlet, request, _naming.pkgFilename(pkg));
   }
   private void processClassPage(ClassType cls)
         throws IOException, ServletException
   {
      Request request = new Request(_context);
      request.setAttribute("cls", cls);
      request.setAttribute("cmd", "cls");
      request.setRequestURI(_naming.clsURI(cls));
      process(_clsJSPServlet, request, _naming.clsFilename(cls));
   }
   private void processClassSourcePage(File sourceFile, ClassType cls)
         throws IOException, ServletException
   {
      String clsHtmlFilename = _naming.sourceFilename(cls);
      _srcHtmlGenerator.produceHtml(sourceFile.getAbsolutePath(), clsHtmlFilename);
      Request request = new Request(_context);
      request.setAttribute("cmd", "cls");
      request.setAttribute("cls_name", cls.getQualifiedName());
      request.setAttribute("source_file", sourceFile);
      request.setAttribute("html_file", clsHtmlFilename);
      request.setRequestURI(_naming.sourceWrappedURI(cls));
      process(_clsSourceJSPServlet, request, _naming.sourceFilenameWrapped(cls));
   }
   private void processConstructors(List constructors)
         throws IOException, ServletException
   {
      Request request = new Request(_context);
      request.setAttribute("cmd", "member");
      request.setAttribute("members", constructors);
      Member member = (Member) constructors.get(0);
      request.setAttribute("member", member);
      request.setRequestURI(_naming.memberURI(member));
      process(_memberJSPServlet, request, _naming.memberFilename(member));
   }
   private void processField(Member field)
         throws IOException, ServletException
   {
      Request request = new Request(_context);
      request.setAttribute("cmd", "member");
      request.setAttribute("member", field);
      request.setRequestURI(_naming.memberURI(field));
      process(_memberJSPServlet, request, _naming.memberFilename(field));
   }
   private void processMethod(List methods)
         throws IOException, ServletException
   {
      Request request = new Request(_context);
      request.setAttribute("cmd", "member");
      request.setAttribute("members", methods);
      Member method = (Member) methods.get(0);
      request.setAttribute("member", method);
      request.setRequestURI(_naming.memberURI(method));
      process(_memberJSPServlet, request, _naming.memberFilename(method));
   }

   private void process(Servlet servlet, Request request, String fileName)
            throws IOException, ServletException
   {
      Response response = new Response();
      PrintWriter writer = new PrintWriter(new FileWriter(fileName));
      response.setWriter(writer);

      servlet.service(request, response);

      response.getWriter().flush();
      response.getWriter().close();
   }
   
   
   private static String DEFAULT_OUTPUTDIR = "out";
   private static String DEFAULT_WEBAPP_PATH = "build/webapp";

   private static File _outputDir;
   public static File _webappPath = null;


   private void processOptions()
   {
      String cwd = System.getProperty("user.dir");
      String path = cwd + File.separator + DEFAULT_OUTPUTDIR;
      
      _log.debug("about to process -d option..");
      _outputDir = Options.getOutputDirCommandLineOption("-d", 
                                                         _rootDoc.options(), 
                                                         new File(path));
      _log.verbose("outputting html files to: "+_outputDir);
      
      _webappPath = new File(cwd + File.separator + DEFAULT_WEBAPP_PATH);
   }


   // -- option-related logic:
   
   static Map optionLengths = new HashMap();
   static
   {
      optionLengths.put("-d", new Integer(2));
      optionLengths.put("-api", new Integer(2));
   }
   public static int optionLength(String option)
   {
      Object value = optionLengths.get(option);
      if (value != null)
         return ((Integer) value).intValue();
      return 0;
   }
   
}
