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

import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

public class StatsPage extends Page
{
   public StatsPage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      if (app.getAttribute("api_count")==null)
      {
         int apis = DBUtils.getCount(conn, "API");
         int packages = DBUtils.getCount(conn, "PACKAGE");
         int classes = DBUtils.getCount(conn, "CLASSTYPE");
         int members = DBUtils.getCount(conn, "MEMBER");

         app.setAttribute("api_count", new Integer(apis));
         app.setAttribute("package_count", new Integer(packages));
         app.setAttribute("class_count", new Integer(classes));
         app.setAttribute("member_count", new Integer(members));
      }
      
      List apipkgcounts = getCountsFor("apistatspkg");
      List apiclscounts = getCountsFor("apistatscls");
      List apimmbcounts = getCountsFor("apistatsmmb");
      
      request.setAttribute("apipkgcounts", apipkgcounts);
      request.setAttribute("apiclscounts", apiclscounts);
      request.setAttribute("apimmbcounts", apimmbcounts);

      return null;
   }



   private List getCountsFor(String stmt_key) throws SQLException
   {
      DBMgr dbmgr = DBMgr.getInstance();
      String sql = dbmgr.getStatement(stmt_key);
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      List counts = new ArrayList(20);
      List pair;
      
      while (rset.next())
      {
         pair = new ArrayList(2);
         pair.add(new Integer(rset.getInt(1)));
         pair.add(rset.getString(2));
         counts.add(pair);
      }
      
      rset.close();
      stmt.close();
      
      return counts;
   }


}

