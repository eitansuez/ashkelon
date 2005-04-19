/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import java.util.Iterator;

import org.ashkelon.util.Logger;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * @author Eitan Suez
 */
public abstract class BaseCmd extends JSAP implements CommandLineCommand
{
   public BaseCmd() { super(); }
   
   public void registerParameters() throws JSAPException
   {
      Switch verbose = new Switch("verbose")
                        .setLongFlag("verbose");
      verbose.setHelp("Turn on verbose logging");
      registerParameter(verbose);
      
      Switch debug = new Switch("debug")
                        .setLongFlag("debug");
      debug.setHelp("Debug mode (lots of logging)");
      registerParameter(debug);
      
      Switch help = new Switch("help").setLongFlag("help")
                                     .setShortFlag('h');
      help.setHelp("Show help information for command");
      registerParameter(help);
   }
   
   public void invoke(JSAPResult arguments)
   {
      Logger log = Logger.getInstance();
      if (arguments.getBoolean("verbose"))
      {
         log.setTraceLevel(Logger.VERBOSE);
      }
      else if (arguments.getBoolean("debug"))
      {
         log.setTraceLevel(Logger.DEBUG);
      }
      
      if (arguments.getBoolean("help"))
      {
         printUsage(null);
         System.exit(0);
      }
      
   }
   
   public abstract String getName();
   public abstract String getDescription();
   
   public void printUsage(JSAPResult result)
   {
      if (result!=null && !result.success())
      {
         Iterator itr = result.getErrorMessageIterator();
         while (itr.hasNext())
         {
            System.err.println("Error: " + itr.next());
         }
      }
      
      System.err.println("Description:\n\t"+getDescription()+"\n");
      System.err.println("Usage:  ashkelon "+getName()+" "+getUsage()+"\n");
      System.err.println(getHelp());
   }
   
}
