/*
 * Created on Apr 12, 2005
 */
package org.ashkelon.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.ashkelon.db.DBUtils;
import org.ashkelon.util.Logger;

/*
 * 1. figure out how to verify correctness of setInternalRefs
 *   -- write unit tests
 * 
 * question:
 *  if leave out the update step altogether and drop the reffield by id
 *  mechanism and simply used the qualifiedname of the class as the foreign
 *  key (even though it's a string and not an int):  question:  would performance
 *  of the query app be very different??
 */

/**
 * @author Eitan Suez
 */
public class RefQuerySpec
{
   private boolean uniqueByIdField = true;
   private String table;
   private String classnamefield;
   private String reffield;
   private String idfield;
   
   private Logger log = Logger.getInstance();
   
   public static RefQuerySpec FIELDTYPE = 
      new RefQuerySpec("FIELD", "typename", "typeid", "id", true);
   
   public static RefQuerySpec METHODRETURNTYPE = 
      new RefQuerySpec("METHOD", "returntypename", "returntypeid", "id", true);
   
   public static RefQuerySpec IIMPLTYPE = 
      new RefQuerySpec("IMPL_INTERFACE", "name", "interfaceid", "classid", false);
   
   public static RefQuerySpec THROWNEXCEPTIONTYPE = 
      new RefQuerySpec("THROWNEXCEPTION", "name", "exceptionid", "throwerid", false);
   
   public static RefQuerySpec PARAMTYPE = 
      new RefQuerySpec("PARAMETER", "typename", "typeid", "execmemberid", false);
   
   public static RefQuerySpec SUPERTYPE = 
      new RefQuerySpec("SUPERCLASS", "name", "superclassid", "classid", false);
   
   
   
   public RefQuerySpec(String table, String classnamefield, 
                       String reffield, String idfield, boolean uniqueByIdField)
   {
      this.table = table;
      this.classnamefield = classnamefield;
      this.reffield = reffield;
      this.idfield = idfield;
      this.uniqueByIdField = uniqueByIdField;
   }
   
   public void setTypeRefs(Connection conn)
   {
      log.traceln("\tProcessing " + table + " references..");

      try
      {
         String query = 
            " select distinct c.id, c.qualifiedname from " + table + ", CLASSTYPE c " + 
            " where " + table + "." + reffield + " is null and " +
               table + "." + classnamefield + " = c.qualifiedname";
      
         PreparedStatement pstmt = conn.prepareStatement(query);
      
         // 1. measure query time
         long start = new Date().getTime();
         ResultSet rset = pstmt.executeQuery();
         long queryTime = new Date().getTime() - start;
         log.debug("query time: "+queryTime+" ms");
      
      
         String update = "update " + table + " set " + reffield + "=? where " + 
           classnamefield + "=? and " + reffield + " is null";
         PreparedStatement pstmt2 = conn.prepareStatement(update);
      
         // 2. measure total update time (a), number of update calls (b), and
         //   time per update (c = a/b)
         int n = 0;
         start = new Date().getTime();
         
         while (rset.next())
         {
            n++;
            pstmt2.setInt(1, rset.getInt(1));  // c.id
            pstmt2.setString(2, rset.getString(2));  // c.qualifiedname
            pstmt2.executeUpdate();
         }
      
         queryTime = new Date().getTime() - start;
         
         log.debug("total update time: "+queryTime+" ms");
         log.debug("number of times through loop: "+n);
         if (n > 0)
            log.debug("avg update time: "+(queryTime/n)+ " ms");
   
         pstmt2.close();
         rset.close();
         pstmt.close();
         
         conn.commit();
         log.verbose("Updated (committed) " + table + " references");
      }
      catch (SQLException ex)
      {
         log.error("Internal Reference Update Failed!");
         DBUtils.logSQLException(ex);
         log.error("Rolling back..");
         try
         {
            conn.rollback();
         }
         catch (SQLException inner_ex)
         {
            log.error("rollback failed!");
         }
      }

   }
   
}
