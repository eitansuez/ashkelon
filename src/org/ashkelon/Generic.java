package org.ashkelon;

import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;

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
