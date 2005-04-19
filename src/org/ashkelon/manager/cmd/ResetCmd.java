/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import java.sql.SQLException;
import org.ashkelon.db.DBUtils;
import org.ashkelon.manager.Ashkelon;
import org.ashkelon.util.Logger;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Eitan Suez
 */
public class ResetCmd extends BaseCmd
{
   public ResetCmd() {}
   
   public String getName() { return "reset"; }
   public String getDescription()
   {
      return "Reset repository (i.e. delete everything, use with care)";
   }
   public String getExample()
   {
      return "ashkelon reset";
   }
   public String getNote()
   {
      return "not too big a deal if you reset a db, it can all be " +
            "reconstituted from the source with a series of 'add' cmds";
   }
   
   public void registerParameters() throws JSAPException
   {
      super.registerParameters();
   }

   public void invoke(JSAPResult arguments)
   {
      super.invoke(arguments);

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
   
}
