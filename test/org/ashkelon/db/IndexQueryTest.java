/*
 * Created on Aug 19, 2004
 */
package org.ashkelon.db;

import java.sql.*;
import java.util.Date;

/**
 * @author Eitan Suez
 */
public class IndexQueryTest
{
   private static int FETCH_SIZE = 20;
   
   public static void main(String[] args) throws Exception
   {
      String query = 
         " select " + 
         " m.id, m.qualifiedname, m.type, m.isstatic, " + 
         " m.isfinal, m.accessibility, m.modifier, meth.isabstract, " + 
         " meth.returntypeid, meth.returntypename, meth.returntypedimension, " + 
         " em.signature, d.summarydescription, d.since, d.deprecated " + 
         " from METHOD meth right outer join MEMBER m on meth.id=m.id " + 
         " left outer join EXECMEMBER em on em.id=m.id, DOC d " + 
         " where m.docid = d.id order by m.name";
      
      Connection conn = DBMgr.getInstance().getConnection();
      PreparedStatement p = conn.prepareStatement(query, 
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      p.setFetchSize(FETCH_SIZE);
      
      java.util.Date start = new java.util.Date();
      ResultSet rset = p.executeQuery();
      long millis = new Date().getTime() - start.getTime();
      int secs = (int) (millis / 1000);
      System.out.println("query execution time: "+secs+" seconds");
      //System.out.println("query execution took: "+millis+" ms");
      
      rset.close();
      /*
      int i=1;
      while (rset.next())
      {
         System.out.println(i+". " + rset.getString(2));
         i++;
      }
      */
   }
   
}
