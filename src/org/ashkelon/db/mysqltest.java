/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

package org.ashkelon.db;

/*
 * mysqltest.java
 * Created on September 19, 2001, 1:13 AM
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
