package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

public class StatsAuthors extends Page
{
   public StatsAuthors()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      request.setAttribute("classCounts", getAuthorStats());
      return null;
   }
   
   
   public List getAuthorStats() throws SQLException
   {
      /*
      String sql = 
         " select * from ( " + 
         "  select a.name, count(*) " +
         "  from classtype c, author a, class_author ca " + 
         "  where c.id=ca.classid and a.id=ca.authorid " +
         "   group by a.name order by count(*) desc " +
         " ) where rownum <= 50 ";
       */
      String sql = DBMgr.getInstance().getStatement("authorstats");
      
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List stats = new ArrayList(50);
      
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
