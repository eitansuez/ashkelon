package org.ashkelon.pages;

import org.ashkelon.util.*;
import java.sql.*;

/**
 * @author Eitan Suez
 */
public class ConfigPage extends Page
{
   public ConfigPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      String ui_config = ServletUtils.getRequestParam(request, "ui_config");
      String new_ui = "";
      if ("classic".equals(ui_config))
      {
         new_ui = "basic/";
      }
      else if ("modern".equals(ui_config))
      {
         new_ui = "";
      }
      else if ("xul".equals(ui_config))
      {
         new_ui = "xul/";
      }
      else
      {
         return null;
      }
      
      String previous_ui = (String) session.getAttribute("ui");
      if (!previous_ui.equals(new_ui))
      {
         session.setAttribute("ui", new_ui);
         return "config";
      }
      return null;
   }
}
