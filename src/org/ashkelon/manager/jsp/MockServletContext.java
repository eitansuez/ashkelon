package org.ashkelon.manager.jsp;

import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Aug 19, 2005
 * Time: 10:58:18 PM
 */
public class MockServletContext implements ServletContext
{
   public ServletContext getContext(String string)
   {
      return null;
   }

   JSPServletLoader _loader;
   public JSPServletLoader getJSPServletLoader() { return _loader; }
   public void setJSPServletLoader(JSPServletLoader loader)
   {
      _loader = loader;
   }

   public int getMajorVersion() { return 0; }
   public int getMinorVersion() { return 1; }

   public String getMimeType(String string)
   {
      return null;
   }

   public Set getResourcePaths(String string)
   {
      return null;
   }

   public URL getResource(String string)
         throws MalformedURLException
   {
      return null;
   }

   public InputStream getResourceAsStream(String string)
   {
      return null;
   }


   public RequestDispatcher getRequestDispatcher(String resourcePath)
   {
      SimpleRequestDispatcher d = new SimpleRequestDispatcher(resourcePath);
      d.setJspServletLoader(_loader);
      return d;
   }

   public RequestDispatcher getNamedDispatcher(String string)
   {
      return null;
   }

   public Servlet getServlet(String string)
         throws ServletException
   {
      return null;
   }

   public Enumeration getServlets()
   {
      return null;
   }

   public Enumeration getServletNames()
   {
      return null;
   }

   public void log(String string) { }
   public void log(Exception exception, String string) { }
   public void log(String string, Throwable throwable) { }

   public String getRealPath(String string)
   {
      return null;
   }

   public String getServerInfo()
   {
      return null;
   }

   public String getInitParameter(String string) { return null; }
   public Enumeration getInitParameterNames()
   {
      return null;
   }

   public String getServletContextName()
   {
      return null;
   }

   Map _attrs = new HashMap();
   public Object getAttribute(String key) { return _attrs.get(key); }
   public Enumeration getAttributeNames() { return Collections.enumeration(_attrs.keySet()); }
   public void setAttribute(String key, Object value) { _attrs.put(key, value); }
   public void removeAttribute(String key) { _attrs.remove(key); }

}
