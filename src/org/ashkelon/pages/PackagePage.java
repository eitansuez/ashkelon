package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

public class PackagePage extends Page
{
   public PackagePage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      int pkgId = 0;

      try
      {
         pkgId = Integer.parseInt(ServletUtils.getRequestParam(request, "pkg_id"));
      }
      catch (NumberFormatException ex)
      {
         String pkgName = ServletUtils.getRequestParam(request, "pkg_name");
         
         String sql = DBMgr.getInstance().getStatement("getpkgid");
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, pkgName);
         ResultSet rset = pstmt.executeQuery();
         log.debug("about to get package id for "+pkgName);
         if (rset.next())
         {
            pkgId = rset.getInt(1);
            log.debug("pkg id is: "+pkgId);
         }
         rset.close();
         pstmt.close();
      }
      
      Integer pkgId_obj = new Integer(pkgId);

      Object packages_obj = app.getAttribute("packages");
      Hashtable packages = null;
      if (packages_obj == null)
      {
         packages = new Hashtable();
      }
      else
      {
         packages = (Hashtable) packages_obj;
         Object pkg_object = packages.get(pkgId_obj);
         if (pkg_object !=null)
         {
            JPackage pkg = (JPackage) pkg_object;
            request.setAttribute("pkg", pkg);

            TreeNode tree = pkg.buildTree(conn);
            request.setAttribute("tree", tree);
            
            return null;
         }
      }
       
      JPackage pkg = JPackage.makePackageFor(conn, pkgId);
      packages.put(pkgId_obj, pkg);
      app.setAttribute("packages", packages);
      request.setAttribute("pkg", pkg);

      TreeNode tree = pkg.buildTree(conn);
      request.setAttribute("tree", tree);
      return null;
   }

}