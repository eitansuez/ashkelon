package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;


public class XrefsPage extends Page
{
   public XrefsPage()
   {
      super();
   }
   
   public String init() throws SQLException
   {
      int clsId = Integer.parseInt(ServletUtils.getRequestParam(request, "cls_id"));
      String cmd = ServletUtils.getCommand(request);
      
      ClassType cls = ClassType.makeClassFor(conn, clsId, true);
      request.setAttribute("cls", cls);
      
      if (cmd.equals("cls.xref"))
      {
         return null;
      }

      String xrefType = StringUtils.split(cmd, ".")[2];
      if (xrefType.equals("field"))
      {
         List fields = getFields(clsId);
         request.setAttribute("field", fields);
      }
      else if (xrefType.equals("returnedby"))
      {
         List returnedBy = getReturnedBy(clsId);
         request.setAttribute("returnedby", returnedBy);
      }
      else if (xrefType.equals("passedto"))
      {
         List passedTo = getPassedTo(clsId);
         request.setAttribute("passedto", passedTo);
      }
      else if (xrefType.equals("thrownby"))
      {
         List thrownBy = getThrownBy(clsId);
         request.setAttribute("thrownby", thrownBy);
      }
      else if (xrefType.equals("implementedby"))
      {
         List implementedBy = getImplementedBy(clsId);
         request.setAttribute("implementedby", implementedBy);
      }
      else if (xrefType.equals("extendedby"))
      {
         List extendedBy = getExtendedBy(clsId);
         request.setAttribute("extendedby", extendedBy);
      }
      else if (xrefType.equals("subclasses"))
      {
         List subclasses = getSubclasses(cls);
         request.setAttribute("subclasses", subclasses);
      }
      else if (xrefType.equals("descendents"))
      {
         TreeNode descendents = cls.getDescendents(conn);
         request.setAttribute("descendents", descendents);
      }

      return null;
   }
   
   public List getFields(int clsId) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("xref_fields");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      ResultSet rset = pstmt.executeQuery();
      
      List fields = new ArrayList();
      
