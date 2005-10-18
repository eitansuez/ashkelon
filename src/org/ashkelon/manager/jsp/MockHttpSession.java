package org.ashkelon.manager.jsp;

import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class MockHttpSession implements HttpSession
{

   public Object getAttribute(String name) { return null; }
   public Enumeration getAttributeNames() { return null; }
   public long getCreationTime() { return new Date().getTime(); }
   public String getId() { return ""; }
   public long getLastAccessedTime() { return new Date().getTime(); }
   public int getMaxInactiveInterval() { return 1000; }
   public ServletContext getServletContext() { return null; }
   public HttpSessionContext getSessionContext() { return null; }
   public Object getValue(String name) { return null; }
   public String[] getValueNames() { return null; }
   public void invalidate() { }
   public boolean isNew() { return false; }
   public void putValue(String name, Object value) { }
   public void removeAttribute(String name) {}
   public void removeValue(String name) {}
   public void setAttribute(String name, Object value) {}
   public void setMaxInactiveInterval(int interval) {}

}
