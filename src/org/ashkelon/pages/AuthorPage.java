package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import java.sql.*;

/**
 * @author Eitan Suez
 */
public class AuthorPage extends Page
{
   public AuthorPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      String authorId = ServletUtils.getRequestParam(request, "id");
      //String cmd = ServletUtils.getRequestParam(request, "cmd");
      
      Author author = Author.fetchAuthor(conn, Integer.parseInt(authorId));
      request.setAttribute("author", author);
      
      return null;
   }
   
}
