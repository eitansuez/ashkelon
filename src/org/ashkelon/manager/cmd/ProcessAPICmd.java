package org.ashkelon.manager.cmd;

import com.martiansoftware.jsap.*;
import org.ashkelon.API;
import org.ashkelon.Config;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 10, 2005
 * Time: 6:45:02 PM
 */
public abstract class ProcessAPICmd extends BaseCmd
{
   public abstract String docletClassName();

   public void registerParameters() throws JSAPException
   {
      super.registerParameters();

      FlaggedOption sourcepathOption = new FlaggedOption("sourcepath")
                                          .setRequired(false)
                                          .setShortFlag(JSAP.NO_SHORTFLAG)
                                          .setLongFlag("sourcepath");

      sourcepathOption.setHelp("Source code path information;  where" +
            " ashkelon can locate your source code.  This is no longer" +
            " recommended.  Try to specify source repository information" +
            " for API instead.");
      registerParameter(sourcepathOption);

      FlaggedOption classpathOption = new FlaggedOption("classpath")
                                          .setRequired(false)
                                          .setShortFlag(JSAP.NO_SHORTFLAG)
                                          .setLongFlag("classpath");

      classpathOption.setHelp("Class path information;  passed to javadoc (optional).");
      registerParameter(classpathOption);

      FlaggedOption sourceOption = new FlaggedOption("source")
                                       .setRequired(false)
                                       .setShortFlag(JSAP.NO_SHORTFLAG)
                                       .setLongFlag("source");
      sourceOption.setHelp("javadoc -source flag: to specify j2se version compatibility");
      registerParameter(sourceOption);

      FlaggedOption encodingOption = new FlaggedOption("encoding")
                                       .setRequired(false)
                                       .setShortFlag(JSAP.NO_SHORTFLAG)
                                       .setLongFlag("encoding");
      encodingOption.setHelp("javadoc -encoding flag: to specify source encoding");
      registerParameter(encodingOption);
   }

   protected void fetchSource(API api)
   {
      String basepath = Config.getInstance().getSourcePathBase();
      File base = new File(basepath);
      if (!base.exists())
      {
         log.traceln("Creating directory "+basepath);
         base.mkdir();
      }
      api.fetch(base);  // fetch api source code from source repository
   }

}
