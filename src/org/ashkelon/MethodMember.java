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
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.MethodDoc;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.MethodDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class MethodMember extends ExecMember
{
   private boolean isAbstract;
   private ClassType returnType;
   private String returnTypeName;
   private int returnTypeDimension;
   private String returnDescription;
   
   private static String TABLENAME = "METHOD";

   public MethodMember(String qualifiedName, String signature)
   {
      super(qualifiedName, signature, Member.METHOD_MEMBER);
      setReturnTypeName("");
      setReturnDescription("");
   }
   
   public MethodMember(Member member, String signature)
   {
      super(member, signature);
   }
   
   public MethodMember(MethodDoc methoddoc, ClassType containingClass)
   {
      super(methoddoc, containingClass);
      setAbstract(methoddoc.isAbstract());
      setReturnTypeName(methoddoc.returnType().qualifiedTypeName());
      setReturnTypeDimension(JDocUtil.getDimension(methoddoc.returnType()));
      setReturnDescription(JDocUtil.resolveDescription(this.getDoc(), methoddoc.tags("@return")));
   }
    
    public void store(Connection conn) throws SQLException
    {
       super.store(conn);
       Map fieldInfo = new HashMap(5);
       fieldInfo.put("ID", new Integer(getId(conn)));
       
       //fieldInfo.put("ISABSTRACT", new Boolean(isAbstract()));
       int boolvalue = isAbstract() ? 1 : 0;
       fieldInfo.put("ISABSTRACT", new Integer(boolvalue));
       
       fieldInfo.put("RETURNTYPENAME", StringUtils.truncate(getReturnTypeName(), 150));
       fieldInfo.put("RETURNTYPEDIMENSION", new Integer(getReturnTypeDimension()));
       fieldInfo.put("RETURNDESCRIPTION", getReturnDescription());
       
       try
       {
          DBUtils.insert(conn, TABLENAME, fieldInfo);
       }
       catch (SQLException ex)
       {
          fieldInfo.put("RETURNDESCRIPTION", StringUtils.truncate(getReturnDescription(), 350));
          DBUtils.insert(conn, TABLENAME, fieldInfo);
       }
    }
    
    public static void delete(Connection conn, int memberid, int docid) throws SQLException
    {
       Map constraint = new HashMap();
       constraint.put("ID", new Integer(memberid));
       DBUtils.delete(conn, TABLENAME, constraint);
       
       ExecMember.delete(conn, memberid, docid);
    }
    
    // accessor methods:
    public boolean isAbstract(){ return isAbstract; }
    public void setAbstract(boolean isAbstract){ this.isAbstract = isAbstract; }

    public ClassType getReturnType(){ return returnType; }
    public void setReturnType(ClassType returnType){ this.returnType = returnType; }

    public String getReturnTypeName(){ return returnTypeName; }
    public void setReturnTypeName(String returnTypeName)
    { 
       this.returnTypeName = StringUtils.avoidNull(returnTypeName);
    }

    public int getReturnTypeDimension(){ return returnTypeDimension; }
    public void setReturnTypeDimension(int returnTypeDimension){ this.returnTypeDimension = returnTypeDimension; }

    public String getReturnDescription(){ return returnDescription; }
    public void setReturnDescription(String returnDescription) {  this.returnDescription = returnDescription; }
    
}
