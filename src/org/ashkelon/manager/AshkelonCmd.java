package org.ashkelon.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.ashkelon.manager.cmd.*;
import org.ashkelon.util.Logger;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Eitan Suez
 */
public class AshkelonCmd
{
   static Map COMMANDS = new HashMap();
   static
   {
      COMMANDS.put("list", new ListCmd());
      COMMANDS.put("remove", new RemoveCmd());
      COMMANDS.put("add", new AddCmd());
      COMMANDS.put("update", new UpdateCmd());
      COMMANDS.put("reset", new ResetCmd());
      COMMANDS.put("export", new ExportCmd());
   }
   
   public static void main(String[] args) throws JSAPException
   {
      Logger log = Logger.getInstance();
      
      if (args == null || args.length == 0)
      {
         printUsage();
         return;
      }
      
      BaseCmd cmd = (BaseCmd) COMMANDS.get(args[0]);
      if (cmd == null)
      {
         printUsage();
         return;
      }
      
      String[] cmdlineargs = new String[args.length - 1];
      for (int i=1; i<args.length; i++)
         cmdlineargs[i-1] = args[i];
      
      cmd.registerParameters();
      
      JSAPResult result = cmd.parse(cmdlineargs);
      
      if (!result.success())
      {
         cmd.printUsage(result);
         return;
      }
      
      log.setPrefix(cmd.getName());
      cmd.invoke(result);
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
