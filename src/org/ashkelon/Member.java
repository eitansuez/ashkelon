package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ashkelon.db.DBUtils;
import org.ashkelon.db.PKManager;
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

import com.sun.javadoc.MemberDoc;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.MemberDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class Member implements JDoc, Serializable
{
   private ClassType containingClass;
   private String name;
   private String qualifiedName;
   private DocInfo doc;
   private String containingClassName;

   private boolean isStatic;
   private boolean isFinal;
   private int accessibility;
   private String modifiers;
   
   private int memberType;

   private int id;
   private boolean idSet = false;
   
   private static String SEQUENCE = "MEMBER_SEQ";
   private static String TABLENAME = "MEMBER";

   /** constants representing the three member types */
   public static final int FIELD_MEMBER = 1;
   /** constants representing the three member types */
   public static final int CONSTRUCTOR_MEMBER = 2;
   /** constants representing the three member types */
   public static final int METHOD_MEMBER = 3;

	public static final String[] MEMBERTYPES = {"field", "constructor", "method"};
   
   
   public Member(String qualifiedName, int memberType)
   {
      setQualifiedName(qualifiedName);
      setMemberType(memberType);
      setName(JDocUtil.unqualify(qualifiedName));
      setContainingClassName(qualifiedName.substring(0, qualifiedName.lastIndexOf(".")));
      setContainingClass(new ClassType(getContainingClassName()));
      setDoc(new DocInfo());
      setModifiers("");
   }
   
   public Member(Member member)
   {
      this(member.getQualifiedName(), member.getMemberType());
      setId(member.getId());
      setStatic(member.isStatic());
      setFinal(member.isFinal());
      setAccessibility(member.getAccessibility());
      setModifiers(member.getModifiers());
      setDoc(member.getDoc());
      setContainingClass(member.getContainingClass());
   }
   
   public Member(MemberDoc memberdoc, ClassType containingClass)
   {
      setDoc(new DocInfo(memberdoc));
      setName(memberdoc.name());
      setQualifiedName(memberdoc.qualifiedName());
      setMemberType(getMemberType(memberdoc));

      setStatic(memberdoc.isStatic());
      setFinal(memberdoc.isFinal());
      setAccessibility(JDocUtil.getAccessibility(memberdoc));
      setModifiers(memberdoc.modifiers());

      setContainingClass(containingClass);
   }
   
   public void store(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(10);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("CLASSID", new Integer(getContainingClass().getId(conn)));
      fieldInfo.put("DOCID", new Integer(getDoc().getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 100));
      fieldInfo.put("QUALIFIEDNAME", StringUtils.truncate(getQualifiedName(), 150));
      
      //fieldInfo.put("ISSTATIC", new Boolean(isStatic()));
      int boolvalue = isStatic() ? 1 : 0;
      fieldInfo.put("ISSTATIC", new Integer(boolvalue));
      
      //fieldInfo.put("ISFINAL", new Boolean(isFinal()));
      boolvalue = isFinal() ? 1 : 0;
      fieldInfo.put("ISFINAL", new Integer(boolvalue));
      
      fieldInfo.put("ACCESSIBILITY", new Integer(getAccessibility()));
      fieldInfo.put("MODIFIER", StringUtils.truncate(getModifiers(), 51));
      fieldInfo.put("TYPE", new Integer(getMemberType()));
      DBUtils.insert(conn, TABLENAME, fieldInfo);

      getDoc().store(conn);
   }
   
   public int getId(Connection conn) throws SQLException
   {
      if (!idSet)
      {
         //id = DBUtils.getNextVal(conn, SEQUENCE);
         id = PKManager.getInstance().nextVal(SEQUENCE);
         idSet = true;
      }
      return id;
   }
   
   public int getId()
   {
      return id;
   }
   
   public void setId(int id)
   {
      this.id = id;
      idSet = true;
   }
   
   public static void delete(Connection conn, int memberid, int docid) throws SQLException
   {
      DocInfo.delete(conn, docid);

      // sever references to reference
      String sql = "update REFERENCE set refdoc_id=null, refdoc_type=null" +
                   " where refdoc_id=?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, memberid);
      pstmt.executeUpdate();
      pstmt.close();
      
      Map constraint = new HashMap();
      constraint.put("ID", new Integer(memberid));
      DBUtils.delete(conn, TABLENAME, constraint);
   }
   
   // accessor methods
   public ClassType getContainingClass() { return containingClass; }
   public void setContainingClass(ClassType containingClass) { this.containingClass = containingClass; }
   
   public DocInfo getDoc() { return doc; }
   public void setDoc(DocInfo doc) { this.doc = doc; }
   
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   
   public String getQualifiedName() { return qualifiedName; }
   public void setQualifiedName(String qualifiedName) { this.qualifiedName = qualifiedName; }

   public boolean isStatic() { return isStatic; }
   public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
   
   public boolean isFinal() { return isFinal; }
   public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
   
   public int getAccessibility() { return accessibility; }
   public void setAccessibility(int accessibility) { this.accessibility = accessibility; }
   
   public String getModifiers() { return modifiers; }
   public void setModifiers(String modifiers) { this.modifiers = modifiers; }

   public int getMemberType() { return memberType; }
   public void setMemberType(int memberType) { this.memberType = memberType; }
   
   public String getMemberTypeName()
   {
      int memberType = getMemberType();
      if (memberType > MEMBERTYPES.length || memberType <= 0)
      {
         return "unknown";
      }
      return MEMBERTYPES[memberType-1];
   }
   
   public String getContainingClassName() { return containingClassName; }
   public void setContainingClassName(String containingClassName)
   { 
      this.containingClassName = containingClassName;
   }
   
   
   /**
    * @return type for a given memberdoc (field, method, constructor)
    */
   public static int getMemberType(MemberDoc memberdoc)
   {
      if (memberdoc.isMethod())
      {
         return METHOD_MEMBER;
      }
      else if (memberdoc.isConstructor())
      {
         return CONSTRUCTOR_MEMBER;
      }
      else if (memberdoc.isField())
      {
         return FIELD_MEMBER;
      }
      else
      {
         return JDocUtil.UNKNOWN_TYPE;
      }
   }
   
   public JPackage getPackage()
   {
      if (getContainingClass() ==  null) { return null; }
      return getContainingClass().getPackage();
   }
   
   
   public static List makeMembersFor(Connection conn, int memberId) throws SQLException
   {
      Logger log = Logger.getInstance();
      
      String sql = "select qualifiedname, type from MEMBER where id=?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, memberId);
      ResultSet rset = pstmt.executeQuery();
      
      int memberType = 0;
      String qualifiedname = "";
      
      while (rset.next())
      {
         qualifiedname = rset.getString(1);
         memberType = rset.getInt(2);
      }
      
      rset.close();
      
      if (memberType == 0) return null;

      
      int FIELDS = 0; int TABLES = 1; int CONDITIONS = 2;
      
      String[][] memberInfo = 
         {
            {" , f.typename, f.typedimension, f.typeid ", " , FIELD f ", " and m.id=f.id "},
            {" , ex.signature , ex.fullyqualifiedname ", " , EXECMEMBER ex ", " and m.id=ex.id "},
            {" , ex.signature , meth.isabstract, meth.returntypename, meth.returntypedimension, meth.returntypeid, meth.returndescription , ex.fullyqualifiedname ", 
             " , EXECMEMBER ex  , METHOD meth ", 
             " and m.id=ex.id  and m.id=meth.id "}
         };
         
      sql = 
            " select m.id, m.qualifiedname, m.type, " +
            "       m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
            "       m.name, m.classid, " + 
            "       d.summarydescription, d.since, d.deprecated, d.description, d.id, " +
            "       c.packageid, c.type, c.qualifiedname " +
            memberInfo[memberType-1][FIELDS] + 
            "    from MEMBER m, DOC d, CLASSTYPE c  " +
            memberInfo[memberType-1][TABLES] + 
            "    where m.qualifiedname=? and m.docid=d.id and m.classid=c.id " + 
            memberInfo[memberType-1][CONDITIONS] + 
            "    order by m.type, m.name " ;
      
      log.debug("member sql statement:\n\t" + sql);
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, qualifiedname);
      rset = pstmt.executeQuery();

      List members = new ArrayList();

      Member member = null;
      DocInfo doc;
      ClassType container;
      
      while (rset.next())
      {
         member = new Member(rset.getString(2), rset.getInt(3));
         member.setId(rset.getInt(1));
         member.setStatic(rset.getBoolean(4));
         member.setFinal(rset.getBoolean(5));
         member.setAccessibility(rset.getInt(6));
         member.setModifiers(rset.getString(7));
      
         doc = new DocInfo(rset.getString(10), rset.getString(11), rset.getString(12));
         doc.setDescription(rset.getString(13));
         doc.setId(rset.getInt(14));
         doc.fetchRefs(conn);
         
         member.setDoc(doc);
         
         container = new ClassType(rset.getString(17));
         container.setId(rset.getInt(9));
         container.getPackage().setId(rset.getInt(15));
         container.setClassType(rset.getInt(16));
         
         member.setContainingClass(container);

         switch (memberType)
         {
            case Member.FIELD_MEMBER:
            {
               FieldMember fm = new FieldMember(member, rset.getString(18));
               fm.setTypeDimension(rset.getInt(19));
               int typeid = rset.getInt(20);
               if (typeid > 0)
               {
                  fm.setType(new ClassType(fm.getTypeName()));
                  fm.getType().setId(typeid);
               }
               members.add(fm);
               break;
            }  // end case
            case Member.CONSTRUCTOR_MEMBER:
            {
               ConstructorMember cm = new ConstructorMember(member, rset.getString(18));
               cm.setFullyQualifiedName(rset.getString(19));
               cm.fetchParameters(conn);
               cm.fetchExceptions(conn);
               members.add(cm);
               break;
            }  // end case
            case Member.METHOD_MEMBER:
            {
               
               MethodMember mm = new MethodMember(member, rset.getString(18));
               mm.setAbstract(rset.getBoolean(19));
               mm.setReturnTypeName(rset.getString(20));
               mm.setReturnTypeDimension(rset.getInt(21));
               mm.setReturnDescription(rset.getString(23));
               mm.setFullyQualifiedName(rset.getString(24));
               int typeid = rset.getInt(22);
               if (typeid > 0)
               {
                  mm.setReturnType(new ClassType(mm.getReturnTypeName()));
                  mm.getReturnType().setId(typeid);
               }
               mm.fetchParameters(conn);
               mm.fetchExceptions(conn);
               members.add(mm);
               break;
            }  // end case
         }  // end switch
         
      } // end while
         
      rset.close();
      pstmt.close();
      
      return members;
      
   }  // end makeMembersFor

   
   public String getSummaryDescription() { return getDoc().getSummaryDescription(); }
   public String getDescription() { return getDoc().getDescription(); }
   public String getSince() { return getDoc().getSince(); }
   public String getDeprecatedDescr() { return getDoc().getDeprecated(); }
   public boolean isDeprecated() { return getDoc().isDeprecated(); }
   public List getReferences() { return getDoc().getReferences(); }
   
   public String getStyle() { return getMemberTypeName(); }

}
