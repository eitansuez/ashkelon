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
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.Parameter;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.Parameter & ParamTag
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class ParameterInfo implements Serializable
{
    private String name;
    private String description;
    private int listedOrder;
    private ClassType type;
    private String typeName;
    private int typeDimension;
    private ExecMember containingMember;
    
    private static String TABLENAME = "PARAMETER";
    
    public ParameterInfo(String typeName)
    {
       setTypeName(typeName);
       setName("");
       setDescription("");
    }
   
    public ParameterInfo(Parameter parameter, int listedOrder, String description, ExecMember containingMember)
    {
      setName(parameter.name());
      setDescription(description);
      setListedOrder(listedOrder);
      setTypeName(parameter.type().qualifiedTypeName());
      setTypeDimension(JDocUtil.getDimension(parameter.type()));
      setContainingMember(containingMember);
    }

   public void store(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(10);
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 50));
      fieldInfo.put("DESCRIPTION", getDescription());
      fieldInfo.put("LISTEDORDER", new Integer(getListedOrder()));
      fieldInfo.put("TYPEDIMENSION", new Integer(getTypeDimension()));
      fieldInfo.put("TYPENAME", StringUtils.truncate(getTypeName(), 150));
      fieldInfo.put("EXECMEMBERID", new Integer(getContainingMember().getId(conn)));
      
      try
      {
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
      catch (SQLException ex)
      {
         fieldInfo.put("DESCRIPTION", StringUtils.truncate(getDescription(), 350));
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
   }
   
    // accessors
    public String getName(){ return name; }
    public void setName(String name)
    {
       this.name = StringUtils.avoidNull(name);
    }

    public String getDescription(){ return description; }
    public void setDescription(String description)
    {
       this.description = StringUtils.avoidNull(description);
    }

    public ClassType getType(){ return type; }
    public void setType(ClassType type){ this.type = type; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName)
    {
       this.typeName = StringUtils.avoidNull(typeName);
    }

    public int getTypeDimension(){ return typeDimension; }
    public void setTypeDimension(int typeDimension){ this.typeDimension = typeDimension; }

    public int getListedOrder(){ return listedOrder; }
    public void setListedOrder(int listedOrder){ this.listedOrder = listedOrder; }

    public ExecMember getContainingMember(){ return containingMember; }
    public void setContainingMember(ExecMember containingMember){ this.containingMember = containingMember; }

}
