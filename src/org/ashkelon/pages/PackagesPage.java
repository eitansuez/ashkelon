package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;

public class PackagesPage extends Page
{
   public PackagesPage()
   {
      super();
   }

   public String init() throws SQLException
   {
      if (app.getAttribute("javaPkgs")!=null)
      {
         return null;  // done
      }
      
      List pkgList = getPackageList();
      //request.setAttribute("packageList", pkgList);
      List javaPkgs = new ArrayList();
      List javaxPkgs = new ArrayList();
      List orgPkgs = new ArrayList();
      List comPkgs = new ArrayList();
      
      JPackage pkg;
      for (int i=0; i<pkgList.size(); i++)
      {
         pkg = (JPackage) pkgList.get(i);
         if (pkg.getName().startsWith("java."))
         {
            javaPkgs.add(pkg);
         }
         else if (pkg.getName().startsWith("javax."))
         {
            javaxPkgs.add(pkg);
         }
         else if (pkg.getName().startsWith("org."))
         {
            orgPkgs.add(pkg);
         }
         else if (pkg.getName().startsWith("com."))
         {
            comPkgs.add(pkg);
         }
      }
      app.setAttribute("javaPkgs", javaPkgs);
      app.setAttribute("javaxPkgs", javaxPkgs);
      app.setAttribute("orgPkgs", orgPkgs);
      app.setAttribute("comPkgs", comPkgs);
      return null;
   }
   
   public List getPackageList() throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getpackages2");

      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List pkgList = new ArrayList();

      JPackage pkg;
      while (rset.next())
      {
         pkg = new JPackage(rset.getString(2));
         pkg.setId(rset.getInt(1));
         pkg.setDoc(new DocInfo(rset.getString(3), rset.getString(4), rset.getString(5)));
         pkgList.add(pkg);
      }
      
      rset.close();
      stmt.close();
      
      return pkgList;
   }
   
}