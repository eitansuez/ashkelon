package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.DBMgr;
import org.ashkelon.util.*;

import java.util.*;
import java.sql.*;


/**
 * @author Eitan Suez
 */
public class APIPage extends Page
{
   public APIPage() { super(); }

   public String handleRequest() throws SQLException
   {
      int apiId = 0;
      
      try
      {
         apiId = Integer.parseInt(ServletUtils.getRequestParam(request, "id"));
      }
      catch (NumberFormatException ex)
      {
         String apiName = ServletUtils.getRequestParam(request, "name");
         
         String sql = DBMgr.getInstance().getStatement("getapiid");
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, apiName);
         ResultSet rset = pstmt.executeQuery();
         log.debug("about to get api id for "+apiName);

         try
         {
            if (rset.next())
            {
               apiId = rset.getInt(1);
               log.debug("api id is: "+apiId);
            }
            else
            {
               // done: no such api..
               request.setAttribute("title", "API " + apiName + " Not Found");
               String description = "ashkelon does not appear to contain the api " + 
                   "<span class=\"api\">" + apiName + "</span>" + 
                   " in its repository";
               request.setAttribute("description", description);
               return "apis";
            }
         }
         finally
         {
            rset.close();
            pstmt.close();
         }
      }
      
      
      Integer apiId_obj = new Integer(apiId);

      Object apis_obj = app.getAttribute("apis");
      Hashtable apis = null;
      if (apis_obj == null)
      {
         apis = new Hashtable();
      }
      else
      {
         apis = (Hashtable) apis_obj;
         Object api_object = apis.get(apiId_obj);
         if (api_object != null /* TODO:  && !dirty(apiId_obj) */)
         {
            API api = (API) api_object;
            log.brief("retrieving api "+api.getName()+" from cache");
            request.setAttribute("api", api);
            return null;
         }
      }
       
      API api = API.makeAPIFor(conn, apiId);
      apis.put(apiId_obj, api);
      app.setAttribute("apis", apis);
      request.setAttribute("api", api);

      return null;
   }

}
