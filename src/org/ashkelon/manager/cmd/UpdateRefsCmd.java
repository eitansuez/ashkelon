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
public class UpdateRefsCmd extends BaseCmd
{
   public UpdateRefsCmd() {}

   public String getName() { return "update refs"; }
   public String getDescription()
   {
      return "Update inter-api references.  You don't need to run this " +
             "but it's there if you like to tinker";
   }
   
   public void registerParameters() throws JSAPException
   {
      super.registerParameters();
   }
   
   public void invoke(JSAPResult result)
   {
      super.invoke(result);
      
      Ashkelon ashkelon = new Ashkelon();
      ashkelon.init();
      ashkelon.updateInternalRefs();
      ashkelon.finish();
   }
}
