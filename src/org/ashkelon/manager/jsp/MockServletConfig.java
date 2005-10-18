package org.ashkelon.manager.jsp;

import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class MockServletConfig implements ServletConfig
{
   ServletContext _context;

   public MockServletConfig(ServletContext context)
   {
      _context = context;
   }

   public String getInitParameter(String name) { return null; }
   public Enumeration getInitParameterNames() { return null; }

   public ServletContext getServletContext() { return _context; }
   public String getServletName() { return "blah"; }


}
