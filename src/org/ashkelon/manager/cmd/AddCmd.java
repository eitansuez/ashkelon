/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Option;
import com.martiansoftware.jsap.UnflaggedOption;
import com.martiansoftware.jsap.JSAPResult;
import org.ashkelon.API;
import org.ashkelon.db.DBMgr;
import org.ashkelon.db.DBUtils;
import java.util.LinkedList;
import java.util.Collection;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Eitan Suez
 */
public class AddCmd extends ProcessAPICmd
{
   public AddCmd() {}

   public String getName() { return "add"; }
   public String getDescription()
   {
      return "Add an API to the ashkelon source repository.";
   }
   public String getExample()
   {
      return "ashkelon add --source 1.4 apis/j2se14.xml";
   }
   public String getNote()
   {
      return "after an API is added, any references between it and apis existing " +
       "in the database will be linked";

      //"ashkelon will pass any valid javadoc option to the javadoc "+ 
      //"parsing engine.  For a list of javadoc options, simply type: javadoc "+
      //"at the command line.";
      // ..no longer true because now i'm forced to explicitly specify
      // all options that command accepts
   }

   public void registerParameters()
         throws JSAPException
   {
      super.registerParameters();

      Option apiOption = new UnflaggedOption("api").setRequired(true);

      apiOption.setHelp("API to populate.  Can be specified in one of three "+
            "ways:  [1] referenced as an api.xml file, [2] specify the name of " +
            "the api (assuming the API record is already in the database; or " +
            "[3] a maven project.xml file (does not work with all maven projects)");
      registerParameter(apiOption);
   }

   public String docletClassName() { return "org.ashkelon.manager.Ashkelon"; }


   public void invoke(JSAPResult arguments)
   {
      super.invoke(arguments);

      String apispec = arguments.getString("api");
      if (apispec.endsWith(".xml"))
         addApiXmlCmd(apispec, arguments);
      else
         addApiNameCmd(docletClassName(), apispec, arguments);
   }

   public void addApiNameCmd(String docletClassName,
                             String apiname,
                             JSAPResult arguments)
   {
      log.debug("api name: "+apiname);

      API api = loadAPIByName(apiname);

      if (api == null)
      {
         log.error("Cannot find API " + apiname + " in database");
         return;
      }

      fetchSource(api);  // ..from source repository

      String[] javadocargs = constructJavadocArgslist(arguments, api);

      // parms are: programName, docletClassName, javadocargs
      com.sun.tools.javadoc.Main.execute("ashkelon",
                                         docletClassName,
                                         javadocargs);
   }

   private String[] constructJavadocArgslist(JSAPResult arguments, API api)
   {
      LinkedList javadocargslist = new LinkedList();

      javadocargslist.addLast("-sourcepath");
      String sourcepath = arguments.getString("sourcepath");
      if (sourcepath != null)
      {
         javadocargslist.addLast(api.sourcepath() + ":" + sourcepath);
      }
      else
      {
         javadocargslist.addLast(api.sourcepath());
      }

      String classpath = arguments.getString("classpath");
      if (classpath != null)
      {
         javadocargslist.addLast("-classpath");
         javadocargslist.addLast(classpath);
      }

      String source = arguments.getString("source");
      if (source != null)
      {
         javadocargslist.addLast("-source");
         javadocargslist.addLast(source);
      }
      String encoding = arguments.getString("encoding");
      if (encoding != null)
      {
         javadocargslist.addLast("-encoding");
         javadocargslist.addLast(encoding);
      }

      // doclet argument:  which api (id) to populate
      javadocargslist.addLast("-api");
      javadocargslist.addLast(""+api.getId());

      // doclet arguments: api packages to process/parse
      Collection packagenames = api.getPackagenames();
      javadocargslist.addAll(packagenames);
      log.debug("argslist before calling javadoc: "+javadocargslist);


      // invoke javadoc..
      String[] addlist = new String[javadocargslist.size()];
      return (String[]) javadocargslist.toArray(addlist);
   }

   private API loadAPIByName(String apiName)
   {
      Connection conn = null;
      try
      {
         conn = DBMgr.getInstance().getConnection();
         return API.makeAPIFor(conn, apiName);
      }
      catch (SQLException ex)
      {
         log.error("Failed to load api: "+apiName);
         DBUtils.logSQLException(ex);
      }
      finally
      {
         if (conn != null)
            DBMgr.getInstance().releaseConnection(conn);
      }
      return null;
   }


   public void addApiXmlCmd(String apifilename, JSAPResult arguments)
   {
      log.debug("api file name: "+apifilename);

      try
      {
         String sourcepath = arguments.getString("sourcepath");
         API api = API.unmarshal(apifilename, sourcepath);
         log.debug("api unmarshalled; name is: "+api.getName());

         if (existsPopulated(api))
         {
            log.traceln("API " + api.getName() + " is already in repository" +
                  (" and populated (skipping);  to update, remove first."));
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

         addApiNameCmd(docletClassName(), api.getName(), arguments);
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

   private boolean existsPopulated(API api)
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


}
   
