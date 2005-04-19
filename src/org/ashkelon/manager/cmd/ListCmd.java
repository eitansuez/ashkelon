/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.ashkelon.db.DBUtils;
import org.ashkelon.manager.Ashkelon;
import org.ashkelon.util.Logger;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * @author Eitan Suez
 */
public class ListCmd extends BaseCmd
{
   public ListCmd() {}
   
   public String getName() { return "list"; }
   public String getDescription()
   {
      return "List apis currently residing in the repository";
   }
   
   public void registerParameters() throws JSAPException
   {
      super.registerParameters();
      
      Switch pending = new Switch("pending").setShortFlag('p')
                        .setLongFlag("pending");
      pending.setHelp("Controls whether to list pending APIs or " +
            " ones that are already populated in the system (default)");
      
      registerParameter(pending);
   }
   
   public void invoke(JSAPResult arguments)
   {
      super.invoke(arguments);
      
      Logger log = Logger.getInstance();
      Ashkelon ashkelon = new Ashkelon();
      
      if (!ashkelon.init())
      {
         log.error("error occurred. exiting");
         return;
      }
      
      try
      {
         boolean pending = arguments.getBoolean("pending");
         
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
   
}
