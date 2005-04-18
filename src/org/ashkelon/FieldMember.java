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

import com.sun.javadoc.FieldDoc;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.FieldDoc
 *
 * @author Eitan Suez
 */
public class FieldMember extends Member
{
    private int typeDimension;
    private ClassType type;
    private String typeName;

    private static String TABLENAME = "FIELD";

    public FieldMember(String qualifiedName, String qualifiedTypeName)
    {
       super(qualifiedName, Member.FIELD_MEMBER);
       setTypeName(qualifiedTypeName);
    }

   public FieldMember(Member member, String qualifiedTypeName)
   {
      super(member);
      setTypeName(qualifiedTypeName);
   }
    
    public FieldMember(FieldDoc fielddoc, ClassType containingClass)
    {
      super(fielddoc, containingClass);
      setTypeName(fielddoc.type().qualifiedTypeName());
      setTypeDimension(JDocUtil.getDimension(fielddoc.type()));
    }
    
    public void store(Connection conn) throws SQLException
    {
       super.store(conn);
       Map fieldInfo = new HashMap(10);
       fieldInfo.put("ID", new Integer(getId(conn)));
       fieldInfo.put("TYPEDIMENSION", new Integer(getTypeDimension()));
       fieldInfo.put("TYPENAME", StringUtils.truncate(getTypeName(), 150));
       DBUtils.insert(conn, TABLENAME, fieldInfo);
    }
    
   public static void delete(Connection conn, int memberid, int docid) throws SQLException
   {
      Map constraint = new HashMap();
      constraint.put("ID", new Integer(memberid));
      DBUtils.delete(conn, TABLENAME, constraint);

      Member.delete(conn, memberid, docid);
   }

    // accessor methods:
    public int getTypeDimension(){ return typeDimension; }
    public void setTypeDimension(int typeDimension){ this.typeDimension = typeDimension; }

    public ClassType getType(){ return type; }
    public void setType(ClassType type){ this.type = type; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
}
