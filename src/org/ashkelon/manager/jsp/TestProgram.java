package org.ashkelon.manager.jsp;

import java.io.FileWriter;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

public class TestProgram
{
   public TestProgram() throws Exception
   {
      // 1. create and setup a request
      ServletContext context = new MockServletContext();
      context.setAttribute("webapp-path", "/home/eitan");
      Request request = new Request(context);
      request.setAttribute("name", "Eitan");

      // 2. create and setup a response
      Response response = new Response();
      response.setWriter(new PrintWriter(new FileWriter("hello.html")));

      // 3. load jsp page
      JSPServletLoader loader = new JSPServletLoader(context);
      Servlet helloJSPServlet = loader.loadJSPServlet("hello.jsp");

      // 4. invoke it, passing it the request and response objects
      helloJSPServlet.service(request, response);

      // 5. flush & close connection
      response.getWriter().flush();
      response.getWriter().close();
   }

   public static void main(String[] args) throws Exception
   {
      new TestProgram();
   }
}
