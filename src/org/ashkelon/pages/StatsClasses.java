package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;


public class StatsClasses extends Page
{
   public StatsClasses()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      List stats = getClassStats();
      request.setAttribute("classCounts", stats);
      return null;
   }
   

   /**
    * contrasting this implementation with the author stats -- author stats
    * are ordered by count whereas class stats (really package stats) are
    * ordered alphabetically while large packages are highlighted.  not sure
    * which is more desirable.  something to think about.
    */
   public List getClassStats() throws SQLException
   {
      /*
      String sql = 
         "select p.name, p.id, count(*) " +
         " from classtype c, package p" + 
         " where c.packageid=p.id " +
         " group by p.name order by p.name";
       */
      
      String sql = DBMgr.getInstance().getStatement("classstats");

      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List stats = new ArrayList(100);
      
      List info = null;
      
      JPackage pkg = null;
      while (rset.next())
      {
         info = new ArrayList(2);
         pkg = new JPackage(rset.getString(1));
         pkg.setId(rset.getInt(2));
         info.add(pkg);
         info.add(rset.getString(3));
         stats.add(info);
      }
      
      return stats;
   }
   
   
}
