package org.ashkelon.db;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.sql.*;
import java.util.*;
import org.ashkelon.util.*;

/**
 * A class containing various static methods that simplify
 *  or encapsulate database calls including:
 *   database queries, batch executions (jdbc 2.0 only),
 *   inserts, deletes, getting sequence.nextval effortlessly,
 *   logging sql exceptions fully (works with org.ashkelon.util.Logger)
 *   and more.
 *
 * @author Eitan Suez
 */
public class DBUtils
{
   /**
    * inserts a record using conn into tableName.
    * @param conn connection handle
    * @param tableName table name
    * @param fieldInfo name/value pairs to insert
    */
   public static void insert(Connection conn, String tableName, Map fieldInfo) throws SQLException
   {
      String cmd = null;
      if (fieldInfo == null || fieldInfo.isEmpty())
      {
         throw new SQLException("Cannot insert a record without field information");
      }
      cmd = "INSERT INTO " + tableName;
      String fieldList = StringUtils.join(fieldInfo.keySet().toArray(), ",");
      cmd += " (" + fieldList + ")";
      cmd += " VALUES (" + StringUtils.join("?", ",", fieldInfo.size()) + ")";

      PreparedStatement pstmt = conn.prepareStatement(cmd);
      bind(pstmt, fieldInfo.values().toArray());
      pstmt.executeUpdate();
      pstmt.close();
   }
   
   /**
    * binds parameters to a prepared statement
    * @param pstmt the prepared statement
    * @param parms the list of objects to bind
    */
   public static void bind(PreparedStatement pstmt, Object[] parms) throws SQLException
   {
      for (int i=0; i<parms.length; i++)
      {
         if (parms[i] == null)
            parms[i] = "";
         // issue with a number of databases that cannot insert text containing the null character
         // go figure out why some javadoc comment in j2se actually encodes the null character in
         // a summary description!
         if (parms[i] instanceof String)
         {
           parms[i] = StringUtils.stripNull((String) parms[i]);
         }
         pstmt.setObject(i+1, parms[i]);
      }
   }
   
   /**
    * executes a query (select statement) where return fields and constraints are
    * somewhat parametrized
    * @param conn connection handle
    * @param tableName table name
    * @param returnFields list of return fields
    * @param constraints equality constraints (key is field name; value is constraint)
    * @param orderBy sort order specification
    *
    * @return a preparedstatement ready to executeQuery to be called on it.  caller
    *  must close preparedstatement of course.
    */
   public static PreparedStatement select(Connection conn, String tableName, String[] returnFields, Map constraints, String orderBy[])
        throws SQLException
   {
      String cmd = "SELECT ";
      if (returnFields == null || returnFields.length == 0)
      {
         cmd += "*";
      } else
      {
         cmd += StringUtils.join(returnFields, ",");
      }
      
      cmd += " FROM " + tableName;
      
      PreparedStatement pstmt = null;
      
      if (constraints!=null && !constraints.isEmpty())
      {
         cmd += " WHERE ";
         Set keyset = constraints.keySet();
         Iterator itr = keyset.iterator();
         ArrayList list = new ArrayList();
         while (itr.hasNext())
         {
            list.add(itr.next() + "=?");
         }
         cmd += StringUtils.join(list.toArray(), " AND ");
         
         pstmt = conn.prepareStatement(cmd);
         bind(pstmt, constraints.values().toArray());
      }
      
      if (orderBy!=null && orderBy.length>0)
      {
         cmd += " ORDER BY " + StringUtils.join(orderBy, ",");
      }
      
      if (pstmt == null)
         pstmt = conn.prepareStatement(cmd);
      
      return pstmt;
   }
   
   public static PreparedStatement select(Connection conn, String tableName, String[] returnFields, Map constraints)
        throws SQLException
   {
      return select(conn, tableName, returnFields, constraints, null);
   }
   
   public static PreparedStatement select(Connection conn, String tableName, String[] returnFields, String[] orderBy)
        throws SQLException
   {
      return select(conn, tableName, returnFields, null, orderBy);
   }
   
   public static PreparedStatement select(Connection conn, String tableName, Map constraints) throws SQLException
   {
      String[] returnFields = {"*"};
      return select(conn, tableName, returnFields, constraints, null);
   }
   
   public static int getCount(Connection conn, String tableName) throws SQLException
   {
      String sql = "select count(*) from " + tableName;
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      rset.next();
      int count = rset.getInt(1);
      rset.close();
      stmt.close();
      return count;
   }

   /**
    * @param tableName the table to query
    * @param constraints input to the construction of a where clause that defines the query
    *   to perform
    * @return whether a query to tableName with constraints returned any matching rows
    */
   public static boolean exists(Connection conn, String tableName, Map constraints)
        throws SQLException
   {
      PreparedStatement pstmt = select(conn, tableName, constraints);
      ResultSet rset = pstmt.executeQuery();
      boolean found = false;
      if (rset.next())
      {
         found = true;
      }
      rset.close();
      pstmt.close();
      return found;
   }
   
