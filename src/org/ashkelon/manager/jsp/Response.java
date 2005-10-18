package org.ashkelon.manager.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class Response implements HttpServletResponse
{
   private PrintWriter _writer;
   
   public Response() {}
   
   public void setWriter(PrintWriter writer) { _writer = writer; }
   public ServletOutputStream getOutputStream() throws IOException { return null; }
   public PrintWriter getWriter() throws IOException { return _writer; }

   public boolean isCommitted() { return false; }
   public void reset() {}

   public String getCharacterEncoding() { return ""; }
   public void setCharacterEncoding(String arg0) {}
   
   public void resetBuffer() {}
   public void flushBuffer() throws IOException {}
   public int getBufferSize() { return 0; }
   public void setBufferSize(int arg0) {}


   public String getContentType() { return null; }
   public void setContentType(String arg0) {}
   public void setContentLength(int arg0) {}

   public Locale getLocale() { return null; }
   public void setLocale(Locale arg0) {}

   public void addCookie(Cookie arg0) {}
   public void addDateHeader(String arg0, long arg1) {}
   public void addHeader(String arg0, String arg1) {}
   public void addIntHeader(String arg0, int arg1) {}
   public boolean containsHeader(String arg0) { return false; }
   public String encodeRedirectUrl(String arg0) { return ""; }
   public String encodeRedirectURL(String arg0) { return ""; }
   public String encodeUrl(String arg0) { return ""; }
   public String encodeURL(String arg0) { return ""; }
   public void sendError(int arg0, String arg1) throws IOException {}
   public void sendError(int arg0) throws IOException {}
   public void sendRedirect(String arg0) throws IOException {}
   public void setDateHeader(String arg0, long arg1) {}
   public void setHeader(String arg0, String arg1) {}
   public void setIntHeader(String arg0, int arg1) {}
   public void setStatus(int arg0, String arg1) {}
   public void setStatus(int arg0) {}

}
