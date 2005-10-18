package org.ashkelon.util;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 10, 2005
 * Time: 7:14:44 PM
 */
public class Options
{
   static Logger log = Logger.getInstance();

   public static boolean getCommandLineOption(String option, String[][] options)
   {
      boolean value = false;
      for (int i = 0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            value = true;
         }
      }
      return value;
   }

   public static int getCommandLineOption(String option, String[][] options, int defaultValue)
   {
      int value = defaultValue;
      for (int i = 0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            try
            {
               value = Integer.parseInt(opt[1]);
            } catch (NumberFormatException ex)
            {
               value = defaultValue;
            }
         }
      }
      return value;
   }

   public static String getStringCommandLineOption(String option, String[][] options)
   {
      for (int i=0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            return opt[1];
         }
      }
      return "";
   }

   public static File getFileCommandLineOption(String option,
                                               String[][] options,
                                               File defaultValue)
   {
      return fileCmdOption(option, options, false, false, defaultValue);
   }
   public static File getDirCommandLineOption(String option, 
                                              String[][] options,
                                              File defaultValue)
   {
      return fileCmdOption(option, options, true, false, defaultValue);
   }
   public static File getOutputDirCommandLineOption(String option, 
                                                    String[][] options,
                                                    File defaultValue)
   {
      return fileCmdOption(option, options, true, true, defaultValue);
   }
   private static File fileCmdOption(String option, 
                                     String[][] options, 
                                     boolean expectDirectory,
                                     boolean forOutput,
                                     File defaultValue)
   {
      String optionTag, optionValue;
      File file;
      for (int i=0; i<options.length; i++)
      {
         optionTag = options[i][0];
         if (optionTag.equals(option))
         {
            optionValue = options[i][1];
            log.debug("found option: "+option+"; value is: "+optionValue);
            file = new File(optionValue);
            if (!file.exists() && expectDirectory && forOutput)
            {
               file.mkdirs();
               log.verbose("Created directory path: "+file.getAbsolutePath());
            }
            
            if (!file.exists())
            {
               String msg = "Invalid option value for "+option;
               msg += "; file " + file.getAbsolutePath() + " does not exist";
               throw new RuntimeException(msg);
            }
            
            if (file.isDirectory() == expectDirectory)
            {
               return file;
            }
            else
            {
               String perhaps = (file.isDirectory()) ? "" : "not";
               String msg = "Invalid option value for " + option;
               msg += "(file " + file.getAbsolutePath() + " is " + perhaps + " a directory)";
               throw new RuntimeException(msg);
            }
         }
      }

      return defaultValue;
   }
}
