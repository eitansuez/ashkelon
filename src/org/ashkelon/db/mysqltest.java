/*
 * mysqltest.java
 * Created on September 19, 2001, 1:13 AM
 */

package org.ashkelon.db;

import java.sql.*;

/**
 * @author  Eitan
 * @version 
 */
public class mysqltest
{

   public mysqltest() throws Exception
   {
      Class.forName("org.gjt.mm.mysql.Driver"); // load mysql mm driver
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test");
      
      Statement stmt = conn.createStatement();
      String sql = "select name, age from test order by age desc";
      ResultSet rset = stmt.executeQuery(sql);

      String name;
      int age;
      
      while (rset.next())
      {
         name = rset.getString(1);
         age = rset.getInt(2);
         System.out.println(name + " is " + age + " years old");
      }
      
      rset.close();
      stmt.close();
      conn.close();
   }
   
   public static void oratest() throws Exception
   {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ashkelon", "system", "manager");
      
      String sql = "insert into temp (fld) values (?)";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      try
      {
         pstmt.setString(1, "thisoneistoolong");
         pstmt.executeUpdate();
      }
      catch (SQLException ex)
      {
         pstmt.setString(1, "valueok");
         pstmt.executeUpdate();
      }

      pstmt.close();
      conn.close();
   }
   
   public static void main(String[] args) throws Exception
   {
      //new mysqltest();
      oratest();
   }

}
