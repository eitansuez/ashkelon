package org.ashkelon.pages;

import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

public class StatsPage extends Page
{
   public StatsPage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      if (app.getAttribute("api_count")==null)
      {
         int apis = DBUtils.getCount(conn, "API");
         int packages = DBUtils.getCount(conn, "PACKAGE");
         int classes = DBUtils.getCount(conn, "CLASSTYPE");
         int members = DBUtils.getCount(conn, "MEMBER");

         app.setAttribute("api_count", new Integer(apis));
         app.setAttribute("package_count", new Integer(packages));
         app.setAttribute("class_count", new Integer(classes));
         app.setAttribute("member_count", new Integer(members));
      }
      
      List apipkgcounts = getCountsFor("apistatspkg");
      List apiclscounts = getCountsFor("apistatscls");
      List apimmbcounts = getCountsFor("apistatsmmb");
      
      request.setAttribute("apipkgcounts", apipkgcounts);
      request.setAttribute("apiclscounts", apiclscounts);
      request.setAttribute("apimmbcounts", apimmbcounts);

      return null;
   }



   private List getCountsFor(String stmt_key) throws SQLException
   {
      DBMgr dbmgr = DBMgr.getInstance();
      String sql = dbmgr.getStatement(stmt_key);
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List counts = new ArrayList(20);
      List pair;
      
      while (rset.next())
      {
         pair = new ArrayList(2);
         pair.add(new Integer(rset.getInt(1)));
         pair.add(rset.getString(2));
         counts.add(pair);
      }
      
      rset.close();
      stmt.close();
      
      return counts;
   }


}

