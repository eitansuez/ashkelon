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

import org.apache.oro.util.*;

public class MemberPage extends Page
{
   public MemberPage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      //int memberId = Integer.parseInt(ServletUtils.getRequestParam(request, "member_id"));
      int memberId = 0;

      try
      {
         memberId = Integer.parseInt(ServletUtils.getRequestParam(request, "member_id"));
      }
      catch (NumberFormatException ex)
      {
         String memberName = ServletUtils.getRequestParam(request, "member_name");
         
         String sql = DBMgr.getInstance().getStatement("getmemberid");
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberName);
         ResultSet rset = pstmt.executeQuery();
         if (rset.next())
            memberId = rset.getInt(1);
         rset.close();
         pstmt.close();
      }
      
      Integer memberId_obj = new Integer(memberId);

      Object members_cache_obj = app.getAttribute("members_cache");
      CacheLRU members_cache = null;
      if (members_cache_obj == null)
      {
         members_cache = new CacheLRU(3000);
      }
      else
      {
         members_cache = (CacheLRU) members_cache_obj;
         Object member_object = members_cache.getElement(memberId_obj);
         if (member_object != null)
         {
            List members = (List) member_object;
            request.setAttribute("members", members);
            request.setAttribute("member", members.get(0));
            return null;
         }
      }
      
      List members = Member.makeMembersFor(conn, memberId);
      members_cache.addElement(memberId_obj, members);
      app.setAttribute("members_cache", members_cache);
      
      request.setAttribute("members", members);
      request.setAttribute("member", members.get(0));
      return null;
   }
   
}

