package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;

import java.util.*;
import java.sql.*;


/**
 * @author Eitan Suez
 */
public class APIPage extends Page
{
   public APIPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      int apiId = Integer.parseInt(ServletUtils.getRequestParam(request, "id"));
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
         if (api_object != null)
         {
            API api = (API) api_object;
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
