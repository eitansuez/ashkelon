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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.ashkelon.db.DBUtils;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.SeeTag;

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
