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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ashkelon.db.DBUtils;
import org.ashkelon.db.PKManager;
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.SeeTag;

/**
 * Part of Persistable javadoc object model known as ashkelon
 * Analog of com.sun.javadoc.Doc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class DocInfo implements Serializable
{
   private String since;
   private String deprecated;
   private String summaryDescription;
   private String description;
   private int docType;
   private List references;
   
   private int id;
   private boolean idSet = false;

   private static String SEQUENCE = "DOC_SEQ";
   private static String TABLENAME = "DOC";

   /** constants representing different primary types of doc elements */
   public static final int PACKAGE_TYPE = 1;
   /** constants representing different types of doc elements */
   public static final int CLASS_TYPE = 2;
   /** constants representing different types of doc elements */
   public static final int MEMBER_TYPE = 3;
   /** constants representing different types of doc elements */
   public static final int EXECMEMBER_TYPE = 4;
   
   public static final String[] DOCTYPES = {"pkg", "cls", "member", "member"};
   
   
   public DocInfo()
   {
      setSummaryDescription("");
      setDescription("");
      setSince("");
      setDeprecated("");
      setReferences(new ArrayList());
   }
   
   public DocInfo(String summaryDescription, String since, String deprecated)
   {
      this(summaryDescription, since, deprecated, "");
   }
   
   public DocInfo(String summaryDescription, String since, String deprecated, String description)
   {
      setSummaryDescription(summaryDescription);
      setDescription(description);
      setSince(since);
      setDeprecated(deprecated);
      setReferences(new ArrayList());
   }
   
   public DocInfo(Doc doc)
   {
      this();
      setSince(JDocUtil.getTagText(doc.tags("@since")));
      setDeprecated(JDocUtil.resolveDescription(this, doc.tags("@deprecated")));
      
      addReferences(doc.seeTags());
      setDocType(getDocType(doc));

      setSummaryDescription(JDocUtil.resolveDescription(this, doc.firstSentenceTags()));
      setDescription(JDocUtil.resolveDescription(this, doc.inlineTags()));
      /*
      setSummaryDescription(JDocUtil.getTagText(doc.firstSentenceTags()));
      setDescription(doc.commentText());
       */
   }
   
   public void store(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(10);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("DOC_TYPE", new Integer(getDocType()));

      fieldInfo.put("SINCE", StringUtils.truncate(getSince(), 75));
      fieldInfo.put("DEPRECATED", StringUtils.truncate(getDeprecated(), 100));
      fieldInfo.put("SUMMARYDESCRIPTION", getSummaryDescription());
      fieldInfo.put("DESCRIPTION", getDescription());
      
      try
      {
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
      catch (SQLException ex)
      {
         fieldInfo.put("SUMMARYDESCRIPTION", StringUtils.truncate(getSummaryDescription(), 400));
         fieldInfo.put("DESCRIPTION", StringUtils.truncate(getDescription(), 3600));
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
      
      
      for (int i=0; i<getReferences().size(); i++)
      {
         ((Reference) references.get(i)).store(conn);
      }
   }
   
   public static void delete(Connection conn, int docid) throws SQLException
   {
      HashMap constraint = new HashMap();
      constraint.put("SOURCEDOC_ID", new Integer(docid));
      DBUtils.delete(conn, "REFERENCE", constraint);
      
      constraint.clear();
      constraint.put("id", new Integer(docid));
      DBUtils.delete(conn, TABLENAME, constraint);
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
   
   // accessor methods
   public String getSince() {  return since; }
   public void setSince(String since)
   {
      this.since = StringUtils.avoidNull(since); 
   }
   
   public String getCleanSince()
   {
      String since = getSince();
      //Perl5Util perlutil = new Perl5Util();
      //perlutil.replace(since, pattern);
      int i = since.indexOf("1.");
      if (i > -1)
         return since.substring(i);
      else
         return since;
   }
   
   
   public String getDeprecated() { return deprecated; }
   public void setDeprecated(String deprecated)
   { 
      this.deprecated = StringUtils.avoidNull(deprecated);
   }
   
   public String getSummaryDescription() {  return summaryDescription; }
   public void setSummaryDescription(String summaryDescription)
   {
      if (StringUtils.isBlank(summaryDescription))
         this.summaryDescription = "No description available.";
      else
         this.summaryDescription = summaryDescription;
   }
   
   public String getDescription() {  return description; }
   public void setDescription(String description)
   { 
      if (StringUtils.isBlank(description))
         this.description = "No description available.";
      else
         this.description = description;
   }
   
   public int getDocType() { return docType; }
   public void setDocType(int docType) { this.docType = docType; }

   public String getDocTypeName()
   {
      return DOCTYPES[getDocType()-1];
   }
   
   
   public List getReferences() { return references; }
   public void setReferences(List references) { this.references = references; }
   
   public void addReferences(SeeTag[] seetags)
   {
      Reference ref = null;
      for (int i=0; i<seetags.length; i++)
      {
         ref = new Reference(this, seetags[i]);
         references.add(ref);
      }
   }

   /**
    * @return the doc element type
    */
   public static int getDocType(Doc doc)
   {
      if(doc instanceof PackageDoc)
      {
         return PACKAGE_TYPE;
      }
      else if(doc instanceof ClassDoc)
      {
         return CLASS_TYPE;
      }
      else if(doc instanceof MemberDoc)
      {
         return MEMBER_TYPE;
      }
      else
      {
         // throw an exception
         return JDocUtil.UNKNOWN_TYPE;
      }
   }
   
   public boolean hasAnythingToShow()
   {
      return (!
         (StringUtils.isBlank(getSince()) && StringUtils.isBlank(getDeprecated()) &&
         getReferences().isEmpty())
      );
   }
   
   public void fetchRefs(Connection conn) throws SQLException
   {
      String sql =
       " select r.label, r.refdoc_name, r.refdoc_type, r.refdoc_id " +
       "  from REFERENCE r " +
       "  where r.sourcedoc_id = ? " +
       "  order by r.label ";

      Logger log = Logger.getInstance();
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      Reference ref;
      
      while (rset.next())
      {
         ref = new Reference(rset.getString(2));
         ref.setLabel(rset.getString(1));
         ref.setRefDocType(rset.getInt(3));
         ref.getRefDoc().setId(rset.getInt(4));
         //ref.setQualifiedSourceName(rset.getString(ref.getRefDocType()+8));
         references.add(ref);
         log.debug("added a reference to " + ref.getRefDocName());
      }
      
      rset.close();
      pstmt.close();
   }
   
   public void fetchRefsOld(Connection conn) throws SQLException
   {
      String sql = 
        " select r.label, r.refdoc_name, r.refdoc_type, r.sourcedoc_id, " +
        " p.id , c.id, m.id, e.id, " + 
        " p.name, c.qualifiedname, m.qualifiedname, e.fullyqualifiedname " +
        " from REFERENCE r, EXECMEMBER e, MEMBER m, CLASSTYPE c, PACKAGE p " + 
        " where r.sourcedoc_id=? " + 
        " and  r.refdoc_name=e.fullyqualifiedname  (+) " + 
        " and r.refdoc_name=m.qualifiedname (+) " + 
        " and r.refdoc_name=c.qualifiedname (+) " + 
        " and r.refdoc_name=p.name (+) " + 
        " order by r.label ";

      Logger log = Logger.getInstance();
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      Reference ref;
      
      while (rset.next())
      {
         ref = new Reference(rset.getString(2));
         ref.setLabel(rset.getString(1));
         ref.setRefDocType(rset.getInt(3));
         ref.getRefDoc().setId(rset.getInt(ref.getRefDocType()+4));
         ref.setQualifiedSourceName(rset.getString(ref.getRefDocType()+8));
         references.add(ref);
         log.debug("added a reference to " + ref.getRefDocName());
      }
      
      rset.close();
      pstmt.close();
   }
   
   
   public boolean isDeprecated()
   {
      return (getDeprecated().trim().length() > 0);
   }
   
   
}
