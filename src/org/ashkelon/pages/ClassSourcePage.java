package org.ashkelon.pages;

import org.ashkelon.db.DBMgr;
import org.ashkelon.manager.Config;
import org.ashkelon.util.*;
import java.io.*;
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
      
      // determine class.api (maybe pass it in the request, because already know it)
      // lookup the api (possibly from cache)
      // from the api, get the sourcepath
      
      String sourcepath = fetchSourcePath(qualifiedName);
      if (!sourcepath.endsWith(File.separator))
         sourcepath += File.separator;
      String sourceFile = sourcepath + fileName;
      log.debug("looking for "+sourceFile);
      if (new File(sourceFile).exists())
      {
         makeHtmlFile(sourceFile, fileName);
         return null;
      }
      
      request.setAttribute("source_file", "");
      return null;
   }
   
   private void makeHtmlFile(String sourceFile, String fileName)
   {
      request.setAttribute("source_file", sourceFile);
      String htmlFile = SRCHTMLDIRNAME + File.separator + fileName + ".html";
      request.setAttribute("html_file", htmlFile);
      String realHtmlFile = srcHtmlDir + File.separator + fileName + ".html";
      
      if (! (new File(realHtmlFile).exists()) )
      {
         generator.produceHtml(sourceFile, realHtmlFile);
      }
   }
   
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
      
      return base + File.separator + modulename + File.separator + sourcepath;
   }
   
}
