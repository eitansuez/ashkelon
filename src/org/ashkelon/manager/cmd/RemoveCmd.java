/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import org.ashkelon.manager.Ashkelon;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Option;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * @author Eitan Suez
 */
public class RemoveCmd extends BaseCmd
{
   public RemoveCmd() {}
   
   public String getName() { return "remove"; }
   public String getDescription()
   {
      return "Remove specified api from the repository";
   }
   public String getExample()
   {
      return "ashkelon remove \"Apache Ant\"";
   }
   public String getNote()
   {
      return "keeping the api record itself in the db after a remove " +
       "can be useful when you later want to repopulate the api (all " +
       "the api information necessary for the add operation was saved)";
   }

   public void registerParameters() throws JSAPException
   {
      super.registerParameters();

      Option apiOption = new UnflaggedOption("apiname").setRequired(true);
      apiOption.setHelp("Name of API to remove");
      registerParameter(apiOption);

      Switch completeFlag = new Switch("complete").setShortFlag('c')
                                                  .setLongFlag("complete");
      
      completeFlag.setHelp("Remove API completely from database (including API record)");
      registerParameter(completeFlag);
   }
   
   public void invoke(JSAPResult arguments)
   {
      super.invoke(arguments);

      boolean complete = arguments.getBoolean("complete");
      String apiname = arguments.getString("apiname");
      
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      ashkelon.doRemove(apiname, complete);
      ashkelon.finish();
   }

}
