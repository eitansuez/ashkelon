package org.ashkelon.pages;

import java.sql.*;

/** jsp's with no or empty associated java class can refer to
 *  this one in the config.xml file 
 */
public class NoopPage extends Page
{
   public NoopPage() throws SQLException
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
       return null;
   }
}
