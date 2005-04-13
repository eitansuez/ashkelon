package org.ashkelon.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.ashkelon.db.DBMgr;
import org.ashkelon.util.Logger;


/**
 * @author Eitan Suez
 */
public class AncestorPopulator
{

    public AncestorPopulator() throws SQLException
    {
       DBMgr mgr = DBMgr.getInstance();
       Connection conn = mgr.getConnection();

       Logger logger = Logger.getInstance();

       String sql = "delete from CLASS_ANCESTORS";
       PreparedStatement pstmt = conn.prepareStatement(sql);
       int numAffected = pstmt.executeUpdate();
       logger.debug("cleared ancestor table, "+numAffected+" records deleted");
       
       sql = "insert into CLASS_ANCESTORS select CLASSID, SUPERCLASSID, 1 from SUPERCLASS where SUPERCLASSID is not null";
       pstmt = conn.prepareStatement(sql);
       numAffected = pstmt.executeUpdate();
       
       int nextLevel = 2;
       int previousLevel = 1;
       
       while (true)
       {
          sql = "delete from TEMP_DELTA";
          pstmt = conn.prepareStatement(sql);
          pstmt.executeUpdate();
          
          sql = "insert into TEMP_DELTA select a.CLASSID, c.SUPERCLASSID, ? " + 
            " from CLASS_ANCESTORS a, SUPERCLASS c where a.SUPERCLASSID=c.CLASSID and a.HIERARCHY = ?" + 
            " and c.SUPERCLASSID is not null";
          
          logger.debug("sql: "+sql);
          pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, nextLevel);
          pstmt.setInt(2, previousLevel);
          numAffected = pstmt.executeUpdate();
          
          logger.debug("pass # "+previousLevel+": inserted "+numAffected+" records");

          if (numAffected == 0)
             break;

          sql = "insert into CLASS_ANCESTORS select CLASSID, SUPERCLASSID, HIERARCHY from TEMP_DELTA";
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
