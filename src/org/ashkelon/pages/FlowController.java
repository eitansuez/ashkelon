package org.ashkelon.pages;

import org.ashkelon.db.*;
import org.ashkelon.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.beans.*;
import java.lang.reflect.*;

import org.apache.oro.text.perl.*;
import org.apache.oro.text.regex.*;


public class FlowController extends HttpServlet
{
   private String errorPage;
   private String cmdNotFoundPage;
   private ConfigInfo configInfo;
   
   private DBMgr dbmgr;
   
   private Perl5Util perlutil;
   private Logger log;
   
   private Map classPool;
   private int count = 0;
   
   
   public String getServletInfo()
   {
      return "Ashkelon Controller Servlet";
   }
   
   public void init() throws ServletException
   {
      errorPage = "/error.jsp";
      cmdNotFoundPage = "/cmd_not_found.jsp";
      
      try
      {
         configInfo = (new ConfigInfo()).load();
      } catch (Exception ex)
      {
         throw new ServletException("Unable to load config information", ex);
      }
      
      dbmgr = DBMgr.getInstance();
         
      log = Logger.getInstance();
      log.setTraceLevel(configInfo.getTraceLevel());
      String traceFile = "trace.log";
      String configTraceFile = configInfo.getTraceFile();
      if(!StringUtils.isBlank(configTraceFile))
      {
         try {
            if (new File(configTraceFile).canWrite())
               traceFile = configTraceFile;
         } catch (Exception ex) {}
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

      String cmd = request.getParameter("cmd");
      // safeguard:
      if (StringUtils.isBlank(cmd))
      {
         cmd = configInfo.getDefaultCmd();
         log.traceln("defaulting to cmd: "+cmd);
         //request.removeAttribute("cmd"); // in case next line adds instead of replacing
         request.setAttribute("cmd", cmd);
      }

      Connection conn = null;
      try
      {
         conn = dbmgr.getConnection();
      } catch (SQLException ex)
      {
         if (conn != null) dbmgr.releaseConnection(conn);
         DBUtils.logSQLException(ex);
         
         request.setAttribute("javax.servlet.jsp.jspException", ex);
         RequestDispatcher rd = getServletContext().getRequestDispatcher(errorPage);
         rd.forward(request, response);
         return;
      }
      
      
      java.util.Date now = new java.util.Date();
      response.setDateHeader("Date", now.getTime());

      /* this logic is incorrect -- on every page there exist dynamic elements
       * for example, the navigation trail would be incorrect if cached page
       * were to be re-used.  The way to cache is to cache the rest of the
       * page and not the dynamic elements.  This precludes a client cache */
      /*
      Calendar expires = new GregorianCalendar();
      expires.add(Calendar.DATE, +7);
      response.setDateHeader("Expires", expires.getTime().getTime());
       */

      
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
         
        /*
           response.getOutputStream().println("pagename: "+pageName);
           response.getOutputStream().println("classname: "+className);
           response.getOutputStream().flush();
         */
         
         try
         {
            // to do: should be able to improve performance with a page cache
            //  instead of re-instantiating Page each time
            
            Page page = (Page) classPool.get(className);
            if (page == null)
            {
               page = (Page) (Class.forName(className).newInstance());
               classPool.put(className, page);
            }
            
            page.setConnection(conn);
            page.setRequest(request);
            page.setApplication(getServletContext());
            
            log.verbose("Received command: "+ServletUtils.getRequestParam(request,"cmd"));
            cmd = page.init();
            if (StringUtils.isBlank(cmd))
            {
               //log.verbose("about to update trail");
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

      /* this should really read like: if (cmdInfo.includeInTrail()).. */
      if (!cmdInfo.isInTrail())
      {
         return;
      }
      
      String[] uriLabelPair = new String[3];
      
      String uri = request.getRequestURI();
      String queryString = ServletUtils.getQueryString(request);
      
      if (queryString != null)
         uri += "?" + queryString;

      //String cmd = ServletUtils.getRequestParam(request, "cmd");
      String cmd = cmdInfo.getCommand();
      String[] cmdparts = StringUtils.split(cmd, ".");
      
      /* no longer need this.
      String id = "";
      log.debug("querystring: "+queryString);
      String[] parts = StringUtils.split(queryString, "&");
      String key[] = {"", ""};
      for (int i=0; i<parts.length; i++)
      {
         key = StringUtils.split(parts[i], "=");
         if (key[0].endsWith("_id"))
         {
            log.debug("found id "+ key[1] + " in key "+key[0]);
            id = key[1];
            
         }
      }
       **/
      
      
      uriLabelPair[0] = uri;
      //CommandInfo cmdInfo = (CommandInfo) configInfo.getCommandMap().get(cmd);
      String caption = cmdInfo.getCaption();
      String tableName = cmdInfo.getTableName();
      String typename = "";

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
         //if (id.length() > 0)
         {
            // does not work -- function sequence error.
           //String sql = "select " + field + " from " + tableName + " where id = " + id;
           //log.debug("sql is: "+sql);
           //caption = firsquest.getAttribute(cmdparts[0]);
            
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
            } else
            {
               String[] result = reflectionmagic(obj, field.toLowerCase());
               String dynvalue = result[0];
               caption = first + dynvalue + last;
               typename = result[1];
            }
         } else
         {
            caption = first + field + last;
         }
         log.debug("caption is: "+caption);
      }
      
      uriLabelPair[1] = caption;
      uriLabelPair[2] = typename;

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
   
   public void destroy()
   {
      // cleanup tasks?
   }
   
   private String[] reflectionmagic(Object obj, String propname)
   {
      String typename = "";
      try
      {
         Class cls = Object.class;
         if (obj instanceof org.ashkelon.JPackage)
         {
            cls = org.ashkelon.JPackage.class;
            typename = "package";
         }
         else if (obj instanceof org.ashkelon.Member)
         {
            cls = org.ashkelon.Member.class;
            typename = ((org.ashkelon.Member) obj).getMemberTypeName();
         }
         else if (obj instanceof org.ashkelon.ClassType)
         {
            cls = org.ashkelon.ClassType.class;
            typename = ((org.ashkelon.ClassType) obj).getClassTypeName();
         }
         else if (obj instanceof org.ashkelon.Author)
         {
            cls = org.ashkelon.Author.class;
            typename = "author";
         }
         else if (obj instanceof org.ashkelon.API)
         {
            cls = org.ashkelon.API.class;
            typename = "api";
         }
         
         log.debug("Class Type is: "+cls.getName());
         PropertyDescriptor d = new PropertyDescriptor(propname, cls);
         Method m = d.getReadMethod();
         Object value = m.invoke(obj, null);
         String[] result = {(String) value, typename};
         return result;
      } catch (Exception ex)
      {
         log.error("failed to lookup value for property "+propname);
         log.error(ex.getMessage());
      }
      String[] result = {"", ""};
      return result;
   }

   /**
    * @return either "basic" if browser is not ie5.5 or 6.0.  returns "" otherwise.
    *  value is used as path prefix for jsp to fetch
    *
    * the calculation of browser type happens only when session is first created.
    * afterwards stored in & retrieved from session object
    */
   private String getJspPathPrefix(HttpServletRequest request)
   {
      HttpSession session = request.getSession();
      Object uiObj = (String) session.getAttribute("ui");
      String ui = "basic/";
      log.debug("session ui value is "+uiObj);
      if (uiObj == null)
      {
         String ua = request.getHeader("User-Agent");
         if (ua == null) ua = "";
         boolean foundie6 = ua.indexOf("MSIE 6.") > -1;
         boolean foundie5 = ua.indexOf("MSIE 5.") > -1;
         boolean foundmozilla = ua.indexOf("Gecko") > -1;
         if (foundie6 || foundie5 || foundmozilla)
         {
            ui = "";
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
      String sourcepath = System.getProperty("source.path");
      log.debug("path string is: "+pathstring);
      String[] paths = StringUtils.split(pathstring, File.pathSeparator);
      return Arrays.asList(paths);
   }
   
}

