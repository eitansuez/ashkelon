package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

/**
 * @author Eitan Suez
 */
public class StatsAuthors extends Page
{
   public StatsAuthors()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      request.setAttribute("classCounts", getAuthorStats());
      return null;
   }
   
   private static int MAX_ROWS = 50;
   
   public List getAuthorStats() throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("authorstats");
      
      Statement stmt = conn.createStatement();
      stmt.setMaxRows(MAX_ROWS);
      
      ResultSet rset = stmt.executeQuery(sql);
      
      List stats = new ArrayList(MAX_ROWS);
      
      List info = null;
      
      Author author = null;
      while (rset.next())
      {
         info = new ArrayList(2);
         author = new Author(rset.getString(1));
         author.setId(rset.getInt(2));
         info.add(author);
         info.add(rset.getString(3));
         stats.add(info);
      }
      
      return stats;
   }
   
}
