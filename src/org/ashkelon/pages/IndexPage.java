package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

/**
 * @author Eitan Suez
 */
public class IndexPage extends Page
{
   private static int PAGE_SIZE = 21;
   
   public IndexPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException
   {
      String startFrom = ServletUtils.getRequestParam(request, "start");
      String cmd = ServletUtils.getCommand(request);
      
      if (StringUtils.isBlank(startFrom))
         return null;
      
      String[] parts = StringUtils.split(cmd, ".");
      String browseType = parts[1];
      List results = null;
      
      if (browseType.equals("package"))
      {
         results = getPackages(startFrom);
         if (results.size()>=PAGE_SIZE)
         {
            JPackage next = (JPackage) results.remove(results.size()-1);
            request.setAttribute("next", next.getName());
         }
      }
      else if (browseType.equals("class"))
      {
         results = getClasses(startFrom);
         if (results.size()>=PAGE_SIZE)
         {
            ClassType next = (ClassType) results.remove(results.size()-1);
            request.setAttribute("next", next.getName());
         }
      }
      else if (browseType.equals("member"))
      {
         results = getMembers(startFrom);
         if (results.size()>=PAGE_SIZE)
         {
            Member next = (Member) results.remove(results.size()-1);
            request.setAttribute("next", next.getName());
         }
      }
      else if (browseType.equals("author"))
      {
         results = getAuthors(startFrom);
         if (results.size()>=PAGE_SIZE)
         {
            Author next = (Author) results.remove(results.size()-1);
            request.setAttribute("next", next.getName());
         }
      }
      
      request.setAttribute("results", results);
      if (results.isEmpty())
         request.setAttribute("next", "");
      
      request.setAttribute("display_results", new Boolean(true));
      return null;
   }
   
   
   public List getPackages(String startFrom) throws SQLException
   {
      startFrom = startFrom.toLowerCase();

      String sql = DBMgr.getInstance().getStatement("getpackages");
      
      PreparedStatement p = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      p.setFetchSize(PAGE_SIZE);
      p.setFetchDirection(ResultSet.FETCH_FORWARD);
      p.setMaxFieldSize(PAGE_SIZE);
      p.setString(1, startFrom);
      ResultSet rset = p.executeQuery();

      List results = new ArrayList();
      JPackage pkg;
      
      int rownum = 0;
      while (rset.next() && rownum < PAGE_SIZE)
      {
        pkg = new JPackage(rset.getString(2));
        pkg.setId(rset.getInt(1));
        pkg.setDoc(new DocInfo(rset.getString(3), rset.getString(4), rset.getString(5)));
        results.add(pkg);
         
        rownum++;
      }
      rset.close();
      p.close();
      
      return results;
   }
   
   public List getClasses(String startFrom) throws SQLException
   {
      startFrom = startFrom.toLowerCase();

      String sql = DBMgr.getInstance().getStatement("getclasses");

      PreparedStatement p = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      p.setFetchSize(PAGE_SIZE);
      p.setFetchDirection(ResultSet.FETCH_FORWARD);
      p.setMaxRows(PAGE_SIZE);
      
      p.setString(1, startFrom);
      ResultSet rset = p.executeQuery();
      
      List results = new ArrayList();
      ClassType c;
      
      int rownum = 0;
      while (rset.next() && rownum < PAGE_SIZE)
      {
        c = new ClassType(rset.getString(2));
        c.setId(rset.getInt(1));
        c.setClassType(rset.getInt(3));
        c.setStatic(rset.getBoolean(4));
        c.setFinal(rset.getBoolean(5));
        c.setAbstract(rset.getBoolean(6));
        c.setAccessibility(rset.getInt(7));
        c.setModifiers(rset.getString(8));
        c.setDoc(new DocInfo(rset.getString(9), rset.getString(10), rset.getString(11)));
        results.add(c);
        rownum++;
      }

      rset.close();
      p.close();
      
      return results;
   }
   
   
   
   public List getMembers(String startFrom) throws SQLException
   {
      startFrom = startFrom.toLowerCase();

      String sql = DBMgr.getInstance().getStatement("getmembers");
      
      PreparedStatement p = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      p.setFetchSize(PAGE_SIZE);
      p.setFetchDirection(ResultSet.FETCH_FORWARD);
      p.setMaxRows(PAGE_SIZE);
      
      p.setString(1, startFrom);
      ResultSet rset = p.executeQuery();

      List results = new ArrayList();
      Member m;

      int rownum = 0;
      while (rownum<PAGE_SIZE && rset.next())
      {
        m = new Member(rset.getString(2), rset.getInt(3));
        m.setId(rset.getInt(1));
        m.setDoc(new DocInfo(rset.getString(13), rset.getString(14), rset.getString(15)));
        m.setStatic(rset.getBoolean(4));
        m.setFinal(rset.getBoolean(5));
        m.setAccessibility(rset.getInt(6));
        m.setModifiers(rset.getString(7));
        String signature = rset.getString(12);
        if (!StringUtils.isBlank(signature))
        {
           ExecMember em = new ExecMember(m, signature);
           String returntypename = rset.getString(10);
           if (!StringUtils.isBlank(returntypename))
           {
               MethodMember method = new MethodMember(m, signature);
               method.setAbstract(rset.getBoolean(8));
               method.setReturnTypeName(returntypename);
               method.setReturnTypeDimension(rset.getInt(11));
               int returntypeid = rset.getInt(9);
               if (returntypeid > 0)
               {
                  method.setReturnType(new ClassType(returntypename));
                  method.getReturnType().setId(returntypeid);
               }
               
               results.add(method);
           }
           else
           {
              results.add(em);
           }
        }
        else
        {
           results.add(m);
        }
         
        rownum++;
      }
      rset.close();
      p.close();
      
      return results;
   }



   public List getAuthors(String startFrom) throws SQLException
   {
      startFrom = startFrom.toLowerCase();

      String sql = DBMgr.getInstance().getStatement("getauthors");

      PreparedStatement p = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      p.setFetchSize(PAGE_SIZE);
      p.setFetchDirection(ResultSet.FETCH_FORWARD);
      p.setMaxRows(PAGE_SIZE);
      
      p.setString(1, startFrom);
      ResultSet rset = p.executeQuery();
      
      List results = new ArrayList();
      
      int rownum = 0;
      Author author = null;
      while (rset.next() && rownum < PAGE_SIZE)
      {
        author = new Author(rset.getString(2));
        author.setId(rset.getInt(1));
        results.add(author);
        rownum++;
      }

      rset.close();
      p.close();
      
      return results;
   }
}



