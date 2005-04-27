package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.PKManager;
import org.ashkelon.manager.Repository;
import org.ashkelon.util.*;
import java.sql.*;
import java.util.*;


/**
 * @author Eitan Suez
 */
public class APIEditPage extends Page
{
   public APIEditPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      String param = ServletUtils.getRequestParam(request, "id");
      
      API api = null;
      String msg = "";
      
      //if (StringUtils.isBlank(param))
      if (true)
      {
         api = new API();
      }
      else
      {
         int apiId = Integer.parseInt(param);
         api = API.makeAPIFor(conn, apiId);
      }
      
      // check cmd:  if submission, then bindtoui and store
      String action = ServletUtils.getRequestParam(request, "action");
      if ("Submit Request".equals(action))
      {
         bindFromUI(api);
         api.store(conn);
         PKManager.getInstance().save();  // must do this
          // (it's time to switch to hibernate..)
         
         msg = "API "+api.getName()+" has been saved.";
         
         if (api.isPopulated())
         {
            // remove api from cache (setdirty), invalidate apis listing
            Object apis = app.getAttribute("apis");
            if (apis != null)
            {
               Hashtable apisTable = (Hashtable) apis;
               Object key = new Integer(api.getId());
               apisTable.remove(key);
            }
            app.setAttribute("apilist", null);
         }
      }

      request.setAttribute("api", api);
      request.setAttribute("msg", msg);
      request.setAttribute("actions", api.getActions());

      return null;
   }
   
   private void bindFromUI(API api)
   {
      String idString = ServletUtils.getRequestParam(request, "api-id");
      int id = Integer.parseInt(idString);
      if (id > 0)
      {
         api.setId(id);
      }
      String populatedString = ServletUtils.getRequestParam(request, "populated");
      api.setPopulated(Integer.parseInt(populatedString));
      
      api.setName(ServletUtils.getRequestParam(request, "api-name"));
      api.setSummaryDescription(ServletUtils.getRequestParam(request, "summary-description"));
      api.setDescription(ServletUtils.getRequestParam(request, "full-description"));
      api.setPublisher(ServletUtils.getRequestParam(request, "publisher"));
      api.setDownloadURL(ServletUtils.getRequestParam(request, "url"));
      
//      ServletUtils.getRequestParam(request, "release-date");
//      api.setReleaseDate();
      
      api.setVersion(ServletUtils.getRequestParam(request, "version"));
      
      String pkgNames = ServletUtils.getRequestParam(request, "package-list");
      StringTokenizer tokenizer = new StringTokenizer(pkgNames, " \n\t", false);
        // false means:  don't return delimiters as tokens
      Collection pkgNamesCollection = new HashSet();
      while (tokenizer.hasMoreTokens())
      {
         pkgNamesCollection.add(tokenizer.nextToken());
      }
      api.setPackagenames(pkgNamesCollection);
      
      String type = ServletUtils.getRequestParam(request, "repository-type");
      String url = ServletUtils.getRequestParam(request, "repository-url");
      String modulename = ServletUtils.getRequestParam(request, "repository-module");
      String tagname = ServletUtils.getRequestParam(request, "repository-revision");
      String srcpath = ServletUtils.getRequestParam(request, "repository-sourcepath");
      Repository repos = new Repository(type, url, modulename, tagname, srcpath);
      
      api.setRepository(repos);
   }

}
