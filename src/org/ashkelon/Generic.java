package org.ashkelon;

import java.util.*;
import java.sql.*;

public class Generic
{
   public static List listNames(Connection conn, String tablename) throws SQLException
   {
      String sql = "select name from " + tablename + " order by name";
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      List pkgs = new ArrayList();
      while (rset.next())
      {
         pkgs.add(rset.getString(1));
      }
      rset.close();
      stmt.close();
      return pkgs;
   }
}
