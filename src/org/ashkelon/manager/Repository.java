/*
 * Created on Mar 19, 2005
 */
package org.ashkelon.manager;

import java.io.*;

/**
 * @author Eitan Suez
 */
public class Repository
{
   private String type = "";
   private String url = "";
   private String modulename = "";
   private String sourcepath = "";
   
   public Repository() {}
   
   // cvs -d :pserver:anonymous@cvs.sourceforge.net:/cvsroot/ashkelon checkout ashkelon/src
   // cvs -d $url checkout $module/$srcpath
   
   public void checkout(File basepath)
   {
      try
      {
         login(basepath);
         
         String cmd = "cvs -d " + url + " checkout " + modulename + 
                  File.separator + sourcepath;
         System.out.println("cmd is: "+cmd);
         
         exec(cmd, basepath);
      }
      catch (IOException ex)
      {
         System.out.println("Checkout Failed.");
         System.err.println(ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         System.err.println("Checkout Process Interrupted;  exception: "+ex.getMessage());
      }
   }
   
   public void update(File basepath)
   {
      try
      {
         String cmd = "cvs -d " + url + " -q update -d " + modulename + 
                  File.separator + sourcepath;
         System.out.println("cmd is: "+cmd);
         exec(cmd, basepath);
      }
      catch (IOException ex)
      {
         System.out.println("Update Failed.");
         System.err.println(ex.getMessage());
      }
      catch (InterruptedException ex)
      {
         System.err.println("Update Process Interrupted;  exception: "+ex.getMessage());
      }
   }
   
   private void exec(String cmd, File basepath) throws IOException, InterruptedException
   {
      Process p = Runtime.getRuntime().exec(cmd, null, basepath);
      BufferedReader reader = 
         new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = reader.readLine();
      while (line != null)
      {
         System.out.println(line);
         line = reader.readLine();
      }
      p.waitFor();
      reader.close();
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
      p.waitFor();
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
   public String getModulename() { return modulename; }
   public String getUrl() { return url; }
   public String getSourcepath() { return sourcepath; }
   
   public String getPath() { return modulename + File.separator + sourcepath; }
   
   public boolean checkedOut(File base)
   {
      File apipath = new File(base, modulename);
      return (apipath.exists());
   }
   
   public void fetch(File base)
   {
      if (!checkedOut(base))
      {
         checkout(base);
      }
      else
      {
         update(base);
      }
      
   }
   
}

