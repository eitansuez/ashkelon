package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import com.sun.javadoc.*;
import java.util.*;
import org.ashkelon.db.*;
import org.ashkelon.util.*;
import java.sql.*;
import java.io.*;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.PackageDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class JPackage implements JDoc, Serializable
{
   private String name;
   private DocInfo doc;
   private List classes;
   
   private List ordinaryClasses;
   private List exceptionClasses;
   private List errorClasses;
   private List interfaces;

   private static String SEQUENCE = "PKG_SEQ";
   private static String TABLENAME = "PACKAGE";
   
   private int id;
   private boolean idSet = false;
   
   private API api;
   
   private transient Logger log;
   
   public JPackage(String name)
   {
      log = Logger.getInstance();
      setName(name);
      setClasses(new ArrayList());
      setDoc(new DocInfo());
   }
   
   public JPackage(PackageDoc packageDoc, boolean recurseClasses)
   {
      this(packageDoc.name());
      setDoc(new DocInfo(packageDoc));
      if (recurseClasses)
      {
         addClasses(packageDoc.allClasses());
      }
   }
   
   public void store(Connection conn) throws SQLException
   {
      String prefix = log.getPrefix();
      log.setPrefix("JPackage");
      if (exists(conn))
      {
         //log.traceln("Skipping package " + getName() + " (already in repository)");
         //log.traceln("(to update " + getName() + ", remove then add)");
         return;
      }

      Map fieldInfo = new HashMap(5);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(name, 60));
      fieldInfo.put("DOCID", new Integer(getDoc().getId(conn)));

      DBUtils.insert(conn, TABLENAME, fieldInfo);
      getDoc().store(conn);
      for (int i=0; i<classes.size(); i++)
      {
         ClassType cls = (ClassType) classes.get(i);
         log.verbose("about to store class: "+cls.getQualifiedName());
         (cls).store(conn);
      }
      log.setPrefix(prefix);
   }
   
   public boolean exists(Connection conn) throws SQLException
   {
      Map constraints = new HashMap();
      constraints.put("NAME", getName());
      Object obj = DBUtils.getObject(conn, TABLENAME, "ID", constraints);
      if (obj == null)
         return false;
      
      if (!idSet)
      {
         id = ((Number) obj).intValue();
         idSet = true;
      }
      return true;
   }
   
   /**
    * @param pkgName the package to remove
    * @return whether pkgName was found in the repository
    */
   public static boolean delete(Connection conn, String pkgName) throws SQLException
   {
      Logger log = Logger.getInstance();
      
      String sql = "select p.id, p.docid, c.id, c.docid " +
                   "from PACKAGE p, CLASSTYPE c " +
                   "where p.name=? and c.packageid=p.id";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, pkgName);
      ResultSet rset = pstmt.executeQuery();

      if (!rset.next())
      {
         log.traceln("Skipping Package " + pkgName + " (not in repository)");
         rset.close();
         pstmt.close();

         // see if package exists with no child classes
         return (deleteNoChildren(conn, pkgName));
      }

      int pid = rset.getInt(1);
      int docid = rset.getInt(2);
      int classid = rset.getInt(3);
      int class_docid = rset.getInt(4);

      // delete classes
      ClassType.delete(conn, classid, class_docid);

      while (rset.next())
      {
         classid = rset.getInt(3);
         class_docid = rset.getInt(4);
         ClassType.delete(conn, classid, class_docid);
      }

      rset.close();
      pstmt.close();

      // delete doc
      DocInfo.delete(conn, docid);

      // sever references to reference
      sql = "update REFERENCE set refdoc_id = null where refdoc_id = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, pid);
      pstmt.executeUpdate();
      pstmt.close();

      // delete self
      HashMap constraint = new HashMap();
      constraint.put("ID", new Integer(pid));
      DBUtils.delete(conn, TABLENAME, constraint);

      return true;
   }

   public static boolean deleteNoChildren(Connection conn, String pkgName)
       throws SQLException
   {
      HashMap constraint = new HashMap();
      constraint.put("NAME", pkgName);
      int num_deleted = DBUtils.delete(conn, TABLENAME, constraint);
      return (num_deleted > 0);
   }
   
   
   public static List getNames(Connection conn) throws SQLException
   {
      String sql = "select name from " + TABLENAME + " order by name";
      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      List pkgs = new ArrayList();
      while (rset.next())
      {
         pkgs.add(rset.getString(1));
      }
      rset.close();
      stmt.close();
      return pkgs;
   }
   
   /**
    * uses the primary key manager to assign an id to this object for/before
    * insertion into db
    */
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
   
   // accessor methods..
   public String getName()
   {
      return name;
   }
   public void setName(String name)
   {
      this.name = name;
   }
   
   public DocInfo getDoc()
   {
      return doc;
   }
   public void setDoc(DocInfo doc)
   { 
      this.doc = doc;
   }

   public List getClasses()
   {
      return classes;
   }
   public void setClasses(List classes)
   {
      this.classes = new ArrayList(35);
      ordinaryClasses = new ArrayList(12);
      interfaces = new ArrayList(12);
      exceptionClasses = new ArrayList(12);
      errorClasses = new ArrayList(12);
      
      for (int i=0; i<classes.size(); i++)
      {
         addClass((ClassType)classes.get(i));
      }
   }

   public void addClasses(ClassDoc[] classes)
   {
      for (int i=0; i<classes.length; i++)
      {
         // algorithm:  innerclasses will be added as children of parent classes
         // therefore skip over them here.
         if (classes[i].containingClass() == null)
            addClass(classes[i]);
      }
   }
   
   public void addClass(ClassDoc classdoc)
   {
      log.verbose(classdoc.qualifiedName());
      addClass(new ClassType(classdoc, this));
   }
   
   public void addClass(ClassType classtype)
   {
      classes.add(classtype);
      switch (classtype.getClassType())
      {
         case ClassType.ORDINARY_CLASS:
         {
            ordinaryClasses.add(classtype);
            break;
         }
         case ClassType.INTERFACE:
         {
            interfaces.add(classtype);
            break;
         }
         case ClassType.ERROR_CLASS:
         {
            errorClasses.add(classtype);
            break;
         }
         case ClassType.EXCEPTION_CLASS:
         {
            exceptionClasses.add(classtype);
            break;
         }
      }
   }
   
   public List getOrdinaryClasses() { return ordinaryClasses; }
   public List getExceptionClasses() { return exceptionClasses; }
   public List getInterfaces() { return interfaces; }
   public List getErrorClasses() { return errorClasses; }
   
   public boolean hasOrdinaryClasses() { return !ordinaryClasses.isEmpty(); }
   public boolean hasExceptionClasses() { return !exceptionClasses.isEmpty(); }
   public boolean hasInterfaces() { return !interfaces.isEmpty(); }
   public boolean hasErrorClasses() { return !errorClasses.isEmpty(); }

   public API getAPI() { return api; }
   public void setAPI(API api) { this.api = api; }

   public static JPackage makePackageFor(Connection conn, int pkgId) throws SQLException
   {
      /*
      String sql =
         "select p.id, p.name " + 
         "  d.summarydescription, d.description, d.since, d.deprecated, d.id " +
         " from package p, doc d " +
         " where p.id=? " +
         "  and p.docid=d.id " +
         " order by p.name ";
       */
      String sql = DBMgr.getInstance().getStatement("makepackage");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, pkgId);
      ResultSet rset = pstmt.executeQuery();
      
      if (!rset.next())
         return null;
      
      JPackage pkg = new JPackage(rset.getString(2));
      pkg.setId(rset.getInt(1));
      pkg.setDoc(new DocInfo(rset.getString(3), rset.getString(5), rset.getString(6), rset.getString(4)));
      pkg.getDoc().setId(rset.getInt(7));
      String apiname = rset.getString(8);
      if (!StringUtils.isBlank(apiname))
      {
         API api = new API(rset.getString(8));
         api.setId(rset.getInt(9));
         pkg.setAPI(api);
      }
      
      rset.close();
      pstmt.close();
      
      pkg.getClassInfo(conn);
      pkg.getDoc().fetchRefs(conn);
      
      return pkg;
   }
   
   public void getClassInfo(Connection conn) throws SQLException
   {
      /*
      String sql =
         "select c.qualifiedname, c.type, c.id, c.superclassname, s.superclassid, " +
         " d.summarydescription, d.since, d.deprecated, " + 
         " c.isstatic, c.isfinal, c.isabstract, c.accessibility, c.modifier " +
         " from classtype c, doc d, superclass s " +
         " where c.packageid=? and c.docid=d.id and c.id=s.classid (+)" + 
         " order by c.type, c.name, isstatic, isabstract";
       */
      String sql = DBMgr.getInstance().getStatement("classinfo");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      ClassType classtype;
      while (rset.next())
      {
         classtype = new ClassType(rset.getString(1));
         classtype.setClassType(rset.getInt(2));
         classtype.setId(rset.getInt(3));
         classtype.setDoc(new DocInfo(rset.getString(6), rset.getString(7), rset.getString(8)));
         classtype.setSuperClassName(rset.getString(4));
         classtype.setStatic(rset.getBoolean(9));
         classtype.setFinal(rset.getBoolean(10));
         classtype.setAbstract(rset.getBoolean(11));
         classtype.setAccessibility(rset.getInt(12));
         classtype.setModifiers(rset.getString(13));
         
         int superid = rset.getInt(5);
         if (superid > 0)
         {
              classtype.setSuperClass(new ClassType(classtype.getSuperClassName()));
              classtype.getSuperClass().setId(superid);
         }
         addClass(classtype);
      }
      
      rset.close();
      pstmt.close();
   }
   
   
   public TreeNode buildTree(Connection conn) throws SQLException
   {
      List classes = getClasses();
      List clstreelist = new ArrayList();
      ClassType c;
      
      for (int i=0; i<classes.size(); i++)
      {
         c = (ClassType) classes.get(i);
         //log.debug("Target class: "+c.getQualifiedName());
         
         TreeNode clstree = c.getSuperclasses(conn);
         
         //log.debug(TreeNode.printTree(clstree,0));
         clstreelist.add(clstree);
      }
      
      TreeNode pkgNode = new TreeNode(this);

      TreeNode clstree;

      log.debug(clstreelist.size()+" little trees to merge");

      for (int i=0; i<clstreelist.size(); i++)
      {
         clstree = (TreeNode) clstreelist.get(i);
         String key = ((ClassType) clstree.getValue()).getQualifiedName();
         //log.debug(key);

         TreeNode node = pkgNode;
         TreeNode targetnode = clstree;
         String targetkey = key;
         
         while(node.getChild(targetkey)!=null)
         {
            //log.debug(targetkey + " already in tree..");
            node = node.getChild(targetkey);
            targetnode = targetnode.getOnlyChild();
            if (targetnode == null)
            {
               continue;
            }
            targetkey = ((ClassType) targetnode.getValue()).getQualifiedName();
         }
         if (targetnode == null)
         {
            continue;
         }
         
         if (node.getValue() instanceof ClassType)
         {
            log.debug(targetkey + " not in tree\n" + 
                           "   adding to : "+((ClassType) node.getValue()).getQualifiedName()+ "\n" + 
                           " ( should be : "+((ClassType)targetnode.getValue()).getSuperClassName()+")");
         }

         String clsname = ((ClassType)targetnode.getValue()).getQualifiedName();
         String supercls = ((ClassType)targetnode.getValue()).getSuperClassName();
         if (!StringUtils.isBlank(supercls) && !clsname.equals("java.lang.Object") && 
             node.getValue() instanceof JPackage)
         {
            if (node.getChild("java.lang.Object")==null)
               continue;
            
            TreeNode newnode = null;
            
            if (node.getChild("java.lang.Object").getChild(supercls)==null)
            {
               newnode = new TreeNode();
            } else
            {
               newnode = node.getChild("java.lang.Object").getChild(supercls);
            }
            //construct classtype for absent superclass:
            ClassType absentparent = new ClassType(supercls);
            absentparent.setClassType(JDocUtil.UNKNOWN_TYPE);
            absentparent.setPackage(this);
            absentparent.setLevel(2);
            absentparent.setDoc(new DocInfo("","",""));
            absentparent.setSuperClassName("java.lang.Object");
            absentparent.setSuperClass((ClassType) node.getChild("java.lang.Object").getValue());
            
            newnode.setValue(absentparent);
            
            ClassType targetCls = (ClassType) targetnode.getValue();
            targetCls.setLevel(3);

            targetnode.setParent(newnode);
            newnode.addChild(targetkey, targetnode);
            
            node.getChild("java.lang.Object").addChild(supercls, newnode);
         }
         else
         {
            node.addChild(targetkey, targetnode);
         }
         
         //log.debug(TreeNode.printTree(pkgNode,0));
         //if (i > 3) { break; }
      }
      
      return pkgNode;
   }
   
   private TreeNode makeSimpleClassTree(List ctree)
   {
      TreeNode root = new TreeNode(ctree.get(0));
      ClassType c = null;
      TreeNode n = root;
      for (int i=1; i<ctree.size(); i++)
      {
         c = (ClassType) ctree.get(i);
         
         n.addChild(c.getQualifiedName(), new TreeNode(c));
         n = n.getOnlyChild();
      }
      return root;
   }
   
   public String toString()
   {
      return getName();
   }
   
   public static void main(String[] args)
   {
      DBMgr mgr = null;
      Connection conn = null;
      Logger.getInstance().setTraceLevel(Logger.DEBUG);
      
      try
      {
         mgr = DBMgr.getInstance();
         conn = mgr.getConnection();
         JPackage pkg = JPackage.makePackageFor(conn, 102);
         TreeNode pkgTree = pkg.buildTree(conn);
         System.out.println(TreeNode.printTree(pkgTree,0));
      } catch (SQLException ex)
      {
         ex.printStackTrace();
         DBUtils.logSQLException(ex);
      }
      finally
      {
         mgr.releaseConnection(conn);
      }
   }
   

   public String getSummaryDescription()
   {
      return getDoc().getSummaryDescription();
   }
   public String getDescription()
   {
      return getDoc().getDescription();
   }
   public String getSince()
   {
      return getDoc().getSince();
   }
   public String getDeprecatedDescr()
   {
      return getDoc().getDeprecated();
   }
   public boolean isDeprecated()
   {
      return getDoc().isDeprecated();
   }
   public List getReferences()
   {
      return getDoc().getReferences();
   }
 
}
