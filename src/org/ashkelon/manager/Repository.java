/*
 * Created on Mar 19, 2005
 */
package org.ashkelon.manager;

import java.io.*;

import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

/**
 * @author Eitan Suez
 */
public class Repository
{
   private String type = "";
   private String url = "";
   private String modulename = "";
   private String tagname = "";
   private String sourcepath = "";
   
   private Logger log = Logger.getInstance();
   
   public Repository() {}
   public Repository(String type, String url, String modulename, String tagname,
         String sourcepath)
   {
      this();
      this.type = type;
      this.url = url;
      this.modulename = modulename;
      this.tagname = tagname;
      this.sourcepath = sourcepath;
   }
   
   public boolean isSpecified()
   {
      return !( StringUtils.isBlank(type) || StringUtils.isBlank(url) ||
               StringUtils.isBlank(modulename) );
   }
   
   // cvs -d :pserver:anonymous@cvs.sourceforge.net:/cvsroot/ashkelon checkout ashkelon/src
   // cvs -d $url checkout $module/$srcpath
   
   public void checkout(File basepath)
   {
      try
      {
         login(basepath);
         
         String cmd = "cvs -d " + url + " checkout " + revision() + 
                  modulename + File.separator + sourcepath;
         
         log.traceln("cmd is: "+cmd);
         exec(cmd, basepath);
      }
      catch (IOException ex)
      {
         log.error("checkout failed!");
         log.error("IOException: "+ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         log.error("Checkout process interrupted");
         log.error("InterrupedException: "+ex.getMessage());
      }
   }
   
   private String revision()
   {
      return (StringUtils.isBlank(tagname)) ? " -r HEAD " : " -r " + tagname + " ";
   }
   
   public void update(File basepath)
   {
      try
      {
         String cmd = "cvs -d " + url + " -q update -d " + revision() + 
                  modulename + File.separator + sourcepath;
         log.traceln("cmd is: "+cmd);
         exec(cmd, basepath);
      }
      catch (IOException ex)
      {
         log.error("Update failed!");
         log.error("IOException: "+ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         log.error("Update Process Interrupted");
         log.error("InterruptedException: "+ex.getMessage());
      }
   }
   
   private void exec(String cmd, File basepath) throws IOException, InterruptedException
   {
      Process p = Runtime.getRuntime().exec(cmd, null, basepath);
      InputStream is = p.getInputStream();
      InputStream er = p.getErrorStream();
      new StreamConsumer(is).start();
      new StreamConsumer(er).start();
      int exitValue = p.waitFor();
      is.close();
      er.close();
   }
   
   
   private void login(File basepath) throws IOException, InterruptedException
   {
      String cmd = "cvs -d " + url + " login";
      Process p = Runtime.getRuntime().exec(cmd, null, basepath);
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      OutputStream os = new BufferedOutputStream(p.getOutputStream());
      
      String line = reader.readLine();
      while ( line!= null )
         line = reader.readLine();
      os.write('\n');
      int exitValue = p.waitFor();
      reader.close();
      os.close();
   }
   
   public String toString()
   {
      String text = url + " ("+type+")\n";
      text += "module: "+modulename+"\n";
      text += "source is in: "+sourcepath+"\n";
      return text;
   }
   
   public String getType() { return type; }
   public String getUrl() { return url; }
   public String getModulename() { return modulename; }
   public String getTagname() { return tagname; }
   public String getSourcepath() { return sourcepath; }
   
   public String getPath() { return modulename + File.separator + sourcepath; }
   
   public boolean checkedOut(File base)
   {
      File apipath = new File(base, modulename);
      return (apipath.exists());
   }
   
   public void fetch(File base)
   {
      if (!isSpecified())
      {
         log.traceln("(no repository specified)");
         return;
      }
      
      if (!checkedOut(base))
      {
         checkout(base);
      }
      else
      {
         update(base);
      }
   }

   
   class StreamConsumer extends Thread
   {
      InputStream _stream;
      StreamConsumer(InputStream stream)
      {
         _stream = stream;
      }
      public void run()
      {
         BufferedReader reader = null;
         try
         {
            reader = new BufferedReader(new InputStreamReader(_stream));
            String line = reader.readLine();
            while (line != null)
            {
               log.traceln(line);
               line = reader.readLine();
            }
         }
         catch (IOException ex)
         {
            System.err.println("IOException: "+ex.getMessage());
            ex.printStackTrace();
         }
         finally
         {
            try
            {
               if (reader != null) reader.close();
            }
            catch (IOException ex)
            {
               System.err.println("Exception attempting to closer stream/reader");
               System.err.println("IOException: "+ex.getMessage());
               ex.printStackTrace();
            }
         }
      }
   }
   
   
}

