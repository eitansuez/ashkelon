package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import com.sun.javadoc.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.SeeTag
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class Reference implements Serializable
{
   private String label;
   private DocInfo refDoc;
   private String refDocName;
   private int refDocType;
   private DocInfo sourceDoc;
   private String qualifiedSourceName;
   
   
   private static String TABLENAME = "REFERENCE";
   
   public Reference(String refDocName)
   {
      setRefDocName(refDocName);
      setLabel("");
      setRefDoc(new DocInfo());
   }
   
   public Reference(DocInfo sourceDoc, SeeTag seetag)
   {
      Logger log = Logger.getInstance();
      
      setSourceDoc(sourceDoc);
      setLabel(seetag.label());
      
      String refDocName = "";
      int refDocType = 0;
      
      if (seetag.referencedMember() == null)
      {
         if (seetag.referencedClass() == null)
         {
            if (seetag.referencedPackage() != null)
            {
               refDocName = seetag.referencedPackage().name();
               refDocType = DocInfo.PACKAGE_TYPE;
            }
         } else
         {
            refDocName = seetag.referencedClass().qualifiedName();
            refDocType = DocInfo.CLASS_TYPE;
         }
      } else
      {
         refDocName = seetag.referencedMember().qualifiedName();
         refDocType = DocInfo.MEMBER_TYPE;
         if (seetag.referencedMember() instanceof ExecutableMemberDoc)
         {
            refDocType = DocInfo.EXECMEMBER_TYPE;
            refDocName += ((ExecutableMemberDoc) seetag.referencedMember()).signature();
         }
      }
      
      if (StringUtils.isBlank(refDocName))
      {
         refDocName = seetag.referencedMemberName();
         if (StringUtils.isBlank(refDocName))
         {
            refDocName = seetag.referencedClassName();
            if (!StringUtils.isBlank(refDocName))
            {
               log.debug("class ref doc name: "+refDocName);
               refDocType = DocInfo.CLASS_TYPE;
            }
         }
         else
         {
            log.debug("member ref doc name: "+refDocName);
            refDocType = DocInfo.MEMBER_TYPE;
         }
      }
      
      if(StringUtils.isBlank(refDocName))
      {
         refDocName = "";
         log.debug("still have blank refdocnames!");
      }
      
      setRefDocName(refDocName);
      setRefDocType(refDocType);
   }
   
   public void store(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(5);

      fieldInfo.put("LABEL", StringUtils.truncate(getLabel(), 60));
      fieldInfo.put("SOURCEDOC_ID", new Integer(getSourceDoc().getId(conn)));
      fieldInfo.put("REFDOC_NAME", StringUtils.truncate(getRefDocName(), 450));
      fieldInfo.put("REFDOC_TYPE", new Integer(getRefDocType()));
      DBUtils.insert(conn, TABLENAME, fieldInfo);
   }
   
   // accessor methods
   public String getLabel() { return label; }
   public void setLabel(String label) { this.label = label; }
   
   public DocInfo getRefDoc() { return refDoc; }
   public void setRefDoc(DocInfo refDoc) { this.refDoc = refDoc; }
   
   public String getRefDocName() { return refDocName; }
   public void setRefDocName(String refDocName) { this.refDocName = refDocName; }
   
   public DocInfo getSourceDoc() { return sourceDoc; }
   public void setSourceDoc(DocInfo sourceDoc) { this.sourceDoc = sourceDoc; }
   
   public int getRefDocType() { return refDocType; }
   public void setRefDocType(int refDocType) { this.refDocType = refDocType; }

   public String toString()
   {
      return getLabel() + "("+getRefDocName()+")";
   }

   public String getQualifiedSourceName()
   {
      return qualifiedSourceName;
   }
   public void setQualifiedSourceName(String qsn)
   {
      this.qualifiedSourceName = qsn;
   }
   
}
