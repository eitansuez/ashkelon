package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;


/**
 * @author Eitan Suez
 */
public class XrefsPage extends Page
{
   public XrefsPage()
   {
      super();
   }
   
   public String handleRequest() throws SQLException, ClassNotFoundException
   {
      int clsId = Integer.parseInt(ServletUtils.getRequestParam(request, "cls_id"));
      String cmd = ServletUtils.getCommand(request);
      
      ClassType cls = ClassType.makeClassFor(conn, clsId, true);
      request.setAttribute("cls", cls);
      
      if (cmd.equals("cls.xref"))
         return null;

      String xrefType = StringUtils.split(cmd, ".")[2];
      if (xrefType.equals("field"))
         doFields(clsId);
      else if (xrefType.equals("returnedby"))
         doReturnedBy(clsId);
      else if (xrefType.equals("passedto"))
         doPassedTo(clsId);
      else if (xrefType.equals("thrownby"))
         doThrownBy(clsId);
      else if (xrefType.equals("implementedby"))
         doImplementedBy(clsId);
      else if (xrefType.equals("extendedby"))
         doExtendedBy(clsId);
      else if (xrefType.equals("subclasses"))
         doSubclasses(cls);
      else if (xrefType.equals("descendents"))
      {
         TreeNode descendents = cls.getDescendents(conn);
         request.setAttribute("descendents", descendents);
      }

      return null;
   }
   
   public void doFields(int clsId) throws SQLException
   {
      ResultSet rset = executeQuery("xref_fields", clsId);
      int position = position(rset);

      List fields = new ArrayList();
      FieldMember field;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
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
      
      rset.close();
      request.setAttribute("field", fields);
   }
   
   private ResultSet executeQuery(String stmtName, int clsId)
      throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement(stmtName);

      PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
      pstmt.setInt(1, clsId);
      pstmt.setFetchSize(FETCH_SIZE);
      return pstmt.executeQuery();
   }
   
   public void doReturnedBy(int clsId) throws SQLException
   {
      ResultSet rset = executeQuery("xref_returnedby", clsId);
      int position = position(rset);

      List methods = new ArrayList();
      MethodMember method;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
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
      
      rset.close();
      request.setAttribute("returnedby", methods);
   }

   
   public void doPassedTo(int clsId) throws SQLException
   {
      ResultSet rset = executeQuery("xref_passedto", clsId);
      int position = position(rset);

      List execs = new ArrayList();
      ExecMember exec;
      int memberType;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
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
      
      rset.close();
      request.setAttribute("passedto", execs);
   }
   

   public void doThrownBy(int clsId) throws SQLException
   {
      ResultSet rset = executeQuery("xref_thrownby", clsId);
      int position = position(rset);

      List execs = new ArrayList();
      ExecMember exec;
      int memberType;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
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
      
      rset.close();
      request.setAttribute("thrownby", execs);
   }


   public void doExtendedBy(int clsId) throws SQLException
   {
      doImplementedBy(clsId, "extendedby");
   }

   public void doImplementedBy(int clsId) throws SQLException
   {
      doImplementedBy(clsId, "implementedby");
   }
   
   public void doImplementedBy(int clsId, String key) throws SQLException
   {
      ResultSet rset = null;
      rset = executeQuery("xref_"+key, clsId);
      
      int position = position(rset);
      
      List classes = new ArrayList();
      ClassType c;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
      {
         c = new ClassType(rset.getString(1));
         c.setId(rset.getInt(2));
         c.setClassType(rset.getInt(4));

         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(3));
         c.setDoc(doc);
         
         classes.add(c);
      }
      
      rset.close();
      request.setAttribute(key, classes);
   }
   

   public void doSubclasses(ClassType refc) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("xref_subclass");

      PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
      pstmt.setString(1, refc.getQualifiedName());
      pstmt.setFetchSize(FETCH_SIZE);
      ResultSet rset = pstmt.executeQuery();

      int position = position(rset);

      List classes = new ArrayList();
      ClassType c;
      while (rset.next() && rset.getRow() <= (position + FETCH_SIZE))
      {
         c = new ClassType(rset.getString(1));
         c.setId(rset.getInt(2));
         c.setClassType(rset.getInt(4));

         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(3));
         c.setDoc(doc);
         
         classes.add(c);
      }
      
      rset.close();
      request.setAttribute("subclasses", classes);
   }
   

}
