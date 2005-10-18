package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.sql.*;
import org.apache.oro.util.*;


/**
 * @author Eitan Suez
 */
public class ClassPage extends Page
{
   public ClassPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException, ClassNotFoundException
   {
      int clsId = 0;
      try
      {
         clsId = Integer.parseInt(ServletUtils.getRequestParam(request, "id"));
      }
      catch (NumberFormatException ex)
      {
         String clsName = ServletUtils.getRequestParam(request, "name");
         
         String sql = DBMgr.getInstance().getStatement("getclassid");
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, clsName);
         ResultSet rset = pstmt.executeQuery();
         try
         {
            if (rset.next())
            {
               clsId = rset.getInt(1);
            }
            else
            {
               // done: no such class..
               request.setAttribute("title", "Class " + clsName + " Not Found");
               String description = "ashkelon does not appear to contain the class " + 
                   "<span class=\"ordinaryClass\">" + clsName + "</span>" + 
                   " in its repository";
               request.setAttribute("description", description);
               return "message";
            }
         }
         finally
         {
            rset.close();
            pstmt.close();
         }
      }
      
      Integer clsId_obj = new Integer(clsId);

      
      Object classCacheObj = app.getAttribute("classCache");
      CacheLRU classCache = null;
      if (classCacheObj == null)
      {
         classCache = new CacheLRU(1000);
      }
      else
      {
         classCache = (CacheLRU) classCacheObj;
         Object clsobject = classCache.getElement(clsId_obj);
         if ( clsobject!=null  /* TODO:  && !dirty(clsId_obj) */ )
         {
            ClassType cls = (ClassType) clsobject;
            log.brief("retrieving class "+cls.getName()+" from cache");
            request.setAttribute("cls", cls);
            TreeNode superclasses = cls.getSuperclasses(conn);
            request.setAttribute("tree", superclasses);
            return null;
         }
      }
      
      ClassType cls = ClassType.makeClassFor(conn, clsId, true);
      
      classCache.addElement(clsId_obj, cls);
      app.setAttribute("classCache", classCache);

      
      request.setAttribute("cls", cls);
      TreeNode superclasses = cls.getSuperclasses(conn);
      request.setAttribute("tree", superclasses);
      return null;
   }
   
}
