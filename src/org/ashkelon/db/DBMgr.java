package org.ashkelon.db;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.ashkelon.util.Logger;

/**
 * A singleton class for managing database connections.
 *
 * This class currently designed to read connection information from the
 * properties resource bundle org.ashkelon.db.conn-info providing values for the
 * following keys: jdbcDriverName, connectionURL, user, password.
 *
 * for example:
 *   jdbcDriverName=oracle.lite.poljdbc.POLJDBCDriver
 *   connectionURL=jdbc:Polite:ashkelon
 *   user=system
 *   password=passwd
 *
 * will probably be revised at a later time to provide this info more
 * flexibly
 *
 * @author Eitan Suez
 */
public class DBMgr
{
   private static DBMgr _instance = null;

   private String dbtype;
   private String jdbcDriverName;
   private String connectionURL;
   private String user;
   private String password;
   
   private boolean targetSet = false;
   private String defaultTarget = "org.ashkelon.db.conn-info";

   private ResourceBundle statements;
   
   private Logger log;
   
   private Map pool;
   private int maxpoolsize = 20;
   
   private static final int BUSY = 1;
   private static final int FREE = 2;
   
   
   private DBMgr()
   {
      log = Logger.getInstance();
   }

   public void setTarget(String resourceName)
   {
      if (targetSet) { return; } // don't allow switching of targets (for now)
      
      log.verbose("loading connection settings from "+resourceName);
      
      ResourceBundle bundle = PropertyResourceBundle.getBundle(resourceName);
      setTarget(bundle);
   }
   
   public void setTarget(ResourceBundle connectionBundle) 
   {
      if (targetSet) { return; } // don't allow switching of targets (for now)
      
      loadConnectionInfo(connectionBundle);
      statements = PropertyResourceBundle.getBundle("org.ashkelon.db.statements");
   
      log.verbose("Connection url is: "+connectionURL);
      log.verbose("User is: "+user);
      //log.verbose("password is: "+password);
   
      loadDriver();
   
      log.verbose("jdbc driver: " + jdbcDriverName + " loaded");
   
      pool = new HashMap(maxpoolsize);
      targetSet = true;
   }
   
   public static DBMgr getInstance()
   {
      if (_instance == null)
      {
         _instance = new DBMgr();
      }
      return _instance;
   }
   
   private void loadConnectionInfo(ResourceBundle bundle) throws MissingResourceException
   {
      dbtype = bundle.getString("dbtype");
      jdbcDriverName = bundle.getString("jdbcDriverName");
      connectionURL = bundle.getString("connectionURL");
      
      if (webContext && connectionURL.equals("jdbc:mckoi:local://./db.conf"))
      {
         // need to translate relative path to an absolute path to
         // resolve the proper location of the db
         connectionURL = "jdbc:mckoi:local://"+realPath+"WEB-INF/db/db.conf";
         // decision to place db in WEB-INF/db is simply a convention
      }
      
      user = bundle.getString("user");
      password = bundle.getString("password");
   }
   
   public String getStatement(String key)
   {
      try
      {
         return statements.getString(key);
      }
      catch (MissingResourceException ex)
      {
         log.error("No sql statement corresponding to key: "+key);
         return "";
      }
   }
   
   private String realPath = "";
   private boolean webContext = false;
   public void setWebApp(javax.servlet.ServletContext webapp)
   {
      webContext = true;
      realPath = webapp.getRealPath("/");
      log.brief("Running in a web context.  Real Path is: "+realPath);
   }
   
   private void loadDriver()
   {
      try
      {
         Class.forName(jdbcDriverName);
      }
      catch (ClassNotFoundException ex)
      {
         log.error("ClassNotFoundException: "+ex.getMessage());
         // System.exit(1);
      }
   }
   
   public synchronized void setPoolSize(int poolsize)
   {
      maxpoolsize = poolsize;
   }
   
