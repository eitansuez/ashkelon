package org.ashkelon.pages;

import org.ashkelon.util.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class ClassSourcePage extends Page
{
   public ClassSourcePage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      String qualifiedName = ServletUtils.getRequestParam(request, "cls_name");
      String fileName = qualifiedName.replace('.', File.separatorChar) + ".java";
      
      request.setAttribute("cls_name", qualifiedName);
      
      //List sourcepaths = getSourcePaths();
      List sourcepaths = (List) app.getAttribute("sourcepath");
      
      log.debug("number of paths in source path: "+sourcepaths.size());
      
      Iterator itr = sourcepaths.iterator();
      String path = null;
      String canonical = null;
      while (itr.hasNext())
      {
         path = (String) itr.next();
         if (!path.endsWith(File.separator))
            path += File.separator;
         canonical = path + fileName;
         log.debug("looking for "+canonical);
         if (new File(canonical).exists())
         {
            request.setAttribute("source_file", canonical);
            return null;
         }
      }

      request.setAttribute("source_file", "");
      return null;
   }
   
}
