/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

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