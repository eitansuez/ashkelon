package org.ashkelon.db;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.*;
import java.sql.*;

import org.ashkelon.util.*;

/**
 * A singleton class that provides function analogous to database "sequences".
 * It assumes a table has been created named 'seqs' as follows:  CREATE TABLE
 * seqs(seqname VARCHAR2(80) PRIMARY KEY, seqval INTEGER); and that the proper
 * sequence records and starting values have already been inserted.
 *
 * It takes care of returning either currVal or nextVal for any given sequence
 * name. You must call save() when done to commit back to database.
 *
 * Default sequence step level is 1, incrementing.
 *
 * @author Eitan Suez
 */
public class PKManager
{
   private static PKManager instance = null;
   
   private Logger log;
   private DBMgr mgr;
   
   private Map indices;
   private int step = 1;
   private boolean autoCommit = false;
   
   //private static String UDPATE_SEQ = "UPDATE SEQS SET SEQVAL=? WHERE SEQNAME=?";
   //private static String SELECT_SEQS = "SELECT * FROM SEQS";
   //private static String ADD_SEQ = "INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES (?, ?)";
   private String UPDATE_SEQ;
   private String SELECT_SEQS;
   private String ADD_SEQ;
   
   public static PKManager getInstance()
   {
      if (instance == null)
      {
         instance = new PKManager();
      }
      return instance;
   }
   
   private PKManager()
   {
      log = Logger.getInstance();
      
      mgr = DBMgr.getInstance();
      
      Connection conn = null;
      try
      {
         conn = mgr.getConnection();
         log.debug("connection established to: "+mgr.getConnectionURL());

         UPDATE_SEQ = mgr.getStatement("update_seq");
         SELECT_SEQS = mgr.getStatement("get_seqs");
         ADD_SEQ = mgr.getStatement("add_seq");
         
         Statement statement = conn.createStatement();
         ResultSet rs = statement.executeQuery(SELECT_SEQS);
         indices = new HashMap(20);
         while (rs.next())
         {
            String indexName = rs.getString("SEQNAME");
            Integer indexValue = new Integer(rs.getInt("SEQVAL"));
            indices.put(indexName, indexValue);
         }
         rs.close();
         statement.close();
         mgr.releaseConnection(conn);
      } catch (SQLException ex)
      {
         log.error("Exiting: failed to load indices");
         DBUtils.logSQLException(ex, SELECT_SEQS);
         if (conn != null)
            mgr.releaseConnection(conn);
         // System.exit(1);
      }
   }
   
  /**
   * updates sequences in database.  must be called at end of use to maintain
   * integrity.
   */
   public void save()
   {
      Connection conn = null;
      try
      {
         conn = mgr.getConnection();
         //boolean saved = conn.getAutoCommit();
         conn.setAutoCommit(false);
         PreparedStatement pstmt = conn.prepareStatement(UPDATE_SEQ);
         
         Set set = indices.entrySet();
         Iterator iter = set.iterator();
         Map.Entry entry = null;
         while (iter.hasNext())
         {
            entry = (Map.Entry) iter.next();
            pstmt.clearParameters();
            pstmt.setInt(1, ((Integer) entry.getValue()).intValue());
            pstmt.setString(2, (String) entry.getKey());
            pstmt.executeUpdate();
         }
         
         pstmt.close();
         conn.commit();
         //conn.setAutoCommit(saved);
      } catch (SQLException ex)
      {
         log.error("Error occurred attempting to write table index counts");
         DBUtils.logSQLException(ex, UPDATE_SEQ);
      }
      finally
      {
         if (conn != null)
            mgr.releaseConnection(conn);
      }
   }
   
   private int getIndex(String name)
   {
      return ((Integer) indices.get(name)).intValue();
   }
   private void setIndex(String name, int index)
   {
      indices.put(name, new Integer(index));
      if (autoCommit)
      {
         /*
         try
         {
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL);
            pstmt.setInt(1, index);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            pstmt.close();
            conn.commit();
         } catch (SQLException ex)
         {
            log.tracelnError("Error occurred attempting to commit nextval for sequence "+name);
            DBUtils.logSQLException(ex);
         }
          **/
      }
   }
   
   /**
    * get current value for sequence name
    * @param name sequence name
    */
   public int currVal(String name)
   {
      return getIndex(name);
   }
   
   /**
    * get next value for sequence name.  default step level is 1 (incrementing).
    * @param name sequence name
    */
   public int nextVal(String name)
   {
      int nextVal = currVal(name) + step;
      setIndex(name, nextVal);
      return nextVal;
   }
   
   /**
    * revise step (or increment) value
    */
   public void setStep(int step)
   {
      this.step = step;
   }
   public int getStep()
   {
      return step;
   }

   /*
   public void setAutoCommit(boolean autoCommit)
   {
      this.autoCommit = autoCommit;
   }
    */
   public boolean isAutoCommit()
   {
      return autoCommit;
   }
   
   
   public void addSequence(Connection conn, String seqName, int startvalue) throws SQLException
   {
      PreparedStatement pstmt = conn.prepareStatement(ADD_SEQ);
      pstmt.setString(1, seqName);
      pstmt.setInt(2, startvalue);
      pstmt.executeUpdate();
      pstmt.close();
   }
   
}
