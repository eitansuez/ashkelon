package org.ashkelon.pages;

import org.ashkelon.util.*;
import java.io.*;
import java.util.*;
import java.sql.*;


/**
 * @author Eitan Suez
 */
public class ClassSourcePage extends Page
{
   private File srcHtmlDir = null;
   private static final String SRCHTMLDIRNAME = "srchtml";
   private HtmlGenerator generator = new Java2HtmlGenerator();

   public ClassSourcePage()
   {
      super();
   }
   
   public void setApplication(javax.servlet.ServletContext context)
   {
      super.setApplication(context);
      
      srcHtmlDir = new File(context.getRealPath("/") + File.separator + SRCHTMLDIRNAME);
      if (!srcHtmlDir.exists()) srcHtmlDir.mkdir();

      generator.initialize(srcHtmlDir);
   }
   
   
   public String handleRequest() throws SQLException
   {
      String qualifiedName = ServletUtils.getRequestParam(request, "cls_name");
      String fileName = qualifiedName.replace('.', File.separatorChar) + ".java";
      
      request.setAttribute("cls_name", qualifiedName);
      
      List sourcepaths = (List) app.getAttribute("sourcepath");
      log.debug("number of paths in source path: "+sourcepaths.size());
      
      Iterator itr = sourcepaths.iterator();
      String path = null;
      String sourceFile = null;
      while (itr.hasNext())
      {
         path = (String) itr.next();
         if (!path.endsWith(File.separator))
            path += File.separator;
         sourceFile = path + fileName;
         log.debug("looking for "+sourceFile);
         if (new File(sourceFile).exists())
         {
            request.setAttribute("source_file", sourceFile);
            String htmlFile = SRCHTMLDIRNAME + File.separator + fileName + ".html";
            request.setAttribute("html_file", htmlFile);
            String realHtmlFile = srcHtmlDir + File.separator + fileName + ".html";
            
            if (! (new File(realHtmlFile).exists()) )
            {
               generator.produceHtml(sourceFile, realHtmlFile);
            }
            
            return null;
         }
      }

      request.setAttribute("source_file", "");
      return null;
   }
   
}
