package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

public class AuthorsPage extends Page
{
   public AuthorsPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      List authors = getAuthors();
      request.setAttribute("authors", authors);
      return null;
   }
   
   
   public List getAuthors() throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getallauthors");
      PreparedStatement p = conn.prepareStatement(sql);
      ResultSet rset = p.executeQuery();

      List authors = new ArrayList(50);
      Author author;
      
      while (rset.next())
      {
        author = new Author(rset.getString(2));
        author.setId(rset.getInt(1));
        authors.add(author);
      }
      rset.close();
      p.close();
      
      return authors;
   }
   
}