   /**
    * performs a select query against tableName using constraints, and retrieves targetField
    * for example: if have a table named 'address' with a field named 'city' a call such as:
    *   getObject(conn, "address", "city", constraints)
    * might return 'Milano' if constrains contains [the map equivalent of] COUNTRY="Italy".
    * @param tableName table name
    * @param targetField the target field
    * @param constraints query constraints
    */
   public static Object getObject(Connection conn, String tableName, String targetField, Map constraints) throws SQLException
   {
      String[] returnFields = new String[1];
      returnFields[0] = targetField;
      PreparedStatement pstmt = select(conn, tableName, returnFields, constraints);
      ResultSet rset = pstmt.executeQuery();
      Object result = null;
      if (rset.next())
      {
         result = rset.getObject(1);
      }
      rset.close();
      pstmt.close();
      return result;
   }
   
   /**
    * deletes rowos from tableName using constraints
    */
   public static int delete(Connection conn, String tableName, Map constraints) throws SQLException
   {
      String sql = "DELETE FROM " + tableName + " WHERE ";

      Set keyset = constraints.keySet();
      Iterator itr = keyset.iterator();
      ArrayList list = new ArrayList();
      while (itr.hasNext())
      {
         list.add(itr.next() + "=?");
      }
      sql += StringUtils.join(list.toArray(), " AND ");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      bind(pstmt, constraints.values().toArray());
      int num_deleted = pstmt.executeUpdate();
      pstmt.close();
      return num_deleted;
   }
   
   /**
    * fully logs a sql exception (sqlstate, error code, exception reason,
    *  chained exceptions, & stack traces using org.ashkelon.util.Logger
    */
   public static void logSQLException(SQLException ex)
   {
      Logger log = Logger.getInstance();
      log.error("Exception: "+ex.getMessage());
      log.error("Error Code: "+ex.getErrorCode());
      log.error("SQL State: "+ex.getSQLState());
      ex.printStackTrace(log.getWriter());

      // if out of database resources - exit
      if (ex.getErrorCode() == -1311 || ex.getErrorCode() == -1310)
      {
         System.exit(1);
      }
      
      while ((ex = ex.getNextException()) != null)
      {
         log.error("Chained sql exception:");
         logSQLException(ex);
      }
   }
   
   public static void logSQLException(SQLException ex, String sql)
   {
      Logger log = Logger.getInstance();
      log.error("Sql Statement: "+sql);
      logSQLException(ex);
   }
   
   /**
    * if need nextval for a sequence, this method will make the oracle database
    *  call (select sequence.nextval from dual) for you and return the nextVal
    * @param conn connection
    * @param sequence name of sequence
    * @return next value for sequence
    */
   public static int getNextVal(Connection conn, String sequence) throws SQLException
   {
      Statement stmt = conn.createStatement();
      String sql = "SELECT " + sequence + ".NEXTVAL FROM DUAL";
      ResultSet rset = stmt.executeQuery(sql);
      int nextVal = rset.getInt(1);
      rset.close();
      stmt.close();
      return nextVal;
   }

   /**
    * use to submit a list of statements in batch mode (at one time) to the database
    * uses jdbc 2.0 api
    */
   public static void submitBatch(Connection conn, List commands) throws SQLException
   {
      Logger log = Logger.getInstance();
      
      if (commands.isEmpty()) return;
      Statement stmt = conn.createStatement();
      
      // 2 ways to implement this:
      try
      {
         for (int i=0; i<commands.size(); i++)
            stmt.addBatch((String) commands.get(i));
         stmt.executeBatch();
      }
      catch (SQLException ex)
      {
         if (ex.getMessage().toLowerCase().indexOf("not implemented") == -1)
         {
            throw ex;
         }
         
         // batching not supported by jdbc driver, use alternate slow method
         for (int i=0; i<commands.size(); i++)
         {
            log.debug((String) commands.get(i));
            stmt.execute((String) commands.get(i));
         }
      }
      
      if (!conn.getAutoCommit())
         conn.commit();
      stmt.close();

   }
   
   
   public static Object getObject(Connection conn, String sql) throws SQLException
   {
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      Object result = rset.getObject(1);
      rset.close();
      stmt.close();
      return result;
   }
   
   // unit test
   public static void main(String args[]) throws Exception
   {
      Connection conn = DBMgr.getInstance().getConnection();
      /*
      List commands = new ArrayList(2);
      commands.add("select * from classtype where id = 101");
      submitBatch(conn, commands);
       */
      
      /* lesson learned from following test:
       *  with oracle, bassing a boolean parameters to store into a number() type succeeds
       *  with mysql, it succeeds but true values are inserted as 0 (false) -- basically doesn't work
       * moral of the story: pass Integer parameters if the underlying type is numeric, since booleans
       *  do not usually have their own types in databases (use numbers 0/1 for t/f)
       */
      Map fieldinfo = new HashMap();
      fieldinfo.put("id", new Integer(13176));
      fieldinfo.put("name", "eitan");
      fieldinfo.put("qualifiedname", "eitan suez");
      //fieldinfo.put("isstatic", new Boolean(true));
      fieldinfo.put("isstatic", new Integer(1));
      DBUtils.insert(conn, "member", fieldinfo);
      
      
      DBMgr.getInstance().releaseConnection(conn);
      
      
   }
   
}
