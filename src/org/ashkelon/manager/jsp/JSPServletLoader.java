package org.ashkelon.manager.jsp;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class JSPServletLoader
{
   ServletContext _context;

   public JSPServletLoader(ServletContext context)
   {
      _context = context;
   }

   /**
    * 
    * @param jspFileName contract is that name of jsp file be passed in, like
    *   this:  hello_world.jsp
    * @throws Exception
    */
   public Servlet loadJSPServlet(String jspFileName) throws Exception
   {
      String clsName = clsName(jspFileName);
      Class servletCls = getClass().getClassLoader().loadClass(clsName);

      Servlet servlet = (Servlet) servletCls.newInstance();
      ServletConfig config = new MockServletConfig(_context);
      servlet.init(config);

      return servlet;
   }

   private String clsName(String jsp_name)
   {
      // 1. escape underscores like this: "_" -> "_005f"
      jsp_name = escapeUnderscore(jsp_name);

      // 2. replace dots with underscorces
      jsp_name = jsp_name.replace('.', '_');

      // 3. precaution:
      if (!jsp_name.endsWith("_jsp"))
      {
         // try this:
         jsp_name += "_jsp";
      }

      return "org.apache.jsp." + jsp_name;
   }

   private String escapeUnderscore(String jsp_name)
   {
      int idx = jsp_name.indexOf("_");
      if (idx > -1)
      {
         String remainder = jsp_name.substring(idx + 1);
         return jsp_name.substring(0, idx) + "_005f" +
                    escapeUnderscore(remainder);
      }
      else
      {
         return jsp_name;
      }
   }

}
