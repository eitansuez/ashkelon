/*
 * AncestorPopulator.java
 *
 * Created on November 7, 2001, 12:22 AM
 */

package org.ashkelon;

import org.ashkelon.db.*;
import java.sql.*;
import org.ashkelon.util.*;


public class AncestorPopulator
{

    public AncestorPopulator() throws SQLException
    {
       DBMgr mgr = DBMgr.getInstance();
       Connection conn = mgr.getConnection();

       Logger logger = Logger.getInstance();
       logger.setTraceLevel(Logger.DEBUG);

       String sql = "DELETE FROM CLASS_ANCESTORS";
       PreparedStatement pstmt = conn.prepareStatement(sql);
       int numAffected = pstmt.executeUpdate();
       logger.debug("cleared ancestor table, "+numAffected+" records deleted");
       
       sql = "INSERT INTO CLASS_ANCESTORS SELECT CLASSID, SUPERCLASSID, 1 FROM SUPERCLASS WHERE SUPERCLASSID IS NOT NULL";
       pstmt = conn.prepareStatement(sql);
       numAffected = pstmt.executeUpdate();
       
       int nextLevel = 2;
       int previousLevel = 1;
       
       while (true)
       {
          sql = "DELETE FROM TEMP_DELTA";
          pstmt = conn.prepareStatement(sql);
          pstmt.executeUpdate();
          
          sql = "INSERT INTO TEMP_DELTA SELECT A.CLASSID, C.SUPERCLASSID, "+nextLevel+
           " FROM CLASS_ANCESTORS A, SUPERCLASS C WHERE A.SUPERCLASSID = C.CLASSID AND A.HIERARCHY = " + previousLevel
             + " AND C.SUPERCLASSID IS NOT NULL";
          logger.debug("sql: "+sql);
          pstmt = conn.prepareStatement(sql);
          numAffected = pstmt.executeUpdate();
          
          logger.debug("pass # "+previousLevel+": inserted "+numAffected+" records");

          if (numAffected == 0)
             break;

          sql = "INSERT INTO CLASS_ANCESTORS SELECT CLASSID, SUPERCLASSID, HIERARCHY FROM TEMP_DELTA";
          pstmt = conn.prepareStatement(sql);
          pstmt.executeUpdate();

          previousLevel = nextLevel;
          nextLevel++;
       }
       
       pstmt.close();
       
       mgr.releaseConnection(conn);
       mgr = null;
    }
    
    public static void main(String args[]) throws Exception
    {
       new AncestorPopulator();
    }

}
