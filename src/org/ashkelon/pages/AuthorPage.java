package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import java.sql.*;

public class AuthorPage extends Page
{
   public AuthorPage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      String authorId = ServletUtils.getRequestParam(request, "id");
      String cmd = ServletUtils.getRequestParam(request, "cmd");
      
      Author author = Author.fetchAuthor(conn, Integer.parseInt(authorId));
      request.setAttribute("author", author);
      
      return null;
   }
   
}
