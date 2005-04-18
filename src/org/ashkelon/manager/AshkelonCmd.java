package org.ashkelon.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.ashkelon.API;
import org.ashkelon.db.DBMgr;
import org.ashkelon.db.DBUtils;
import org.ashkelon.util.Logger;
import org.jibx.runtime.JiBXException;

/**
 * @author Eitan Suez
 */
public class AshkelonCmd
{
   public static void addApiNameCmd(String[] args)
   {
      Logger log = Logger.getInstance();
      String apiname = args[args.length-1];
      log.debug("api name: "+apiname);
      
      API api = loadAPIByName(apiname);
      
      if (api == null)
      {
         log.error("Cannot find API " + apiname + " in database");
         return;
      }
      
      fetchSource(api);  // ..from source repository
      
      LinkedList argslist = new LinkedList(Arrays.asList(args));
      argslist.removeFirst();  // the ashkelon "add" cmd
      argslist.removeLast();  // api name
      
      // adjust source path
      int sourcepathIndex = getSourcePathIndex(args);
      if (sourcepathIndex < 0)
      {
         argslist.addLast("-sourcepath");
         argslist.add(api.getSourcePath());
      }
      else
      {
         argslist.remove(sourcepathIndex);
         String sourcepath = getSourcePathOption(args);
         argslist.add(sourcepathIndex, api.getSourcePath() + ":" + sourcepath);
      }
      
      // doclet argument:  which api (id) to populate
      argslist.add("-api");
      argslist.add(""+api.getId());
      
      // doclet arguments: api packages to process/parse
      Collection packagenames = api.getPackagenames();
      argslist.addAll(packagenames);
      log.debug("argslist before calling javadoc: "+argslist);
      
      
      // invoke javadoc..
      String[] addlist = new String[argslist.size()];
      String[] javadocargs = (String[]) argslist.toArray(addlist);
      
      // parms are: programName, docletClassName, javadocargs
      com.sun.tools.javadoc.Main.execute("ashkelon", "org.ashkelon.manager.Ashkelon", 
            javadocargs);
   }
   
   private static void fetchSource(API api)
   {
      String basepath = Config.getInstance().getSourcePathBase();
      File base = new File(basepath);
      if (!base.exists())
      {
         Logger.getInstance().traceln("Creating directory "+basepath);
         base.mkdir();
      }
      api.fetch(base);  // fetch api source code from source repository
   }
   
   
   private static API loadAPIByName(String apiName)
   {
      Connection conn = null;
      try
      {
         conn = DBMgr.getInstance().getConnection();
         API api = API.makeAPIFor(conn, apiName);
         return api;
      }
      catch (SQLException ex)
      {
         Logger.getInstance().error("Failed to load api: "+apiName);
         DBUtils.logSQLException(ex);
      }
      finally
      {
         if (conn != null)
            DBMgr.getInstance().releaseConnection(conn);
      }
      return null;
   }
   
   
   public static void addApiXmlCmd(String[] args)
   {
      Logger log = Logger.getInstance();
      String apifilename = args[args.length-1];
      log.debug("api file name: "+apifilename);
      try
      {
         String sourcepath = getSourcePathOption(args);
         API api = API.unmarshal(apifilename, sourcepath);
         log.debug("api unmarshalled; name is: "+api.getName());
         
         if (existsPopulated(api))
         {
            log.traceln("API " + api.getName() + " is already in repository" +
                  ("and populated (skipping);  to update, remove first."));
            return;
         }
         
         Connection conn = null;
         try
         {
            conn = DBMgr.getInstance().getConnection();
            api.store(conn);
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
            return;
         }
         finally
         {
            if (conn != null)
               DBMgr.getInstance().releaseConnection(conn);
         }
         
         LinkedList argslist = new LinkedList(Arrays.asList(args));
         argslist.removeLast();
         argslist.addLast(api.getName());
         
         log.debug("argslist is: "+argslist);
         
         String[] addlist = new String[argslist.size()];
         args = (String[]) argslist.toArray(addlist);
         
         addApiNameCmd(args);
         
      }
      catch (FileNotFoundException ex)
      {
         log.error("File "+apifilename+" not found.  Aborting");
      }
      catch (java.text.ParseException ex)
      {
         log.error("Exception: "+ex.getMessage());
         ex.printStackTrace(log.getWriter());
      }
   }
   
