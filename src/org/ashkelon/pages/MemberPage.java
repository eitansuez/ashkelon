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
   
   public String handleRequest() throws SQLException
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

