package org.ashkelon;

import com.sun.javadoc.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import org.exolab.castor.xml.*;

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
      Ashkelon ashkelon = new Ashkelon(root);
      
      boolean verbose = StringUtils.getCommandLineOption("-verbose", root.options());
      boolean debug = StringUtils.getCommandLineOption("-debug", root.options());
      Logger log = Logger.getInstance();
      
      if (verbose)
   	   log.setTraceLevel(Logger.VERBOSE);
      if (debug)
         log.setTraceLevel(Logger.DEBUG);
      
      if (!ashkelon.init())
         return false;
      
      ashkelon.doAdd();

      return ashkelon.finish();
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
   
   private boolean init()
   {
      dbmgr = DBMgr.getInstance();
      conn = null;
      try
      {
         conn = dbmgr.getConnection();
         conn.setAutoCommit(false);
      } catch (SQLException ex)
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
   
   private boolean finish()
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

      String apifilename = StringUtils.getStringCommandLineOption("-api", root.options());

      API api = null;
      if (!StringUtils.isBlank(apifilename))
      {
         try
         {
            api = new API().load(new FileReader(apifilename));
            log.debug("api unmarshalled; name is: "+api.getName());
         }
         catch (Exception ex)
         {
            log.error("Exception: "+ex.getMessage());
         }
      }
      
		if (api != null)
		{
		  try
		 {
			 log.traceln("Storing api");
			 api.store(conn);
			 conn.commit();
		 }
		 catch (SQLException ex)
		 {
			  DBUtils.logSQLException(ex);
		 }
		}
      
      if(!refsonly)
      {
         try
         {
            //callStoredProc("del_idx"); // speeds up add
            proc.doAction(conn, "del_idx");
         } catch (SQLException ex)
         {
            log.verbose("no add/remove index optimizations during population for current db");
         }
         
         PackageDoc[] packages = root.specifiedPackages();
         for (int i=0; i<packages.length; i++)
         {
            try
            {
               log.traceln("Processing package " + packages[i].name() + "..");
               new JPackage(packages[i], true, api).store(conn);
               conn.commit();
               log.traceln("Package: " + packages[i].name() + " stored (committed)", Logger.VERBOSE);
            } catch (SQLException ex)
            {
               log.error("Store (package: "+packages[i].name()+") failed!");
               DBUtils.logSQLException(ex);
               log.error("Rolling back..");
               try
               {
                  conn.rollback();
               } catch (SQLException inner_ex)
               {
                  log.error("rollback failed!");
               }
            }
         }

         ClassDoc[] classes = root.specifiedClasses();
         for (int i=0; i<classes.length; i++)
         {
            try
            {
               new ClassType(classes[i], null, api).store(conn);
               conn.commit();
               log.traceln("Class: "+classes[i].qualifiedName()+" stored (committed)", Logger.VERBOSE);
            } catch (SQLException ex)
            {
               log.error("Store (class: "+classes[i].qualifiedName()+") failed!");
               DBUtils.logSQLException(ex);
               log.error("Rolling back..");
               try
               {
                  conn.rollback();
               } catch (SQLException inner_ex)
               {
                  log.error("rollback failed!");
               }
            }
         }
      }

      long addtime = new java.util.Date().getTime() - start;
      log.traceln("Add Time: "+addtime/1000+" seconds");
      
      if (!norefs)
      {
         log.traceln("Updating Internal References..");
         try
         {
            //callStoredProc("add_idx"); // speeds up setting refs
            proc.doAction(conn, "add_idx");
         } catch (SQLException ex)
         {
            log.verbose("no add/remove index optimizations during population for current db");
         }
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
   
   
   
   private void doRemove(String[] elements)
   {
      if (elements.length == 1 && elements[0].startsWith("@") && elements[0].endsWith(".xml"))
      {
         try
         {
            API api = new API().load(new FileReader(elements[0].substring(1)));
            log.debug("api unmarshalled; name is: "+api.getName());
            api.delete(conn);
            conn.commit();
         }
         catch (Exception ex)
         {
            log.error("Exception: "+ex.getMessage());
         }
         log.traceln("remove done");
         return;
      }
      
      if (elements.length == 1 && elements[0].startsWith("@"))
      {
         List elemList = JDocUtil.getPackageListFromFileName(elements[0]);
         elements = (String[]) elemList.toArray(elements);
      }
      for (int i=0; i<elements.length; i++)
      {
         log.traceln("Processing " + elements[i] + " for deletion..");
         try
         {
            boolean found = JPackage.delete(conn, elements[i]);  // package does its own commit
            if (found)
            {
               conn.commit();
               log.traceln("Package: " + elements[i] + " stored (committed)", Logger.VERBOSE);
            }
            else
            {
               found = ClassType.delete(conn, elements[i]);
               if (found)
               {
                  conn.commit();
                  log.traceln("Class: " + elements[i] + " stored (committed)", Logger.VERBOSE);
               }
               else  // neither package nor class found
               {
                  log.traceln("Element " + elements[i] + " not found in repository");
               }
            }
         }
         catch (SQLException ex)
         {
            log.error("Failed to remove element " + elements[i]);
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try
            {
               conn.rollback();
            } catch (SQLException inner_ex)
            {
               log.error("rollback failed!");
            }
         }
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
   
   private void setInternalReferences()
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
         
         } catch (SQLException ex)
         {
            log.error("Internal Reference Update Failed!");
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try
            {
               conn.rollback();
            } catch (SQLException inner_ex)
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
         }
      } catch (SQLException ex)
      {
         log.error("Internal Reference Update Failed!");
         DBUtils.logSQLException(ex);
         log.error("Rolling back..");
         try
         {
            conn.rollback();
         } catch (SQLException inner_ex)
         {
            log.error("rollback failed!");
         }
      }
   }
   
   
   private static void addCmd(String[] args)
   {
      String[] javadocargs = new String[args.length + 1];
      javadocargs[0] = "-doclet";
      javadocargs[1] = "org.ashkelon.Ashkelon";
      for (int i=1; i<args.length; i++)
      {
         javadocargs[i+1] = args[i];
      }
      com.sun.tools.javadoc.Main.main(javadocargs);
   }
   
   public static void addapiCmd(String[] args)
   {
      Logger log = Logger.getInstance();
      String apifilename = args[args.length-1].substring(1); // remove leading @ char
      log.debug("api file name: "+apifilename);
      try
      {
         API api = new API().load(new FileReader(apifilename));
         log.debug("api unmarshalled; name is: "+api.getName());
         LinkedList argslist = new LinkedList(Arrays.asList(args));
         argslist.removeLast();
         
         argslist.add("-api");
         argslist.add(apifilename);
         
         Collection packagenames = api.getPackagenames();
         argslist.addAll(packagenames);
         log.debug(StringUtils.join(argslist.toArray(), " "));
         String[] addlist = new String[argslist.size()];
         addCmd((String[]) argslist.toArray(addlist));

         /* does not work because calling javadoc never returns 
          * (must contain a system.exit in it)
         Ashkelon ashkelon = new Ashkelon();
         ashkelon.init();
         api.store(ashkelon.conn);
         ashkelon.finish();
         log.traceln("..done");
          *
          * this is why above i must pass -api [apifilename] parameters to the doclet!
          */
      }
      catch (FileNotFoundException ex)
      {
         log.error("File "+apifilename+" not found.  Aborting");
      }
      catch (MarshalException ex)
      {
         log.error("MarshalException: "+ex.getMessage());
         ex.printStackTrace(log.getWriter());
      }
      catch (ValidationException ex)
      {
         log.error("ValidationException: "+ex.getMessage());
         ex.printStackTrace(log.getWriter());
      }
   }
   
   
   private static void testCmd()
   {
      String[] javadocargs = new String[5];
      javadocargs[0] = "-doclet";
      javadocargs[1] = "org.ashkelon.Ashkelon";
      javadocargs[2] = "-sourcepath";
      javadocargs[3] = "c:\\jdk1.3\\src";
      //javadocargs[4] = "-remove";
      javadocargs[4] = "java.util";
      com.sun.tools.javadoc.Main.main(javadocargs);
   }
   
   public static void resetCmd()
   {
      Logger log = Logger.getInstance();
      log.traceln("Please wait while all tables are reset..");
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      try
      {
         /*
         log.traceln("Calling stored procedure..");
         ashkelon.callStoredProc("reset");
         log.traceln("Stored procedure returned..");
          */
         //ashkelon.callStoredProc("reset_seqs");
         ashkelon.proc.doAction(ashkelon.conn, "reset");
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      /*
      log.traceln("Attempting to insert sequences..");
      ashkelon.addSequences();
       */
      //ashkelon.finish();
      ashkelon.dbmgr.releaseConnection(ashkelon.conn);
      log.traceln("..done");
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
      } catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
         log.error("Rolling back..");
         try
         {
            conn.rollback();
         } catch (SQLException inner_ex)
         {
            log.error("rollback failed!");
         }
      }
   }
   
   
   private static void printUsage()
   {
      Logger log = Logger.getInstance();
      try
      {
         InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("org/ashkelon/Usage.txt");
         if (is == null) { return; }
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         String line = "";
         while ((line = br.readLine()) != null)
            log.traceln(line);
         br.close();
         is.close();
      } catch (IOException ex)
      {
         log.error("Unable to print usage!");
         log.error("IOException: "+ex.getMessage());
      }
   }
   
	public static void updateRefsCmd()
	{
	  Logger log = Logger.getInstance();
	  Ashkelon ashkelon = new Ashkelon();
	  ashkelon.init();
	  ashkelon.setInternalReferences();
	  try
	  {
		  new AncestorPopulator(); // populates class ancestor table
	  }
	  catch (SQLException ex)
	  {
		  log.error("Failed to populate class ancestors table");
		  DBUtils.logSQLException(ex);
	  }
	}

   public static void listCmd()
   {
      Logger log = Logger.getInstance();
      log.setPrefix("list");
      Ashkelon ashkelon = new Ashkelon();
      
      if (!ashkelon.init())
      {
         log.error("error occurred. exiting");
         return;
      }
      
      try
      {
         List names = Generic.listNames(ashkelon.conn, API.getTableName());
         Iterator i = names.iterator();
         while (i.hasNext())
            log.traceln((String) i.next());
         if (names.isEmpty())
            log.traceln("repository is empty");
      } catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      ashkelon.finish();
   }
   
   public static void removeCmd(String args[])
   {
      Logger log = Logger.getInstance();
      log.setPrefix("remove");
      
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      String[] removeargs = new String[args.length - 1];
      for (int i=1; i<args.length; i++)
      {
         removeargs[i-1] = args[i];
      }
      ashkelon.doRemove(removeargs);
      ashkelon.finish();
   }

   
   // entry point into ashkelon
   public static void main(String[] args)
   {
      //String[] javadocargs;
      
      Logger log = Logger.getInstance();

      if (args.length >=2)
      {
         if (args[1].equals("-verbose"))
         {
            log.setTraceLevel(Logger.VERBOSE);
         }
         else if (args[1].equals("-debug"))
         {
            log.setTraceLevel(Logger.DEBUG);
         }
      }
      
      if (args.length == 0)
      {
         printUsage();
         return;
      }
      else if (args[0].equals("reset"))
      {
         resetCmd();
         return;
      }
      else if (args[0].equals("list"))
      {
         listCmd();
         return;
      }
      else if (args[0].equals("test"))
      {
         testCmd();
         return;
      }
      else if (args[0].equals("remove"))
      {
         if (args.length == 1)
            printUsage();
         removeCmd(args);
         return;
      }
      else if (args[0].equals("add"))
      {
         if (args.length == 1)
            printUsage();
         String lastarg = args[args.length-1];
         if (lastarg != null && lastarg.startsWith("@") && lastarg.endsWith(".xml"))
            addapiCmd(args);
         else
            addCmd(args);
         return;
      }
      else if (args[0].equals("updaterefs"))
      {
         updateRefsCmd();
      }
      else
      {
         printUsage();
      }
   }
   
}
