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

package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ashkelon.db.DBMgr;
import org.ashkelon.db.DBUtils;
import org.ashkelon.db.PKManager;
import org.ashkelon.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

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
   private String email;
   private List classes;

   private static String SEQUENCE = "AUTHOR_SEQ";
   private static String TABLENAME = "AUTHOR";

   private int id;
   private boolean idSet = false;
   
   public Author(String name)
   {
      parseName(name);
      setClasses(new ArrayList());
   }

   /**
    * add logic to properly parse out an <a href..> author tag into a name and an email
    * address
    */
   private void parseName(String data)
   {
     String name = data;
     String email = "";
     try
     {
       DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
       InputStream is = new ByteArrayInputStream(data.getBytes());
       Document d = builder.parse(is);
       Element root = d.getDocumentElement();
       NamedNodeMap attr = root.getAttributes();
       for (int i=0; i<attr.getLength(); i++)
       {
         email = attr.item(i).getNodeValue();
         int idx = email.indexOf("mailto:");
         email = email.substring(idx+7);
       }
       name = root.getFirstChild().getNodeValue();
     }
     catch (ParserConfigurationException pconfex) { }
     catch (SAXException saxex) { }
     catch (IOException ioex) { }

    setName(name);
    setEmail(email);
   }
   
   private void parseNameAlternative()
   {
    // regular expression:
    // to match email address: (\w+\@\w+\.\w+)
    // to match caption: >((\w+\s*)+)<
    // tbd.
   }
   
   public void store(Connection conn) throws SQLException
   {
      if (exists(conn))
         return;
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 120));
      fieldInfo.put("EMAIL", StringUtils.truncate(getEmail(), 120));
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
   
   public String getEmail() { return email; }
   public void setEmail(String email) { this.email = email; }

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
         author.setEmail(rset.getString(5));
         
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
