package org.ashkelon.pages;

import org.ashkelon.util.*;
import java.sql.*;


public class SearchPage extends Page
{
   public SearchPage() throws SQLException
   {
      super();
   }
   
   public String init() throws SQLException
   {
      String simple = ServletUtils.getRequestParam(request, "simple");
      if (StringUtils.isBlank(simple))  // to distinguish from initial page visit and actual search form submissions
      {
         return null;
      }

      String searchType = ServletUtils.getRequestParam(request, "srch_type");
      if ("class".equals(searchType))
      {
         return "cls";
      }
      else
      {
         return "member";
      }
   }
}