   /**
    * @return a java.sql.connection object
    */
   public synchronized Connection getConnection() throws SQLException
   {
      if (!targetSet)  // use default
      {
         setTarget(defaultTarget);
      }
      
      if (haveAConnection())
      {
         PooledConnection conn = getAvailableConnection();
         conn.setState(BUSY);
         getPoolStatus();
         return conn.getConnection();
      }
      else
      {
         if (pool.size() >= maxpoolsize)
         {
            getPoolStatus();
            throw new SQLException("org.ashkelon.db.DBMgr: No more connections available");
         }
         else
         {
            PooledConnection conn = makeNewConnection();
            conn.setState(BUSY);
            pool.put(conn.getConnection(), conn);
            getPoolStatus();
            return conn.getConnection();
         }
      }
   }
   

   private boolean haveAConnection()
   {
      if (pool.size() > maxpoolsize)
      {
         Collection conns = pool.values();
         Iterator itr = conns.iterator();
         PooledConnection pc;
         int busycount = 0;
         while (itr.hasNext())
         {
            pc = (PooledConnection) itr.next();
            if (pc.getState() == BUSY)
            {
               busycount++;
            }
         }
         if (busycount > maxpoolsize)
         {
            return false;
         }
      }
      
      Collection conns = pool.values();
      Iterator itr = conns.iterator();
      PooledConnection pc;
      while (itr.hasNext())
      {
         pc = (PooledConnection) itr.next();
         if (pc.getState() == FREE)
         {
            return true;
         }
      }
      return false;
   }
   
   
   private PooledConnection getAvailableConnection()
   {
      Collection conns = pool.values();
      Iterator itr = conns.iterator();
      PooledConnection pc;
      while (itr.hasNext())
      {
         pc = (PooledConnection) itr.next();
         if (pc.getState() == FREE)
         {
            return pc;
         }
      }
      return null;  // what's the saying?  somewhere along the way something  went terribly wrong.
   }
   
   private PooledConnection makeNewConnection() throws SQLException
   {
      Connection conn = DriverManager.getConnection(connectionURL, user, password);
      conn.setAutoCommit(false);
      return new PooledConnection(conn);
   }
   
   
   public synchronized void releaseConnection(Connection conn)
   {
      PooledConnection pc = (PooledConnection) pool.get(conn);
      if (pc != null)
         pc.setState(FREE);
   }
   
   public void resetConnections()
   {
      synchronized(this)
      {
         int numreset = 0;
         int numfailed = 0;
         log.traceln("about to reset connections");
         // close all connections
         Collection conns = pool.keySet();
         Iterator itr = conns.iterator();
         Connection c;
         while (itr.hasNext())
         {
            c = (Connection) itr.next();
            try {
               c.close();
               numreset++;
            } catch (SQLException ex)
            {
               numfailed++;
            }
         }
         log.traceln("successfully reset "+numreset+" connections");
         log.traceln("failed to reset "+numreset+" connections");
         pool = new HashMap(maxpoolsize);
      }
   }
   
   public String getPoolStatus()
   {
      Iterator itr = pool.values().iterator();
      PooledConnection pc = null;
      int numUsed = 0;
      int numFree = 0;
      while (itr.hasNext())
      {
         pc = (PooledConnection) itr.next();
         if (pc.getState() == BUSY) {
             numUsed++; 
         } else {
             numFree++;
         }
      }
      String statusmsg = "Used/Free/Max: " + numUsed + "/" + numFree + "/" + maxpoolsize;
      log.verbose(statusmsg);
      return statusmsg;
   }
   
   protected void finalize() throws Throwable
   {
      resetConnections();
      pool = null;
      log.verbose("DBMgr finalized");
      log = null;
   }
   
   public String getDbtype()
   {
      return dbtype;
   }
   public boolean isOracle() { return "oracle".equals(dbtype); }
   public boolean isAnsiSql()
   {
      return "mysql".equals(dbtype) || "postgres".equals(dbtype);
   }
   
   /**
    * get the value of the connectionURL in use
    */
   public String getConnectionURL()
   {
      return connectionURL;
   }
   /**
    * get the value of the jdbc driver name in use
    */
   public String getJdbcDriverName()
   {
      return jdbcDriverName;
   }

   class PooledConnection
   {
      private Connection conn;
      private int state;

      public PooledConnection(Connection conn)
      {
         setConnection(conn);
         setState(FREE);
      }

      public int getState() { return state; }
      public void setState(int state) { this.state = state; }

      public Connection getConnection() { return conn; }
      private void setConnection(Connection conn) { this.conn = conn; }
   }

}
