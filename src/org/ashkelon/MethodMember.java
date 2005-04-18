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
