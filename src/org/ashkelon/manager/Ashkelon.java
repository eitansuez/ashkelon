/*
 * Created on Mar 19, 2005
 */
package org.ashkelon.manager;

import java.sql.*;
import java.util.*;

import org.ashkelon.API;
import org.ashkelon.Generic;
import org.ashkelon.JPackage;
import org.ashkelon.db.*;
import org.ashkelon.util.*;
import com.sun.javadoc.*;

/**
 * @author Eitan Suez
 */
public class Ashkelon extends Doclet
{
   private RootDoc root = null;
   private DBMgr dbmgr;
   private Connection conn;
   private Logger log;
   private PKManager pkmgr;
   private DBProc proc;


   /** required for Doclet inheritance */
   public static boolean start(RootDoc root)
   {
      boolean verbose = StringUtils.getCommandLineOption("-verbose", root.options());
      boolean debug = StringUtils.getCommandLineOption("-debug", root.options());
      Logger log = Logger.getInstance();
      
      if (verbose)
         log.setTraceLevel(Logger.VERBOSE);
      if (debug)
         log.setTraceLevel(Logger.DEBUG);
      
      Ashkelon ashkelonDoclet = new Ashkelon(root);
      if (!ashkelonDoclet.init())
         return false;
      
      ashkelonDoclet.doAdd();

      return ashkelonDoclet.finish();
   }
   
   public Ashkelon()
   {
      log = Logger.getInstance();
      log.setPrefix("Ashkelon");
   }
   
   public Ashkelon(RootDoc root)
   {
      this();
      this.root = root;
   }
   
   public boolean init()
   {
      dbmgr = DBMgr.getInstance();
      conn = null;
      try
      {
         conn = dbmgr.getConnection();
         conn.setAutoCommit(false);
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
         if (conn != null)
            dbmgr.releaseConnection(conn);
         return false;
      }
      pkmgr = PKManager.getInstance();
      proc = new DBProc();
      return true;
   }
   
   public boolean finish()
   {
      dbmgr.releaseConnection(conn);
      pkmgr.save();
      return true;
   }
   
   protected void finalize() throws Throwable
   {
      root = null;
      if (conn!=null)
         dbmgr.releaseConnection(conn);
      conn = null;
      dbmgr = null;
      log = null;
      pkmgr = null;
   }
   
