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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.ashkelon.db.DBUtils;

import com.sun.javadoc.ConstructorDoc;

/**
 * Part of Persistable javadoc object model known as Ashkelon
 * Analog of com.sun.javadoc.ConstructorDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class ConstructorMember extends ExecMember
{
   private static String TABLENAME = "CONSTRUCTOR";
   
   public ConstructorMember(String qualifiedName, String signature)
   {
      super(qualifiedName + qualifiedName.substring(qualifiedName.lastIndexOf(".")), signature, Member.CONSTRUCTOR_MEMBER);
   }
   
   public ConstructorMember(ConstructorDoc constructordoc, ClassType containingClass)
   {
      super(constructordoc, containingClass);
   }

   public ConstructorMember(Member member, String signature)
   {
      super(member, signature);
   }
   
   public void store(Connection conn) throws SQLException
   {
      super.store(conn);
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("ID", new Integer(getId(conn)));
      DBUtils.insert(conn, TABLENAME, fieldInfo);
   }
   
   public static void delete(Connection conn, int memberid, int docid) throws SQLException
   {
      Map constraint = new HashMap();
      constraint.put("ID", new Integer(memberid));
      DBUtils.delete(conn, TABLENAME, constraint);
      
      ExecMember.delete(conn, memberid, docid);
   }
   
   public static String fixQualifiedName(String qualifiedName)
   {
      int idx = qualifiedName.lastIndexOf(".");
      String lastPart = qualifiedName.substring(idx);
      return qualifiedName + lastPart;
   }
}