   private static boolean existsPopulated(API api)
   {
      Connection conn = null;
      try
      {
         conn = DBMgr.getInstance().getConnection();
         return (api.existsPopulated(conn));
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      finally
      {
         if (conn != null)
            DBMgr.getInstance().releaseConnection(conn);
      }
      return false;
   }
   
   private static String getSourcePathOption(String[] args)
   {
      return getOption("-sourcepath", args);
   }
   private static String getOption(String optionFlag, String[] args)
   {
      for (int i=0; i<args.length; i++)
      {
         if (optionFlag.equals(args[i]))
         {
            return args[i+1];
         }
      }
      return ".";
   }
   
   private static int getSourcePathIndex(String[] args)
   {
      return getIndex("-sourcepath", args);
   }
   private static int getIndex(String optionFlag, String[] args)
   {
      for (int i=0; i<args.length; i++)
      {
         if (optionFlag.equals(args[i]))
         {
            return i;
         }
      }
      return -1;
   }
   
   public static void resetCmd()
   {
      Logger log = Logger.getInstance();
      log.traceln("Please wait while all tables are reset..");
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      try
      {
         ashkelon.reset();
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      ashkelon.finish();
      log.traceln("..done");
   }
   
	public static void updateRefsCmd()
   {
      Logger log = Logger.getInstance();
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      ashkelon.updateInternalRefs();
   }

   public static void listCmd()
   {
      listCmd(false);
   }
   
   public static void listCmd(boolean pending)
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
         List names = ashkelon.listAPINames(pending);
         log.traceln(names.size()+" APIs in ashkelon:");
         Iterator i = names.iterator();
         while (i.hasNext())
         {
            log.traceln("   " + (String) i.next());
            // TODO: Put the version of this package into the list
            // TODO: List the packages and classes for this api
            if (names.isEmpty())
            {
               log.traceln("repository is empty");
            }
         }
         if (names.isEmpty())
         {
            log.traceln("repository is empty");
         }
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      ashkelon.finish();
   }
   
   public static void removeCmd(String apiname)
   {
      removeCmd(apiname, false);  // default
   }
   public static void removeCmd(String apiname, boolean includingApiRecord)
   {
      Logger log = Logger.getInstance();
      log.setPrefix("remove");
      
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      ashkelon.doRemove(apiname, includingApiRecord);
      ashkelon.finish();
   }

   
   public static void main(String[] args)
   {
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
      
      
      if ("reset".equals(args[0]))
      {
         resetCmd();
      }
      else if ("list".equals(args[0]))
      {
         listCmd();
      }
      else if ("listpending".equals(args[0]))
      {
         listCmd(true);
      }
      else if ("remove".equals(args[0]))
      {
         removeApiCmd(args);
      }
      else if ("add".equals(args[0]))
      {
         addApiCmd(args);
      }
      else if ("update".equals(args[0]))
      {
         removeApiCmd(args);
         addApiCmd(args);
      }
      else if ("updaterefs".equals(args[0]))
      {
         updateRefsCmd();
      }
      else if ("exportapis".equals(args[0]))
      {
         try
         {
            Ashkelon ashkelon = new Ashkelon();
            ashkelon.init();
            ashkelon.dumpAPISet();
            ashkelon.finish();
         }
         catch (JiBXException ex)
         {
            System.err.println("JiBXException: "+ex.getMessage());
            ex.printStackTrace();
         }
         catch (SQLException ex)
         {
            System.err.println("SQLException: "+ex.getMessage());
            ex.printStackTrace();
         }
      }
      else
      {
         printUsage();
      }
   }
   
   private static void addApiCmd(String[] args)
   {
      if (args.length == 1)
      {
         printUsage();
         return;
      }
      
      String lastarg = args[args.length-1];
      if (lastarg.endsWith(".xml"))
      {
         addApiXmlCmd(args);
      }
      else
      {
         addApiNameCmd(args);
      }
   }
   private static void removeApiCmd(String[] args)
   {
      String lastarg = args[args.length - 1];
      String apiname = lastarg;
      if ("-f".equals(args[args.length - 2])) // hard remove -f : "full" remove
      {
         removeCmd(apiname, true);
      }
      else
      {
         removeCmd(apiname, false);
      }
   }
   

   private static void printUsage()
   {
      Logger log = Logger.getInstance();
      try
      {
         ClassLoader loader = AshkelonCmd.class.getClassLoader();
         InputStream is = loader.getResourceAsStream("org/ashkelon/manager/Usage.txt");
         if (is == null) { return; }
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         String line = "";
         while ((line = br.readLine()) != null)
            log.traceln(line);
         br.close();
         is.close();
      }
      catch (IOException ex)
      {
         log.error("Unable to print usage!");
         log.error("IOException: "+ex.getMessage());
      }
   }
   
}