   private void doAdd()
   {
      long start = new java.util.Date().getTime();
      
      boolean refsonly = StringUtils.getCommandLineOption("-refsonly", root.options());
      boolean norefs = StringUtils.getCommandLineOption("-norefs", root.options());

      if(!refsonly)
      {
         int apiId = StringUtils.getCommandLineOption("-api", root.options(), -1);
         
         if (apiId == -1)
         {
            log.error("Failed to resolve api in db");
            return;
         }
         
         try
         {
            API api = API.makeAPIFor(conn, apiId);
            if (api == null || root.specifiedPackages().length == 0)
            {
               log.error("No api to add (or 0 packages)..exiting.");
               return;
            }
            populateAPI(api);
         }
         catch (SQLException ex)
         {
            log.error("Failed to load api (id "+apiId+") from db..");
            log.error("SQLException: "+ex.getMessage());
            return;
         }
         
      }

      long addtime = new java.util.Date().getTime() - start;
      log.traceln("Add Time: "+addtime/1000+" seconds");
      
      if (!norefs)
      {
         updateInternalRefs();
      }
      
      long reftime = new java.util.Date().getTime() - start - addtime;
      log.traceln("Ref. Time: "+reftime/1000+" seconds");

      try
      {
         conn.commit();
      }
      catch (SQLException ex)
      {
         log.error("jdbc commit failed");
         DBUtils.logSQLException(ex);
      }
      log.traceln("done");
   }
   
   
   private void populateAPI(API api)
   {
      try
      {
         if (api.isPopulated())
         {
            log.traceln("Skipping API " + api.getName() + " (already populated)");
            return;
         }
         
         api.setPopulated(true);  // if add fails, transaction will rollback
         String sql = "update API set populated=1 where name=?";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, api.getName());
         pstmt.executeUpdate();
         pstmt.close();
         
         storePackagesAndClasses(api);
         conn.commit();
      }
      catch (SQLException ex)
      {
         log.error("Store (api: "+api.getName()+") failed!");
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
   
   private void storePackagesAndClasses(API api) throws SQLException
   {
      delIndices();
      
      PackageDoc[] packages = root.specifiedPackages();
      log.traceln(packages.length + " packages to process..");
      for (int i=0; i<packages.length; i++)
      {
         log.traceln("Processing package " + packages[i].name() + "..");
         new JPackage(packages[i], true, api).store(conn);
         log.traceln("Package: " + packages[i].name() + " stored", Logger.VERBOSE);
      }

   }
   
   private void addIndices()
   {
      try
      {
         proc.doAction(conn, "add_idx"); // speeds up setting refs
      }
      catch (SQLException ex)
      {
         log.verbose("no add/remove index optimizations during population for current db");
      }
   }
   private void delIndices()
   {
      try
      {
         proc.doAction(conn, "del_idx"); // speeds up add
      }
      catch (SQLException ex)
      {
         log.verbose("no add/remove index optimizations during population for current db");
      }
   }
   
   private void updateInternalRefs()
   {
      log.traceln("Updating Internal References..");
      addIndices();
      setInternalReferences();
      
      try
      {
         new AncestorPopulator();  // populates class ancestor table
      }
      catch (SQLException ex)
      {
         log.error("Failed to populate class ancestors table");
         DBUtils.logSQLException(ex);
      }
   }

   
   public void doRemove(String apiname)
   {
      try
      {
         API api = API.makeAPIFor(conn, apiname);
         if (api == null)
         {
            log.error("No api named "+apiname+" found in db");
            return;
         }
         
         api.delete(conn);
         conn.commit();
      }
      catch (Exception ex)
      {
         log.error("Exception: "+ex.getMessage());
      }
      log.traceln("remove done");
   }

   private void callStoredProc(String action) throws SQLException
   {
      String sql = "{call db_proc(?)}";
      CallableStatement cstmt = conn.prepareCall(sql);
      cstmt.setString(1, action);
      cstmt.executeUpdate();
      cstmt.close();
      conn.commit();
   }

   /** required for Doclet inheritance */
   public static int optionLength(String option)
   {
      if(option.equals("-norefs") || option.equals("-refsonly"))
         return 1;
      if (option.equals("-debug") || option.equals("-verbose"))
         return 1;
      if (option.equals("-api"))
         return 2;
      return 0;
   }
   
   public void setInternalReferences()
   {
      String[][] params = {{"FIELD", "typename", "typeid", "id"},
                           {"METHOD", "returntypename", "returntypeid", "id"},
                           {"IMPL_INTERFACE", "name", "interfaceid", "classid"},
                           {"THROWNEXCEPTION", "name", "exceptionid", "throwerid"},
                           {"PARAMETER", "typename", "typeid", "execmemberid"},
                           {"SUPERCLASS", "name", "superclassid", "classid"}
                          };

      String sql = "";
      String sql2 = "";

      for (int i=0; i<params.length; i++)
      {
         log.traceln("\tProcessing " + params[i][0] + " references..");

         try
         {
            sql = " select " + params[i][0] + "." + params[i][3] + ", c.id, c.qualifiedname " + 
                  " from " + params[i][0] + ", CLASSTYPE c " +
                  " where " + params[i][0] + "." + params[i][1] + " = c.qualifiedname and " +
                              params[i][0] + "." + params[i][2] + " is null";
            
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            String supplementary = "";
            if (i>=2)
               supplementary = " and " + params[i][1] + "=? ";
            
            sql2 = "update " + params[i][0] + " set " + params[i][2] + "=? " + 
                   " where " + params[i][3] + "=?" + supplementary;
            
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            
            while (rset.next())
            {
               pstmt.clearParameters();
               pstmt.setInt(1, rset.getInt(2));
               pstmt.setInt(2, rset.getInt(1));
               if (!supplementary.equals(""))
                  pstmt.setString(3, rset.getString(3));
               pstmt.executeUpdate();
            }
            
            pstmt.close();
            rset.close();
            
            stmt.close();

            conn.commit();
            log.traceln("Updated (committed) " + params[i][0] + " references", Logger.VERBOSE);
         
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
         
      }  // end for loop
         

      try
      {
         String[][] params2 = {{"PACKAGE", "name"},
                               {"CLASSTYPE", "qualifiedname"},
                               {"MEMBER", "qualifiedname"},
                               {"EXECMEMBER", "fullyqualifiedname"}};

         for (int i=0; i<params2.length; i++)
         {
            log.traceln("\tProcessing seetag " + params2[i][0] + " references..");

            sql = "select r.sourcedoc_id, " + params2[i][0] + ".id, " + 
                     params2[i][0] + "." + params2[i][1] + 
                     " from REFERENCE r, " + params2[i][0] + 
                     " where r.refdoc_name = " + params2[i][0] + "." + params2[i][1] + 
                     " and r.refdoc_id is null";

            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            sql2 = "update REFERENCE set refdoc_id=? where sourcedoc_id=? and refdoc_name=?";

            PreparedStatement pstmt = conn.prepareStatement(sql2);

            while (rset.next())
            {
               pstmt.clearParameters();
               pstmt.setInt(1, rset.getInt(2));
               pstmt.setInt(2, rset.getInt(1));
               pstmt.setString(3, rset.getString(3));
               pstmt.executeUpdate();
            }

            pstmt.close();
            rset.close();
            stmt.close();
            
            conn.commit();
         }
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
   
   public void reset() throws SQLException
   {
      proc.doAction(conn, "reset");
   }
   
   public List listAPINames() throws SQLException
   {
      String sql = "select name from API where populated = 1";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rset = pstmt.executeQuery();
      List names = new ArrayList();
      String name;
      while (rset.next())
      {
         name = rset.getString(1);
         names.add(name);
      }
      return names;
   }

   private void addSequences()
   {
      try
      {
         String seqs[] = {"PKG_SEQ", "CLASSTYPE_SEQ", "AUTHOR_SEQ", "DOC_SEQ", "MEMBER_SEQ"};
         for (int i=0; i<seqs.length; i++)
         {
            pkmgr.addSequence(conn, seqs[i], 100);
         }
         conn.commit();
         log.traceln("added (committed) ashkelon sequences to db");
      }
      catch (SQLException ex)
      {
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
