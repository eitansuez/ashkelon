package org.ashkelon.pages;

import org.ashkelon.db.*;
import org.ashkelon.util.*;
import org.ashkelon.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.beans.*;

import org.apache.oro.text.perl.*;
import org.apache.oro.text.regex.*;


/**
 * @author Eitan Suez
 */
public class FlowController extends HttpServlet
{
   private String errorPage;
   private String cmdNotFoundPage;
   private ConfigInfo configInfo;
   
   private DBMgr dbmgr;
   
   private Perl5Util perlutil;
   private Logger log = Logger.getInstance();
   
   private Map classPool;
   
   
   public String getServletInfo() { return "Ashkelon Controller Servlet"; }
   
   public void init() throws ServletException
   {
      errorPage = "/error.jsp";
      cmdNotFoundPage = "/cmd_not_found.jsp";
      
      try
      {
         configInfo = (new ConfigInfo()).load();
      }
      catch (Exception ex)
      {
         throw new ServletException("Unable to load config information", ex);
      }
      
      dbmgr = DBMgr.getInstance();
      dbmgr.setWebApp(getServletContext());
         
      log.setTraceLevel(configInfo.getTraceLevel());
      String traceFile = "trace.log";
      String configTraceFile = configInfo.getTraceFile();
      if(!StringUtils.isBlank(configTraceFile))
      {
         try
         {
            if (new File(configTraceFile).canWrite())
               traceFile = configTraceFile;
         }
         catch (Exception ex)
         {
           log.error("benign exception: "+ex.getMessage());
         }
      }
      
      try
      {
         PrintWriter writer = new PrintWriter(new FileWriter(traceFile, true));
         log.setWriter(writer);
      }
      catch (IOException ex) { }
      
      String sourcepath = getServletContext().getInitParameter("sourcepath");
      log.traceln("retrieving source path: "+sourcepath);
      getServletContext().setAttribute("sourcepath", getPaths(sourcepath));
      
      perlutil = new Perl5Util();
      
      classPool = new HashMap(30);
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, java.io.IOException
   {
      doPost(request, response);
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException
   {
      log.setPrefix("FlowController");

      String cmd = ServletUtils.getCommand(request, configInfo.getDefaultCmd());
      request.setAttribute("cmd", cmd);

      Connection conn = null;
      try
      {
         conn = dbmgr.getConnection();
      }
      catch (SQLException ex)
      {
         if (conn != null) dbmgr.releaseConnection(conn);
         DBUtils.logSQLException(ex);
         
         request.setAttribute("javax.servlet.jsp.jspException", ex);
         RequestDispatcher rd = getServletContext().getRequestDispatcher(errorPage);
         rd.forward(request, response);
         return;
      }
      
      CommandInfo cmdInfo;
      String pageName, className;
      int numRedirects = 0;
      while (!StringUtils.isBlank(cmd) && numRedirects < 3)
      {
         cmdInfo = (CommandInfo) configInfo.getCommandMap().get(cmd);
         
         if (cmdInfo == null) // not found
         {
            dbmgr.releaseConnection(conn);
            RequestDispatcher rd = getServletContext().getRequestDispatcher(cmdNotFoundPage);
            rd.forward(request, response);
            return;
         }
         
         String jspPathPrefix = getJspPathPrefix(request);
         pageName = "/" + jspPathPrefix + cmdInfo.getPageName();
         log.debug("page name: "+pageName);
         className = configInfo.getDefaultPkg() + "." + cmdInfo.getClassName();
         
         try
         {
            Page page = (Page) classPool.get(className);
            if (page == null)
            {
               page = (Page) (Class.forName(className).newInstance());
               classPool.put(className, page);
            }
            
            page.setConnection(conn);
            page.setRequest(request);
            page.setApplication(getServletContext());
            
            log.verbose("Received command: "+cmd);
            cmd = page.handleRequest();
            if (StringUtils.isBlank(cmd))
            {
               updateTrail(request, cmdInfo);
               
               dbmgr.releaseConnection(conn);

               RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
               rd.forward(request, response);
               return;
            }
            
            numRedirects++;
         }
         catch (Exception ex)
         {
            dbmgr.releaseConnection(conn);
            if (ex instanceof SQLException)
            {
               DBUtils.logSQLException((SQLException) ex);
               dbmgr.resetConnections();
            }
            else
            {
               log.error("Exception: "+ex.getMessage());
            }
            
            request.setAttribute("javax.servlet.jsp.jspException", ex);
            RequestDispatcher rd = getServletContext().getRequestDispatcher(errorPage);
            rd.forward(request, response);
            return;
         }
      }
      
      dbmgr.releaseConnection(conn);

      request.setAttribute("javax.servlet.jsp.jspException", new Exception("FlowController Says: Too many internal redirects"));
      RequestDispatcher rd = getServletContext().getRequestDispatcher(errorPage);
      rd.forward(request, response);
      return;
   }
   
   private void updateTrail(HttpServletRequest request, CommandInfo cmdInfo) throws SQLException
   {
      HttpSession session = request.getSession();
      LinkedList trail = (LinkedList) session.getAttribute("trail");
      if (trail == null)
      {
         trail = new LinkedList();
         session.setAttribute("trail", trail);
      }

      if (!cmdInfo.isInTrail()) return;
      
      String[] uriLabelPair = new String[3];
      
      String uri = request.getRequestURI();
      String queryString = ServletUtils.getQueryString(request);
      
      if (queryString != null)
         uri += "?" + queryString;

      String cmd = cmdInfo.getCommand();
      String[] cmdparts = StringUtils.split(cmd, ".");
      
      uriLabelPair[0] = uri;
      String caption = cmdInfo.getCaption();
      String style = "";

      String pattern = "m/(.*)\\$(\\w+)\\$(.*)/";
      boolean matchsuccess = perlutil.match(pattern, caption);

      if (matchsuccess && perlutil.groups() > 3)
      {
         log.debug("Match Length: "+perlutil.groups());
         MatchResult mr = perlutil.getMatch();
         String first = mr.group(1);
         String field = mr.group(2);
         String last = mr.group(3);
         log.debug("Matched: 1: "+first+"; 2: "+field+"; 3: "+last);

         if (cmdparts.length >= 1)
         {
            /*
             * this is important to understand and relies entirely on convention:
             *  jsp request object must have same name as cmd's first part/
             *  e.g. if cmd is cls.xyz then request.getParameter("cls") must
             *   return class object in question, so that can use reflection
             *   to get class property such as getName (corresponding to a 
             *     $NAME$ dyn string)
             */
            Object obj = request.getAttribute(cmdparts[0]);
            if (obj == null)
            {
               // try this:
               obj = request.getAttribute(cmdparts[0] + "_" + field.toLowerCase()); // e.g. cls_name
               if (obj == null)
                  caption = first + cmdparts[0] + last;
               else
                  caption = first + obj.toString() + last;  // e.g. see cls.source jsp page
            }
            else if (obj instanceof JDoc)
            {
               String[] result = reflectionmagic((JDoc) obj, field.toLowerCase());
               String dynvalue = result[0];
               caption = first + dynvalue + last;
               style = result[1];
            }
         } else
         {
            caption = first + field + last;
         }
         log.debug("caption is: "+caption);
      }
      
      uriLabelPair[1] = caption;
      uriLabelPair[2] = style;

      if (!StringUtils.isBlank(caption))
      {
         if ( !trail.isEmpty() )
         {
            String[] last_pair = (String[]) trail.getLast();
            String last_uri = last_pair[0];
            if (!uri.equals(last_uri))
            {
               trail.addLast(uriLabelPair);
               log.verbose("Added "+uriLabelPair[0]+" to trail");
            }
         }
         else
         {
            trail.addLast(uriLabelPair);
            log.verbose("Added "+uriLabelPair[0]+" to trail");
         }

         if (trail.size() > configInfo.getMaxTrailLength())
            trail.removeFirst();
      }

      log.verbose("Updating trail");
      session.setAttribute("trail", trail);
   }
   
   // TODO: change name of this method to something meaningful
   private String[] reflectionmagic(JDoc doc, String propname)
   {
      try
      {
         PropertyDescriptor d = new PropertyDescriptor(propname, doc.getClass());
         Object value = d.getReadMethod().invoke(doc, null);
         return new String[] {(String) value, doc.getStyle()};
      }
      catch (Exception ex)
      {
         log.error("failed to lookup value for property "+propname);
         log.error(ex.getMessage());
      }
      return new String[] {"", ""};
   }

   /**
    * @return either "basic" if browser does not support modern.  returns "" otherwise.
    *  value is used as path prefix for jsp to fetch
    *
    * the calculation of browser type happens only when session is first created.
    * afterwards stored in & retrieved from session object
    */
   private String getJspPathPrefix(HttpServletRequest request)
   {
      HttpSession session = request.getSession();
      Object uiObj = (String) session.getAttribute("ui");
      String ui = "";
      log.debug("session ui value is "+uiObj);
      if (uiObj == null)
      {
         String ua = request.getHeader("User-Agent");
         if (ua == null) ua = "";
         boolean new_explorer = ua.indexOf("MSIE 6.") > -1 || ua.indexOf("MSIE 5.") > -1;
         boolean new_mozilla = ua.indexOf("Gecko") > -1;
         if (new_explorer || new_mozilla)  // don't yet serve xul for mozilla as it's
             // far from being in a working state -- allow config page for manual
             // switch to xul.
         {
            ui = "";
         }
         else
         {
            ui = "basic/";
         }
         session.setAttribute("ui", ui);
      }
      else
      {
         ui = (String) uiObj;
      }
      log.debug("decided to use ui "+ui);
      return ui;
   }


   private List getPaths(String pathstring)
   {
      log.debug("path string is: "+pathstring);
      String[] paths = StringUtils.split(pathstring, File.pathSeparator);
      return Arrays.asList(paths);
   }
   
   public void destroy() {}
   
}

