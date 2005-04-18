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
