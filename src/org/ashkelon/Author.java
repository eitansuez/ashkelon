package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Part of Persistable javadoc object model known as 'ashkelon'
 * An author represents a java class or interface author
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class Author implements Serializable
{
   private String name;
   private List classes;

   private static String SEQUENCE = "AUTHOR_SEQ";
   private static String TABLENAME = "AUTHOR";

   private int id;
   private boolean idSet = false;
   
   public Author(String name)
   {
      setName(name);
      setClasses(new ArrayList());
   }
   
   public void store(Connection conn) throws SQLException
   {
      if (exists(conn))
         return;
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 120));
      DBUtils.insert(conn, TABLENAME, fieldInfo);
      conn.commit();
   }

   public int getId(Connection conn) throws SQLException
   {
      if (!idSet)
      {
         //id = DBUtils.getNextVal(conn, SEQUENCE);
         id = PKManager.getInstance().nextVal(SEQUENCE);
         idSet = true;
      }
      return id;
   }

   public int getId()
   {
      return id;
   }
   
   public void setId(int id)
   {
      this.id = id;
      idSet = true;
   }
   
   
   private boolean exists(Connection conn) throws SQLException
   {
      Map constraints = new HashMap();
      constraints.put("NAME", getName());
      Object obj = DBUtils.getObject(conn, TABLENAME, "ID", constraints);
      if (obj == null)
         return false;
      
      if (!idSet)
      {
         id = ((Number) obj).intValue();
         idSet = true;
      }
      return true;
   }
   
   // accessors
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   
   public void setClasses(List classes) { this.classes = classes; }
   public List getClasses() { return classes; }
   
   public void addClass(ClassType classtype) { classes.add(classtype); }
   
   
   public String toString()
   {
      return getName();
   }
   
   
   public void fetchClasses(Connection conn) throws SQLException
   {
      String sql =  DBMgr.getInstance().getStatement("fetch_authorclasses");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      ClassType ct;
      while (rset.next())
      {
         ct = new ClassType(rset.getString(2));
         ct.setId(rset.getInt(1));
         addClass(ct);
      }
      
      rset.close();
      pstmt.close();
   }
   
   public static Author fetchAuthor(Connection conn, int id) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("fetchauthor");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, id);
      ResultSet rset = pstmt.executeQuery();
      
      ClassType ct;
      Author author = null;
      
      if (rset.next())
      {
         author = new Author(rset.getString(1));
         author.setId(id);
         
         ct = new ClassType(rset.getString(3));
         ct.setId(rset.getInt(2));
         ct.setClassType(rset.getInt(4));
         author.addClass(ct);
      }
      
      while (rset.next())
      {
         ct = new ClassType(rset.getString(3));
         ct.setId(rset.getInt(2));
         ct.setClassType(rset.getInt(4));
         author.addClass(ct);
      }
      
      rset.close();
      pstmt.close();
      
      return author;
   }
   
   
}
