/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import org.ashkelon.manager.Ashkelon;

import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Eitan Suez
 */
public class UpdateCmd extends AddCmd
{
   public UpdateCmd() {}
   
   public String getName() { return "update"; }
   public String getDescription()
   {
      return "Remove specified api name, update the source code and " +
                "add the api back in.";
   }
   public String getExample()
   {
      return "ashkelon update JiBX";
   }
   public String getNote()
   {
      return "remember to pass in the api name, not the api.xml filename.";
   }
   
   public void registerParameters() throws JSAPException
   {
      super.registerParameters();
   }
   
   public void invoke(JSAPResult arguments)
   {
      String apiname = arguments.getString("api");
      
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      ashkelon.doRemove(apiname, false);
      ashkelon.finish();
      
      // add:
      super.invoke(arguments);
   }
}
