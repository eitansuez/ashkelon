package org.ashkelon.pages;

import org.ashkelon.db.*;
import org.ashkelon.util.*;

import java.util.*;
import java.sql.*;


public class AdminPage extends Page
{
   DBMgr dbmgr;
   
   public AdminPage()
   {
      super();
      dbmgr = DBMgr.getInstance();
   }
   
   public String init() throws SQLException
   {
      String command = ServletUtils.getRequestParam(request, "command");
      if ("resetconns".equals(command))
      {
         dbmgr.resetConnections();
      }
      else if ("resetcache".equals(command))
      {
         Enumeration attributes = app.getAttributeNames();
         while (attributes.hasMoreElements())
         {
            app.removeAttribute((String) attributes.nextElement());
         }
      }
      return null;
   }
}