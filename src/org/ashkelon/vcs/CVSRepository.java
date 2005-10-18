/*
 * Created on Apr 20, 2005
 */
package org.ashkelon.vcs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StreamConsumer;
import org.ashkelon.util.StreamInteractor;
import org.ashkelon.util.StringUtils;
import org.ashkelon.Repository;
import org.ashkelon.Config;

/**
 * @author Eitan Suez
 */
public class CVSRepository implements IRepository
{
   private static CVSRepository _instance = null;

   public static CVSRepository getInstance()
   {
      if (_instance == null)
         _instance = new CVSRepository();
      return _instance;
   }

   private Logger log = Logger.getInstance();

   private CVSRepository() {}

   // cvs -d :pserver:anonymous@cvs.sourceforge.net:/cvsroot/ashkelon checkout ashkelon/src
   // cvs -d $url checkout $module/$srcpath

   public void checkout(File basepath, Repository r)
   {
      try
      {
         login(basepath, r);

         String[] sourcepaths = StringUtils.split(r.getSourcepath(), ":");
         String basecmd = "cvs -d " + r.getUrl() + " checkout " + revision(r) +
                             r.getModulename();
         String cmd;

         if (StringUtils.isBlank(r.getSourcepath()))
         {
            log.traceln("cmd is: " + basecmd);
            exec(basecmd, basepath);
            return;
         }

         for (int i=0; i<sourcepaths.length; i++)
         {
            cmd = basecmd +  File.separator + sourcepaths[i];
            log.traceln("cmd is: " + cmd);
            exec(cmd, basepath);
         }
      }
      catch (IOException ex)
      {
         log.error("cvs checkout failed!");
         log.error("IOException: "+ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         log.error("cvs checkout process interrupted");
         log.error("InterrupedException: "+ex.getMessage());
      }
   }

   private void login(File basepath, Repository r) throws IOException, InterruptedException
   {
      String cmd = "cvs -d " + r.getUrl() + " login";
      Process p = Runtime.getRuntime().exec(cmd, null, basepath);

      new StreamConsumer(p.getErrorStream()).start();
      new StreamInteractor(p.getInputStream(), p.getOutputStream(), "(Logging in to ", "").start();
      int exitValue = p.waitFor();
   }


   private void exec(String cmd, File basepath) throws IOException, InterruptedException
   {
      log.debug("exec'ing: "+cmd);
      Process p = Runtime.getRuntime().exec(cmd, null, basepath);
      InputStream is = p.getInputStream();
      InputStream er = p.getErrorStream();
      new StreamConsumer(is).start();
      new StreamConsumer(er).start();
      int exitValue = p.waitFor();
   }

   public void update(File basepath, Repository r)
   {
      try
      {
         String[] sourcepaths = StringUtils.split(r.getSourcepath(), ":");
         String basecmd = "cvs -d " + r.getUrl() + " -q update -d " + revision(r) +
                  r.getModulename() + File.separator;

         String cmd;
         for (int i=0; i<sourcepaths.length; i++)
         {
            cmd = basecmd+sourcepaths[i];
            log.traceln("cmd is: "+cmd);
            exec(cmd, basepath);
         }

      }
      catch (IOException ex)
      {
         log.error("cvs update failed!");
         log.error("IOException: "+ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         log.error("cvs update process interrupted");
         log.error("InterruptedException: "+ex.getMessage());
      }
   }

   private String revision(Repository r)
   {
      String tagname = r.getTagname();
      return (StringUtils.isBlank(tagname)) ? " -r HEAD " : " -r " + tagname + " ";
   }

   public String sourcepath(Repository r)
   {
      return sourcepath(r.getSourcepath(), r.getModulename());
   }
   public String sourcepath(String sourcepath, String modulename)
   {
      String basepath = Config.getInstance().getSourcePathBase();
      String[] paths = sourcepath.split(":");
      String expanded = "";
      for (int i=0; i<paths.length; i++)
      {
         expanded += basepath + File.separator +
                     modulename + File.separator +
                     paths[i];

         if (i<paths.length-1)
            expanded += ":";
      }
      return expanded;
   }

}
