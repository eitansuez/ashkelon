package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;

/**
 * @author Eitan Suez
 */
public class APIsPage extends Page
{
   public APIsPage()
   {
      super();
   }

   public String handleRequest() throws SQLException
   {
      if (app.getAttribute("apilist")==null)
      {
         List apiList = getAPIList();
         app.setAttribute("apilist", apiList);
         if (apiList == null || apiList.size() <= 1)
            return "pkg";
      }
      
      return null;
   }
   
   public List getAPIList() throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getapis");

      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List apiList = new ArrayList();

      API api;
      while (rset.next())
      {
         api = new API(rset.getString(2));
         api.setId(rset.getInt(1));
         api.setSummaryDescription(rset.getString(3));
         api.setPublisher(rset.getString(4));
         api.setVersion(rset.getString(5));
         apiList.add(api);
      }
      
      rset.close();
      stmt.close();
      
      return apiList;
   }
   
}
