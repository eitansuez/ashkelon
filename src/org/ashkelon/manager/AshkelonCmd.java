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
import org.ashkelon.util.StringUtils;

/**
 * @author Eitan Suez
 */
public class AshkelonCmd
{
   public static void addapiCmd(String[] args)
   {
      Logger log = Logger.getInstance();
      String apifilename = args[args.length-1];
      log.debug("api file name: "+apifilename);
      try
      {
         String sourcepath = getSourcePathOption(args);
         API api = new API().unmarshal(apifilename, sourcepath);
         log.debug("api unmarshalled; name is: "+api.getName());
         
         if (exists(api))
         {
            log.traceln("API " + api.getName() + " is already in repository" +
                  (" (skipping);  to update, remove first."));
            return;
         }
         
         String basepath = Config.getInstance().getSourcePathBase();
         File base = new File(basepath);
         if (!base.exists())
         {
            log.traceln("Creating directory "+basepath);
            base.mkdir();
         }
         api.fetch(base);
         
         LinkedList argslist = new LinkedList(Arrays.asList(args));
         argslist.removeFirst();  // the ashkelon "add" cmd
         argslist.removeLast();  // @apiname.xml
         
         int sourcepathIndex = getSourcePathIndex(args);
         if (sourcepathIndex < 0)
         {
            argslist.addLast("-sourcepath");
            argslist.add(api.getSourcePath());
         }
         else
         {
            argslist.remove(sourcepathIndex+1);
            argslist.add(sourcepathIndex+1, api.getSourcePath() + ":" + sourcepath);
         }
         
         argslist.add("-api");
         argslist.add(apifilename);
         
         Collection packagenames = api.getPackagenames();
         argslist.addAll(packagenames);
         log.debug(StringUtils.join(argslist.toArray(), " "));
         
//         System.out.println(argslist);
         
         String[] addlist = new String[argslist.size()];
         String[] javadocargs = (String[]) argslist.toArray(addlist);
         
         // parms are: programName, docletClassName, javadocargs
         com.sun.tools.javadoc.Main.execute("ashkelon", "org.ashkelon.manager.Ashkelon", 
               javadocargs);
         
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
   
   private static boolean exists(API api)
   {
      Connection conn = null;
      try
      {
         conn = DBMgr.getInstance().getConnection();
         return (api.exists(conn));
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
         ashkelon.reset();
      }
      catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
      }
      ashkelon.finish();
      log.traceln("..done");
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
         List names = ashkelon.listAPINames();
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
      }
      else if (args[0].equals("reset"))
      {
         resetCmd();
      }
      else if (args[0].equals("list"))
      {
         listCmd();
      }
      else if (args[0].equals("test"))
      {
         testCmd();
      }
      else if (args[0].equals("remove"))
      {
         if (args.length == 1)
         {
            printUsage();
            return;
         }
         removeCmd(args);
      }
      else if (args[0].equals("add"))
      {
         if (args.length == 1)
         {
            printUsage();
            return;
         }
         
         String lastarg = args[args.length-1];
         if (lastarg == null ||
               ! lastarg.endsWith(".xml") )
         {
            printUsage();
            return;
         }
         addapiCmd(args);
         
      }
      else if (args[0].equals("update"))
      {
         if (args.length == 1)
         {
            printUsage();
            return;
         }
         removeCmd(args);
         
         String lastarg = args[args.length-1];
         if (lastarg == null ||
               ! lastarg.endsWith(".xml") )
         {
            printUsage();
            return;
         }
         addapiCmd(args);
         
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
