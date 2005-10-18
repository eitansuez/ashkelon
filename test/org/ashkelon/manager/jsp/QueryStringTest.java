package org.ashkelon.manager.jsp;

import junit.framework.TestCase;
import javax.servlet.ServletContext;

public class QueryStringTest extends TestCase
{
   public void testQueryString()
   {
      String queryString = "eitan=three&leslie=four";

      SimpleRequestDispatcher d = new SimpleRequestDispatcher("");

      ServletContext context = new MockServletContext();
      context.setAttribute("webapp-path", "/home/eitan");
      Request request = new Request(context);

      d.fillRequestFromQueryString(queryString, request);

      assertEquals(request.getParameterMap().size(), 2);
      assertEquals("three", request.getParameter("eitan"));
   }
}
