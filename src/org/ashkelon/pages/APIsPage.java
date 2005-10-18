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
   public APIsPage() { super(); }

   public String handleRequest() throws SQLException
   {
      boolean cacheEmpty = cacheEmpty();
      int currentMemberCount = fetchMemberCount();
      boolean cacheDirty = cacheDirty(currentMemberCount);
      
      if (cacheEmpty || cacheDirty)
      {
         List apiList = getAPIList();
         app.setAttribute("apilist", apiList);
         app.setAttribute("membercount", new Integer(currentMemberCount));
         
         if (apiList == null || apiList.size() <= 1)
            return "pkg";
      }
      
      return null;
   }
   
   private boolean cacheEmpty()
   {
      return ( app.getAttribute("apilist") == null );
   }
   
   protected boolean cacheDirty(int currentMemberCount)
   {
      if ( app.getAttribute("membercount") == null )
         return true;
      
      int memberCount = ((Integer) app.getAttribute("membercount")).intValue();
      return (memberCount != currentMemberCount);
   }
   
   private int fetchMemberCount() throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getmembercount");
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      rset.next();
      return rset.getInt(1);
   }

   
   public List getAPIList() throws SQLException
   {
      log.brief("Fetching APIs Listing..");
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
