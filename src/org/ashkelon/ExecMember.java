package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ashkelon.db.DBUtils;
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ThrowsTag;

/**
 * Part of Persistable javadoc object model known as ashkelon
 * Analog of com.sun.javadoc.ExecutableMemberDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class ExecMember extends Member implements Comparator
{
   private boolean isSynchronized;
   private boolean isNative;
   private List thrownExceptions;
   private String fullyQualifiedName;
   private String signature;
   private List parameters;
   
   private static String TABLENAME = "EXECMEMBER";

   
   public ExecMember(String qualifiedName, String signature, int memberType)
   {
      super(qualifiedName, memberType);
      setSignature(signature);
      setFullyQualifiedName(qualifiedName+signature);
      setThrownExceptions(new ArrayList());
      setParameters(new ArrayList());
   }
   
   public ExecMember(Member member, String signature)
   {
      super(member);
      setThrownExceptions(new ArrayList());
      setParameters(new ArrayList());
      setSignature(signature);
   }
   
   public ExecMember(ExecutableMemberDoc exmemdoc, ClassType containingClass)
   {
      super(exmemdoc, containingClass);
      parameters = new ArrayList();
      thrownExceptions = new ArrayList();
      setNative(exmemdoc.isNative());
      setSynchronized(exmemdoc.isSynchronized());
      setSignature(exmemdoc.signature());
      setFullyQualifiedName(getQualifiedName() + getSignature());
      
      addParameters(exmemdoc.parameters(), exmemdoc.paramTags());
      addThrownExceptions(exmemdoc.thrownExceptions(), exmemdoc.throwsTags());
   }
   
   public void store(Connection conn) throws SQLException
   {
      super.store(conn);
      Map fieldInfo = new HashMap(10);
      fieldInfo.put("ID", new Integer(getId(conn)));
      
      //fieldInfo.put("ISSYNCHRONIZED", new Boolean(isSynchronized()));
      int boolvalue = isSynchronized() ? 1 : 0;
      fieldInfo.put("ISSYNCHRONIZED", new Integer(boolvalue));
      
      //fieldInfo.put("ISNATIVE", new Boolean(isNative()));
      boolvalue = isNative() ? 1 : 0;
      fieldInfo.put("ISNATIVE", new Integer(boolvalue));
      
      fieldInfo.put("SIGNATURE", StringUtils.truncate(getSignature(), 300));
      fieldInfo.put("FULLYQUALIFIEDNAME", StringUtils.truncate(getFullyQualifiedName(), 450));
      DBUtils.insert(conn, TABLENAME, fieldInfo);
      
      for (int i=0; i<parameters.size(); i++)
      {
         ((ParameterInfo) parameters.get(i)).store(conn);
      }
      
      for (int i=0; i<thrownExceptions.size(); i++)
      {
         ((ThrownException) thrownExceptions.get(i)).store(conn);
      }
   }
   
   public static void delete(Connection conn, int memberid, int docid) throws SQLException
   {
      deleteThrownExceptions(conn, memberid);
      deleteParameters(conn, memberid);
      
      Map constraint = new HashMap();
      constraint.put("ID", new Integer(memberid));
      DBUtils.delete(conn, TABLENAME, constraint);

      Member.delete(conn, memberid, docid);
   }
   
   public static void deleteThrownExceptions(Connection conn, int memberid) throws SQLException
   {
      Map constraint = new HashMap();
      constraint.put("throwerid", new Integer(memberid));
      DBUtils.delete(conn, "thrownexception", constraint);
   }
    
   public static void deleteParameters(Connection conn, int memberid) throws SQLException
   {
      Map constraint = new HashMap();
      constraint.put("execmemberid", new Integer(memberid));
      DBUtils.delete(conn, "parameter", constraint);
   }

   // accessor methods:
   public boolean isNative(){ return isNative; }
   public void setNative(boolean isNative){ this.isNative = isNative; }

   public boolean isSynchronized(){ return isSynchronized; }
   public void setSynchronized(boolean isSynchronized){ this.isSynchronized = isSynchronized; }

   public String getFullyQualifiedName(){ return fullyQualifiedName; }
   public void setFullyQualifiedName(String fullyQualifiedName){ this.fullyQualifiedName = fullyQualifiedName; }
   
   public String getSignature(){ return signature; }
   public void setSignature(String signature){ this.signature = signature; }
   
   public List getParameters() { return parameters; }
   public void setParameters(List parameters) { this.parameters = parameters; }
   
   public void addParameters(Parameter[] parameters, ParamTag[] tags)
   {
      Map paramMap = JDocUtil.makeParamMap(getDoc(), tags);
      String paramdescription;
      ParameterInfo paramInfo = null;
      
      for (int i=0; i<parameters.length; i++)
      {
         if (paramMap.get(parameters[i].name()) == null)
         {
            paramdescription = "";
         } else
         {
            paramdescription = (String) paramMap.get(parameters[i].name());
         }
         paramInfo = new ParameterInfo(parameters[i], i, paramdescription, this);
         addParameter(paramInfo);
      }
   }
   
   public void addParameter(ParameterInfo param)
   {
      this.parameters.add(param);
   }

   
   public List getThrownExceptions() { return thrownExceptions; }
   public void setThrownExceptions(List thrownExceptions) { this.thrownExceptions = thrownExceptions; }
   
   public void addThrownExceptions(ClassDoc[] exceptions, ThrowsTag[] throwsTags)
   {
      String exceptionComment;
      ThrownException thrownException;
      for (int i=0; i<exceptions.length; i++)
      {
         exceptionComment = "";
         for (int j=0; j<throwsTags.length; j++)
         {
            if (exceptions[i].qualifiedName().endsWith(throwsTags[j].exceptionName()))
            {
               //exceptionComment = throwsTags[j].exceptionComment();
               exceptionComment = JDocUtil.resolveDescription(getDoc(), throwsTags[j].inlineTags());
               break;
            }
         }
         thrownException = new ThrownException(exceptions[i].qualifiedName(), exceptionComment, this);
         addThrownException(thrownException);
      }
   }
   
   public void addThrownException(ThrownException thrownException)
   {
      thrownExceptions.add(thrownException);
   }
   
    
    public int compare(Object first, Object second)
    {
       ExecMember m1 = (ExecMember) first;
       ExecMember m2 = (ExecMember) second;
       if (m1.getQualifiedName().equals(m2.getQualifiedName()))
       {
          return (m1.getParameters().size() - m2.getParameters().size());
       }
       else
       {
          return 1;
       }
    }


   public void fetchParameters(Connection conn) throws SQLException
   {
      
      String sql =
         "select p.name, p.description, p.typeid, p.typedimension, " +
         " p.typename, p.listedorder " +
         "from PARAMETER p " +
         " where p.execmemberid=? " +
         " order by p.listedorder";

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      ParameterInfo param;
      while (rset.next())
      {
         param = new ParameterInfo(rset.getString(5));
         param.setName(rset.getString(1));
         param.setDescription(rset.getString(2));
         param.setListedOrder(rset.getInt(6));
         param.setTypeDimension(rset.getInt(4));
         param.setContainingMember(this);
         int typeid = rset.getInt(3);
         if (typeid > 0)  // i.e. not a primitive and internally referenceable
         {
            param.setType(new ClassType(param.getTypeName()));
            param.getType().setId(typeid);
         }
         addParameter(param);
      }
      
      rset.close();
      pstmt.close();
   }
   
   public void fetchExceptions(Connection conn) throws SQLException
   {
      String sql = 
         "select ex.exceptionid, ex.name, ex.description " +
         " from THROWNEXCEPTION ex " +
         " where ex.throwerid=? " +
         " order by ex.name";
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      ThrownException ex;
      while (rset.next())
      {
         ex = new ThrownException(rset.getString(2));
         ex.setDescription(rset.getString(3));
         int exid = rset.getInt(1);
         if (exid > 0)  // i.e. not a primitive and internally referenceable
         {
            ex.setException(new ClassType(ex.getName()));
            ex.getException().setId(exid);
         }
         ex.setThrower(this);
         addThrownException(ex);
      }
      
      rset.close();
      pstmt.close();
   }
}
