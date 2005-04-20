package org.ashkelon.pages;

import org.ashkelon.db.DBMgr;
import org.ashkelon.manager.Config;
import org.ashkelon.util.*;
import java.io.*;
import java.sql.*;
import java.util.Iterator;
import java.util.List;


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
      
      File sourceFile = fetchSourceFile(qualifiedName, fileName);
      if (sourceFile != null)
      {
         makeHtmlFile(sourceFile, fileName);
         return null;
      }
      
      // try maybe inner class..
      int idx = qualifiedName.lastIndexOf(".");
      boolean isPossibleInnerClass = (idx > 0) && 
                     (idx < qualifiedName.length() - 1) && 
                     (Character.isUpperCase(qualifiedName.charAt(idx+1)));
      if (isPossibleInnerClass)
      {
         String containingClass = qualifiedName.substring(0, idx);
         String containingFile = containingClass.replace('.', File.separatorChar) + ".java";
         sourceFile = fetchSourceFile(containingClass, containingFile);
         
         if (sourceFile != null)
         {
            makeHtmlFile(sourceFile, containingFile);
            return null;
         }
      }
      
      request.setAttribute("source_file", "");
      return null;
   }
   
   private void makeHtmlFile(File sourceFile, String fileName)
   {
      request.setAttribute("source_file", sourceFile);
      String htmlFile = SRCHTMLDIRNAME + File.separator + fileName + ".html";
      request.setAttribute("html_file", htmlFile);
      String realHtmlFile = srcHtmlDir + File.separator + fileName + ".html";
      
      if (! (new File(realHtmlFile).exists()) )
      {
         generator.produceHtml(sourceFile.getAbsolutePath(), realHtmlFile);
      }
   }
   
   // TODO: this should be encapsulated in API
   private String fetchSourcePath(String className) throws SQLException
   {
      String base = Config.getInstance().getSourcePathBase();
      
      String sql = DBMgr.getInstance().getStatement("getsourcepath");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, className);
      ResultSet rset = pstmt.executeQuery();
      rset.next();
      String modulename = rset.getString(1);
      String sourcepath = rset.getString(2);
      rset.close();
      pstmt.close();
      
      String[] paths = sourcepath.split(":");
      String expanded = "";
      for (int i=0; i<paths.length; i++)
      {
         expanded += base + File.separator + modulename + 
                  File.separator + paths[i];
         if (i < (paths.length - 1))
            expanded += ":";
      }
      return expanded;
   }
   
   private File fetchSourceFile(String qualifiedName, String fileName) throws SQLException
   {
      // first search in repository path, and fall back to specified sourcepath
      String[] sourcepaths = {fetchSourcePath(qualifiedName), 
            (String) app.getAttribute("sourcepath")};
      
      File sourceFile = null;
      for (int i=0; i<sourcepaths.length; i++)
      {
         sourceFile = searchIn(sourcepaths[i], fileName);
         if (sourceFile != null) return sourceFile;
      }
      return sourceFile;
   }
   
   private File searchIn(String sourcepath, String fileName)
   {
      String[] paths = StringUtils.split(sourcepath, ":");
      File sourceFile = null;
      for (int i=0; i<paths.length; i++)
      {
         sourceFile = composeFile(paths[i], fileName);
         if (sourceFile.exists())
         {
            log.debug("Found sourceFile at: "+sourceFile.getAbsolutePath());
            return sourceFile;
         }
      }
      return null;
   }
      
   private File composeFile(String path, String fileName)
   {
      if (!path.endsWith(File.separator))
         path += File.separator;
      path += fileName;
      return new File(path);
   }

   
}
