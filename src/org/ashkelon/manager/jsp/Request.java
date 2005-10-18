package org.ashkelon.manager.jsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.security.Principal;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Request implements HttpServletRequest
{
   private Map _attrs = new HashMap();
   private ServletContext _context;
   private String _servletPath;

   public Request(ServletContext context)
   {
      _context = context;
      File servletPathFile = (File) _context.getAttribute("webapp-path");
      _servletPath = servletPathFile.getAbsolutePath();
      if (!_servletPath.endsWith(File.separator))
         _servletPath = _servletPath + File.separator;
   }

   public String getServletPath() { return _servletPath; }

   public Object getAttribute(String key) { return _attrs.get(key); }
   public void setAttribute(String key, Object value) { _attrs.put(key, value); }
   public void removeAttribute(String key) { _attrs.remove(key); }
   public Enumeration getAttributeNames()
   {
      return Collections.enumeration(_attrs.keySet());
   }
   
   private Map _params = new HashMap();

   public String getParameter(String key) { return (String) _params.get(key); }
   public void setParameter(String key, String value)
   {
      _params.put(key, value);
   }
   public Map getParameterMap() { return _params; }
   public Enumeration getParameterNames()
   {
      Set keySet = _params.keySet();
      return Collections.enumeration(keySet);
   }

   // FIXME: handle more than a single value
   public String[] getParameterValues(String key)
   {
      String firstValue = (String) _params.get(key);
      return new String[] { firstValue };
   }

   
   private String _requestURI = "";
   public void setRequestURI(String uri) { _requestURI = uri; }
   public String getRequestURI() { return _requestURI; }

   
   public Locale getLocale() { return Locale.getDefault(); }
   public Enumeration getLocales()
   {
      Locale[] locales = Locale.getAvailableLocales();
      return Collections.enumeration(Arrays.asList(locales));
   }

   public String getCharacterEncoding() { return null; }
   public int getContentLength() { return -1; }
   public String getContentType() { return null; }
   public ServletInputStream getInputStream() throws IOException { return null; }
   public String getLocalAddr() { return null; }
   public String getLocalName() { return null; }
   public int getLocalPort() { return -1; }
   public String getProtocol() { return null; }
   public BufferedReader getReader() throws IOException { return null; }
   public String getRealPath(String arg0) { return null; }
   public String getRemoteAddr() { return null; }
   public String getRemoteHost() { return null; }
   public int getRemotePort() { return -1; }
   
   /* example param:  "include.html" */
   public RequestDispatcher getRequestDispatcher(String resourcePath)
   {
      SimpleRequestDispatcher d = new SimpleRequestDispatcher(resourcePath);
      d.setJspServletLoader(((MockServletContext) _context).getJSPServletLoader());
      return d;
   }
   public String getScheme() { return null; }
   public String getServerName() { return null; }
   public int getServerPort() { return -1; }
   public boolean isSecure() { return false; }
   public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {}

   public String getAuthType() { return ""; }
   public String getContextPath() { return "/"; }
   public Cookie[] getCookies() { return new Cookie[0]; }
   public long getDateHeader(String arg0) { return new Date().getTime(); }
   public String getHeader(String arg0) { return ""; }
   public Enumeration getHeaderNames() { return null; }
   public Enumeration getHeaders(String arg0) { return null; }
   public int getIntHeader(String arg0) { return -1; }
   public String getMethod() { return "GET"; }
   public String getPathInfo() { return "/"; }
   public String getPathTranslated() { return "/"; }
   public String getQueryString() { return ""; }
   public String getRemoteUser() { return ""; }
   public String getRequestedSessionId() { return ""; }
   public StringBuffer getRequestURL() { return new StringBuffer(); }
   public HttpSession getSession() { return new MockHttpSession(); }
   public HttpSession getSession(boolean arg0) { return null; }
   public Principal getUserPrincipal() { return null; }
   public boolean isRequestedSessionIdFromCookie() { return false; }
   public boolean isRequestedSessionIdFromUrl() { return true; }
   public boolean isRequestedSessionIdFromURL() { return true; }
   public boolean isRequestedSessionIdValid() { return true; }
   public boolean isUserInRole(String arg0) { return false; }
 
}