      FieldMember field;
      while(rset.next())
      {
         field = new FieldMember(rset.getString(1), rset.getString(3));
         field.setId(rset.getInt(2));
         field.setTypeDimension(rset.getInt(4));
         field.setStatic(rset.getBoolean(6));
         field.setFinal(rset.getBoolean(7));
         field.setAccessibility(rset.getInt(8));
         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(5));
         doc.setDeprecated(rset.getString(9));
         field.setDoc(doc);
         fields.add(field);
      }
      
      return fields;
   }

   public List getReturnedBy(int clsId) throws SQLException
   {
      /*
      String sql = 
         " select m.qualifiedname, m.id, m.isstatic, m.isfinal, m.accessibility, m.modifier, " + 
         "  ex.signature, meth.returntypename, meth.returntypedimension, meth.isabstract, " +
         " d.summarydescription, d.since, d.deprecated " + 
         " from method meth, execmember ex, member m, doc d " + 
         " where meth.returntypeid=? " + 
         "  and meth.id=ex.id  " + 
         "  and meth.id=m.id " + 
         "  and m.docid=d.id " + 
         "  and rownum < 50 " + 
         " order by m.qualifiedname ";
       */
      String sql = DBMgr.getInstance().getStatement("xref_returnedby");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      ResultSet rset = pstmt.executeQuery();
      
      List methods = new ArrayList();
      
      MethodMember method;
      while(rset.next())
      {
         method = new MethodMember(rset.getString(1), rset.getString(7));
         method.setId(rset.getInt(2));
         method.setStatic(rset.getBoolean(3));
         method.setFinal(rset.getBoolean(4));
         method.setAccessibility(rset.getInt(5));
         method.setModifiers(rset.getString(6));
         
         method.setReturnTypeName(rset.getString(8));
         method.setReturnTypeDimension(rset.getInt(9));
         method.setAbstract(rset.getBoolean(10));
         
         method.setDoc(new DocInfo(rset.getString(11), rset.getString(12), rset.getString(13)));
         
         methods.add(method);
      }
      
      return methods;
   }

   
   public List getPassedTo(int clsId) throws SQLException
   {
      /*
      String sql = 
         " select mem.qualifiedname, mem.id, mem.type, " + 
         "  ex.signature, d.summarydescription, " + 
         "  p.typedimension, p.name " + 
         " from parameter p, " + 
         "  execmember ex, member mem, doc d " + 
         " where p.typeid=? " + 
         "  and p.execmemberid=ex.id " + 
         "  and ex.id=mem.id " + 
         "  and mem.docid=d.id " + 
         "  and rownum < 50 " + 
         " order by mem.qualifiedname, mem.type ";
      */
      String sql = DBMgr.getInstance().getStatement("xref_passedto");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      ResultSet rset = pstmt.executeQuery();
      
      List execs = new ArrayList();
      
      ExecMember exec;
      int memberType;
      while(rset.next())
      {
         memberType = rset.getInt(3);
         if (memberType == Member.METHOD_MEMBER)
         {
            exec = new MethodMember(rset.getString(1), rset.getString(4));
            ((MethodMember) exec).setAbstract(rset.getBoolean(14));
         }
         else
         {
            exec = new ConstructorMember(rset.getString(1), rset.getString(4));
         }
         
         exec.setId(rset.getInt(2));
         exec.setModifiers(rset.getString(13));
         exec.setAccessibility(rset.getInt(12));
         exec.setFinal(rset.getBoolean(11));
         exec.setStatic(rset.getBoolean(10));

         exec.setDoc(new DocInfo(rset.getString(5), rset.getString(6), rset.getString(7)));
         execs.add(exec);
      }
      
      return execs;
   }
   

   public List getThrownBy(int clsId) throws SQLException
   {
      /*
      String sql = 
         " select mem.qualifiedname, mem.id, mem.type, " + 
         "  ex.signature, d.summarydescription, " + 
         "  te.description, te.name " + 
         " from thrownexception te, " +
         "  execmember ex, member mem, doc d " + 
         " where te.exceptionid=? " + 
         "  and te.throwerid=ex.id " + 
         "  and ex.id=mem.id " + 
         "  and mem.docid=d.id " + 
         "  and rownum < 50 " + 
         " order by mem.qualifiedname, mem.type ";
       */
      String sql = DBMgr.getInstance().getStatement("xref_thrownby");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      ResultSet rset = pstmt.executeQuery();
      
      List execs = new ArrayList();
      
      ExecMember exec;
      int memberType;
      while(rset.next())
      {
         memberType = rset.getInt(3);
         if (memberType == Member.METHOD_MEMBER)
            exec = new MethodMember(rset.getString(1), rset.getString(4));
         else
            exec = new ConstructorMember(rset.getString(1), rset.getString(4));
         
         exec.setId(rset.getInt(2));

         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(5));
         exec.setDoc(doc);
         
         execs.add(exec);
      }
      
      return execs;
   }


   public List getExtendedBy(int clsId) throws SQLException
   {
      return getImplementedBy(clsId, false);
   }

   public List getImplementedBy(int clsId) throws SQLException
   {
      return getImplementedBy(clsId, true);
   }
   
   public List getImplementedBy(int clsId, boolean classtarget) throws SQLException
   {
      /*
      String sql =
         " select " +
         "  c.qualifiedname, c.id,  " + 
         "  d.summarydescription, c.type " + 
         " from IMPL_INTERFACE ii, " +
         "  CLASSTYPE c, DOC d " + 
         " where ii.interfaceid=? " + 
         "  and ii.classid=c.id " + 
         "  and c.type "+maybe+"=? " + 
         "  and c.docid=d.id " + 
         "  and rownum < 50 " + 
         " order by c.qualifiedname ";
         
      if (DBMgr.getInstance().getDbtype().equals("mysql"))
      {
         sql = 
         " select " +
         "  c.qualifiedname, c.id,  " + 
         "  d.summarydescription, c.type " + 
         " from IMPL_INTERFACE ii, " +
         "  CLASSTYPE c, DOC d " + 
         " where ii.interfaceid=? " + 
         "  and ii.classid=c.id " + 
         "  and c.type "+maybe+"=? " + 
         "  and c.docid=d.id " + 
         " order by c.qualifiedname " +
         " limit 50";
      }
      */
      
      String sql = "";
      if( classtarget ) {
         sql = DBMgr.getInstance().getStatement("xref_implementedbyclasstarget");
      } else {
         sql = DBMgr.getInstance().getStatement("xref_implementedby");
      }
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      pstmt.setInt(2, ClassType.INTERFACE);
      ResultSet rset = pstmt.executeQuery();
      
      List classes = new ArrayList();
      
      ClassType c;
      while(rset.next())
      {
         c = new ClassType(rset.getString(1));
         c.setId(rset.getInt(2));
         c.setClassType(rset.getInt(4));

         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(3));
         c.setDoc(doc);
         
         classes.add(c);
      }
      
      return classes;
   }
   

   public List getSubclasses(ClassType refc) throws SQLException
   {
      /*
      String sql =
         " select " +
         "  c.qualifiedname, c.id,  " + 
         "  d.summarydescription, c.type " + 
         " from classtype c, doc d " + 
         " where c.superclassname=? " + 
         "  and c.type != ? " + 
         "  and c.docid=d.id " + 
         "  and rownum < 50 " + 
         " order by c.qualifiedname ";
       */
      String sql = DBMgr.getInstance().getStatement("xref_subclass");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, refc.getQualifiedName());
      pstmt.setInt(2, ClassType.INTERFACE);
      ResultSet rset = pstmt.executeQuery();
      
      List classes = new ArrayList();
      
      ClassType c;
      while(rset.next())
      {
         c = new ClassType(rset.getString(1));
         c.setId(rset.getInt(2));
         c.setClassType(rset.getInt(4));

         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(3));
         c.setDoc(doc);
         
         classes.add(c);
      }
      
      return classes;
   }
   

}
