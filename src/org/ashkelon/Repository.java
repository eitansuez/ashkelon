/*
 * Created on Mar 19, 2005
 */
package org.ashkelon;

import java.io.*;

import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;
import org.ashkelon.vcs.IRepository;
import org.ashkelon.vcs.CVSRepository;
import org.ashkelon.vcs.SVNRepository;

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

   public String getType() { return type; }
   public String getUrl() { return url; }
   public String getModulename() { return modulename; }
   public String getTagname() { return tagname; }
   public String getSourcepath() { return sourcepath; }

   public String sourcepath()
   {
      if (!isSpecified()) return "";
      return type().sourcepath(this);
   }

   public void fetch(File base)
   {
      if (!isSpecified())
      {
         log.traceln("(no repository specified)");
         return;
      }

      if (checkedOut(base))
         update(base);
      else
         checkout(base);
   }

   public boolean isSpecified()
   {
      return !( StringUtils.isBlank(type) || StringUtils.isBlank(url) ||
               StringUtils.isBlank(modulename) );
   }

   public boolean checkedOut(File base)
   {
      File apipath = new File(base, modulename);
      return (apipath.exists());
   }

   public void checkout(File basepath)
   {
      type().checkout(basepath, this);
   }

   public void update(File basepath)
   {
      type().update(basepath, this);
   }

   private IRepository type()
   {
      if ("cvs".equals(type))
      {
         return CVSRepository.getInstance();
      }
      else if ("svn".equals(type))
      {
         return SVNRepository.getInstance();
      }
      else
      {
         throw new IllegalArgumentException("Invalid or unsupported repository type: "+type);
      }
   }

   public String toString()
   {
      String text = url + " ("+type+")\n";
      text += "module: "+modulename+"\n";
      text += "source is in: "+sourcepath+"\n";
      return text;
   }

}

