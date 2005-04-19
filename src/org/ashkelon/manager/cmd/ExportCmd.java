/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import java.sql.SQLException;
import org.ashkelon.manager.Ashkelon;
import org.jibx.runtime.JiBXException;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Eitan Suez
 */
public class ExportCmd extends BaseCmd
{
   public ExportCmd() {}
   
   public String getName() { return "export"; }
   public String getDescription()
   {
      return "Dump list of apis to xml (system.out)";
   }
   public String getExample()
   {
      return "ashkelon export";
   }
   public String getNote()
   {
      return "Does not drill down to packages, classes, and methods.";
   }
   
   public void registerParameters() throws JSAPException
   {
      super.registerParameters();
   }
   
   public void invoke(JSAPResult result)
   {
      super.invoke(result);
      
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
   
}
