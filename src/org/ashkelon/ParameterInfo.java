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
