package org.ashkelon.db;

import java.util.*;
import java.sql.*;
import org.ashkelon.util.*;

public class DBProc
{
   static Map scriptMap = null;
   private Logger log;
   
   public DBProc()
   {
      if (scriptMap == null)
      {
         scriptMap = new HashMap();
         scriptMap.put("reset", "org/ashkelon/db/ashkelon.sql");
         scriptMap.put("reset_seqs", "org/ashkelon/db/init.sql");
         scriptMap.put("add_idx", "org/ashkelon/db/ashkelon_addidx.sql");
         scriptMap.put("del_idx", "org/ashkelon/db/ashkelon_dropidx.sql");
      }
      log = Logger.getInstance();
      //log.setPrefix("DBProc");
      //log.setTraceLevel(Logger.DEBUG);
   }
   
   // for 8i lite, signature must be:
   //public static void entryPoint(Connection conn, String action)
   // for 8i standard/enterprise, signature must be:
   public static void entryPoint(String action)
   {
      DBProc proc = new DBProc();
      String script = (String) scriptMap.get(action);
      proc.log.debug("Script is: "+script);
      if (script == null) return;
      List commands = ScriptParser.parse(script);
      proc.log.debug("Commands length: "+commands.size());

      try
      {
         // this is how to get ref to conn using 8i standard/enterprise:
         Connection conn = DriverManager.getConnection("jdbc:default:connection:");
         
         Statement stmt = conn.createStatement();
         String command = "";
         for (int i=0; i<commands.size(); i++)
         {
            command = (String) commands.get(i);
            proc.log.debug("Cmd: "+command);
            try {  // desired behavior: if one statement fails, don't block rest
               stmt.execute(command);
            } catch (SQLException ex)
            {
               proc.log.error("Cmd Failed: "+command);
            }
         }
         stmt.close();
      } catch (SQLException ex)
      {
         proc.log.error("Failed: "+ex.getMessage());
         DBUtils.logSQLException(ex);
      }
   }
   
   public void doAction(Connection conn, String action) throws SQLException
   {
      String script = (String) scriptMap.get(action);
      if (script == null)
      {
         log.brief("cannot fulfill request to "+action);
         return;
      }
      // script = StringUtils.substitute(script, "\\$dbtype", DBMgr.getInstance().getDbtype());
      List commands = ScriptParser.parse(script);
      DBUtils.submitBatch(conn, commands);
   }
   
   public static void main(String args[]) throws SQLException
   {
      //DBMgr mgr = DBMgr.getInstance();
      //Connection conn = mgr.getConnection();
      if (args.length == 0)
      {
         Logger.getInstance().traceln("Must provide an action");
         return;
      }
      //entryPoint(conn, args[0]);
      entryPoint(args[0]);
      //mgr.releaseConnection(conn);
   }
   
}
