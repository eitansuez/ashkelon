package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import com.sun.javadoc.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Part of Persistable javadoc object model known as Ashkelon
 * Analog of com.sun.javadoc.ClassDoc
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class ClassType implements Comparator, JDoc, Serializable
{
   private int classType;
   private String qualifiedName;
   private String name;
   private JPackage jPackage;
   private ClassType superClass;
   private String superClassName;
   private boolean isAbstract;
   private String version;
   private DocInfo doc;
   
   private boolean isStatic;
   private boolean isFinal;
   private int accessibility;
   private String modifiers;
   
   private List authors;
   private List interfaces;
   
   private List fields;
   private List constructors;
   private List methods;

   private String containingClassName;
   private ClassType containingClass;
   private List innerClasses;
   private API api;
   
   private int id;
   private boolean idSet = false;
   
   private boolean storePackage = false;
   
   private int level;  // not kept in database.  
                   //useful for building class hierarchy trees
   
   private static String SEQUENCE = "CLASSTYPE_SEQ";
   private static String TABLENAME = "CLASSTYPE";

   /** constants representing the four types of classesdocs */
   public static final int ORDINARY_CLASS = 1;
   /** constants representing the four types of classesdocs */
   public static final int INTERFACE = 2;
   /** constants representing the four types of classesdocs */
   public static final int EXCEPTION_CLASS = 3;
   /** constants representing the four types of classesdocs */
   public static final int ERROR_CLASS = 4;

   public static final String[] CLASSTYPES = {"ordinaryClass", "interface", "exception", "errorClass"};

   private transient Logger log;
   
   public ClassType(String qualifiedName)
   {
      setQualifiedName(qualifiedName);
      setName(JDocUtil.unqualify(qualifiedName));
      
      setAuthors(new ArrayList());
      setInterfaces(new ArrayList());
      
      setFields(new ArrayList());
      setConstructors(new ArrayList());
      setMethods(new ArrayList());
      
      setSuperClassName("");
      
      setContainingClassName("");
      setInnerClasses(new ArrayList());

      Logger.getInstance().debug("qualified name: "+qualifiedName);

      // there are cases where the qualified name passed in may not be fully qualified
      // as expected by this method.  such a situation is where a class extends another class
      // that is in a separate package that is not in the sourcepath.  in that case, 
      // superclass().qualifiedName() will not return the superclass qualified name
      if (qualifiedName.indexOf(".") > -1)
        setPackage(new JPackage(qualifiedName.substring(0, qualifiedName.lastIndexOf("."))));
      
      setDoc(new DocInfo());
      
      log = Logger.getInstance();
   }
   
   public ClassType(ClassDoc classdoc, API api)
   {
      this.api = api;
      setName(classdoc.name());
      setQualifiedName(classdoc.qualifiedTypeName());
      
      setDoc(new DocInfo(classdoc));

      ClassDoc superClass = classdoc.superclass();
      if (superClass == null)
         setSuperClassName("");
      else
         setSuperClassName(superClass.qualifiedName());
      
      ClassDoc containingClass = classdoc.containingClass();
      if (containingClass == null)
         setContainingClassName("");
      else
         setContainingClassName(containingClass.qualifiedName());
      
      
      setAbstract(classdoc.isAbstract());
      String version = JDocUtil.getTagText(classdoc.tags("@version"));
      if (version == null)
         version = "";
      setVersion(version);
      setClassType(getClassType(classdoc));

      setStatic(classdoc.isStatic());
      setFinal(classdoc.isFinal());
      setAccessibility(JDocUtil.getAccessibility(classdoc));
      String modifiers = classdoc.modifiers();
      if (modifiers.endsWith(" interface"))
      {
         modifiers = modifiers.substring(0, modifiers.indexOf(" interface"));
      }
      setModifiers(modifiers);

      authors = new ArrayList();
      addAuthors(classdoc);

      interfaces = new ArrayList();
      addInterfaces(classdoc.interfaces());
      
      fields = new ArrayList();
      addFields(classdoc.fields());
      
      constructors = new ArrayList();
      addConstructors(classdoc.constructors());

      methods = new ArrayList();
      addMethods(classdoc.methods());
      
      innerClasses = new ArrayList();
      addInnerClasses(classdoc.innerClasses());
   }
   
   public ClassType(ClassDoc classdoc, JPackage jp, API api)
   {
      this(classdoc, api);
      if (jp == null)
      {
         jp = new JPackage(classdoc.containingPackage(), false, api);
         storePackage = true;
      }
      setPackage(jp);
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
   
   public void store(Connection conn) throws SQLException
   {
      if (storePackage)
      {
         // will not recurse because package was generated without any child classes
         getPackage().store(conn);
      }

      Map fieldInfo = new HashMap(20);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("TYPE", new Integer(getClassType()));
      fieldInfo.put("QUALIFIEDNAME", StringUtils.truncate(getQualifiedName(), 150));
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 100));
      fieldInfo.put("SUPERCLASSNAME", StringUtils.truncate(getSuperClassName(), 150));

      fieldInfo.put("CONTAININGCLASSNAME", StringUtils.truncate(getContainingClassName(), 150));
      
      //fieldInfo.put("ISABSTRACT", new Boolean(isAbstract()));
      int boolvalue = isAbstract() ? 1 : 0;
      fieldInfo.put("ISABSTRACT", new Integer(boolvalue));

      fieldInfo.put("VERSION", StringUtils.truncate(getVersion(), 100));
      fieldInfo.put("DOCID", new Integer(getDoc().getId(conn)));
      fieldInfo.put("PACKAGEID", new Integer(getPackage().getId(conn)));
      
      //fieldInfo.put("ISSTATIC", new Boolean(isStatic()));
      boolvalue = isStatic() ? 1 : 0;
      fieldInfo.put("ISSTATIC", new Integer(boolvalue));

      //fieldInfo.put("ISFINAL", new Boolean(isFinal()));
      boolvalue = isFinal() ? 1 : 0;
      fieldInfo.put("ISFINAL", new Integer(boolvalue));

      fieldInfo.put("ACCESSIBILITY", new Integer(getAccessibility()));
      fieldInfo.put("MODIFIER", StringUtils.truncate(getModifiers(), 31));
      DBUtils.insert(conn, TABLENAME, fieldInfo);

      getDoc().store(conn);
      
      storeClassAuthors(conn);
      storeInterfaces(conn);
      storeSuperClass(conn);
      
      for (int i=0; i<fields.size(); i++)
      {
         ((FieldMember) fields.get(i)).store(conn);
      }
      for (int i=0; i<constructors.size(); i++)
      {
         ((ConstructorMember) constructors.get(i)).store(conn);
      }
      for (int i=0; i<methods.size(); i++)
      {
         ((MethodMember) methods.get(i)).store(conn);
      }
      
      for (int i=0; i<innerClasses.size(); i++)
      {
         ((ClassType) innerClasses.get(i)).store(conn);
      }
   }
   
   public void storeClassAuthors(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("CLASSID", new Integer(getId(conn)));
      Author author = null;
      for (int i=0; i<authors.size(); i++)
      {
         author = (Author) authors.get(i);
         author.store(conn);
         
         fieldInfo.put("AUTHORID", new Integer(author.getId(conn)));
         DBUtils.insert(conn, "CLASS_AUTHOR", fieldInfo);
      }
   }
   
   public void storeInterfaces(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("CLASSID", new Integer(getId(conn)));
      String interface_name = "";
      for (int i=0; i<interfaces.size(); i++)
      {
         interface_name = ((ClassType) interfaces.get(i)).getQualifiedName();
         fieldInfo.put("NAME", StringUtils.truncate(interface_name, 150));
         DBUtils.insert(conn, "IMPL_INTERFACE", fieldInfo);
      }
   }

   public void storeSuperClass(Connection conn) throws SQLException
   {
      if (StringUtils.isBlank(getSuperClassName()))
         return;
      Map fieldInfo = new HashMap(3);
      fieldInfo.put("CLASSID", new Integer(getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(getSuperClassName(), 150));
      DBUtils.insert(conn, "SUPERCLASS", fieldInfo);
   }
   
   
   /**
    * @param className the class to remove
    * @return whether the className was found in the repository
    */
   public static boolean delete(Connection conn, String className) throws SQLException
   {
      //Logger log = Logger.getInstance();
      String sql = DBMgr.getInstance().getStatement("getclsanddocid");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, className);
      ResultSet rset = pstmt.executeQuery();

      if (!rset.next())
         return false;

      int classid = rset.getInt(1);
      int classdocid = rset.getInt(2);
      rset.close();
      pstmt.close();

      deleteInnerClasses(conn, className);
      
      delete(conn, classid, classdocid);
      return true;
   }
   
   public static void delete(Connection conn, int classid, int class_docid) throws SQLException
   {
      deleteFields(conn, classid);
      deleteMethods(conn, classid);
      deleteConstructors(conn, classid);
      
      deleteInterfaces(conn, classid);
      deleteSuperClass(conn, classid);
      deleteClassAuthors(conn, classid);
      
      DocInfo.delete(conn, class_docid);

      // sever references
      String[][] params = {{"IMPL_INTERFACE", "interfaceid"},
                           {"SUPERCLASS", "superclassid"},
                           {"FIELD", "typeid"},
                           {"METHOD", "returntypeid"},
                           {"THROWNEXCEPTION", "exceptionid"},
                           {"PARAMETER", "typeid"},
                           {"REFERENCE", "refdoc_id"}};

      String sql = "";
      PreparedStatement pstmt;
      for (int i=0; i<params.length; i++)
      {
         sql = "update " + params[i][0] + " set " + params[i][1] + " = null where " + params[i][1] + " = ?";
         Logger.getInstance().debug(sql);
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, classid);
         pstmt.executeUpdate();
         pstmt.close();
      }
         
      // delete self
      HashMap constraint = new HashMap();
      constraint.put("ID", new Integer(classid));
      DBUtils.delete(conn, TABLENAME, constraint);
   }
   
   public static void deleteFields(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getfieldanddocid");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      ResultSet rset = pstmt.executeQuery();
      
      while (rset.next())
      {
         FieldMember.delete(conn, rset.getInt(1), rset.getInt(2));
      }
      
      rset.close();
      pstmt.close();
   }
   
   public static void deleteMethods(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getmethodanddocid");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      ResultSet rset = pstmt.executeQuery();
      
      while (rset.next())
      {
         MethodMember.delete(conn, rset.getInt(1), rset.getInt(2));
      }
      
      rset.close();
      pstmt.close();
   }

   public static void deleteConstructors(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getconstranddocid");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      ResultSet rset = pstmt.executeQuery();
      
      while (rset.next())
      {
         ConstructorMember.delete(conn, rset.getInt(1), rset.getInt(2));
      }
      
      rset.close();
      pstmt.close();
   }
   
   public static void deleteClassAuthors(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("deleteclassauthor");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      pstmt.executeUpdate();
      pstmt.close();
   }
   
   public static void deleteInterfaces(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("deleteinterfaces");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      pstmt.executeUpdate();
      pstmt.close();
   }

   public static void deleteSuperClass(Connection conn, int classid) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("deletesuperclass");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, classid);
      pstmt.executeUpdate();
      pstmt.close();
   }
   
   public static void deleteInnerClasses(Connection conn, String containingClassName) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("deleteinnercls");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, containingClassName);
      pstmt.executeUpdate();
      pstmt.close();
   }
   
   
   public boolean isInterface()
   {
      return (getClassType() == INTERFACE);
   }
   public boolean isClass()
   {
      return (getClassType() == ORDINARY_CLASS);
   }
   public boolean isException()
   {
      return (getClassType() == EXCEPTION_CLASS);
   }
   public boolean isError()
   {
      return (getClassType() == ERROR_CLASS);
   }
   
   public boolean isInnerClass()
   {
      return !StringUtils.isBlank(getContainingClassName());
   }

   
   // accessor methods
   public int getClassType() { return classType; }
   public void setClassType(int classType) { this.classType = classType; }
   
   public String getClassTypeName()
   {
      int clsType = getClassType();
      if (clsType > CLASSTYPES.length || clsType <= 0)
      {
         return "unknown";
      }
      return CLASSTYPES[clsType-1];
   }
   
   public String getQualifiedName() { return qualifiedName; }
   public void setQualifiedName(String qualifiedName)
   {
      // should really throw an exception if isblank.
      this.qualifiedName = StringUtils.avoidNull(qualifiedName);
   }
   
   // accessor methods
   public String getName() { return name; }
   public void setName(String name)
   {
      this.name = StringUtils.avoidNull(name);
   }
   
   public DocInfo getDoc() { return doc; }
   public void setDoc(DocInfo doc) { this.doc = doc; }
   
   public JPackage getPackage() { return jPackage; }
   public void setPackage(JPackage jPackage) { this.jPackage = jPackage; }
   
   public ClassType getSuperClass() { return superClass; }
   public void setSuperClass(ClassType superClass) { this.superClass = superClass; }
   
   
   public String getSuperClassName() { return superClassName; }
   public void setSuperClassName(String superClassName)
   {
      this.superClassName = StringUtils.avoidNull(superClassName);
   }

   public String getContainingClassName() { return containingClassName; }
   public void setContainingClassName(String containingClassName)
   {
      this.containingClassName = StringUtils.avoidNull(containingClassName);
   }
   
   public ClassType getContainingClass() { return containingClass; }
   public void setContainingClass(ClassType containingClass) { this.containingClass = containingClass; }
   
   
   public boolean isAbstract() { return isAbstract; }
   public void setAbstract(boolean isAbstract) {  this.isAbstract = isAbstract; }
   
   public String getVersion() { return version; }
   public void setVersion(String version) 
   {
      this.version = StringUtils.avoidNull(version);
   }
   
   public boolean isStatic() { return isStatic; }
   public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
   
   public boolean isFinal() { return isFinal; }
   public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
   
   public int getAccessibility() { return accessibility; }
   public void setAccessibility(int accessibility) { this.accessibility = accessibility; }
   
   public String getModifiers() { return modifiers; }
   public void setModifiers(String modifiers) { this.modifiers = modifiers; }

   public void addAuthors(ClassDoc classdoc)
   {
      String[] names = JDocUtil.getTagList(classdoc.tags("@author"));
      addAuthors(names);
   }
   
   public void addAuthors(String[] names)
   {
      //Author author = null;
      for (int i=0; i<names.length; i++)
      {
         addAuthor(names[i]);
      }
   }
   
   public void addAuthor(String authorName)
   {
      addAuthor(new Author(authorName));
   }
   
   public void addAuthor(Author author)
   {
      authors.add(author);
      author.addClass(this);
   }
   
   public List getAuthors() { return authors; }
   public void setAuthors(List authors) { this.authors = authors; }
   
   
   public void addInterfaces(ClassDoc[] impl_interfaces)
   {
      for (int i=0; i<impl_interfaces.length; i++)
      {
         addInterface(impl_interfaces[i].qualifiedName());
      }
   }
   
   public void addInterface(String qualifiedName)
   {
      if (StringUtils.isBlank(qualifiedName)) return;
      interfaces.add(new ClassType(qualifiedName));
   }

   public List getInterfaces() { return interfaces; }
   public void setInterfaces(List interfaces) { this.interfaces = interfaces; }
   
   
   public void addFields(FieldDoc[] fielddocs)
   {
      //FieldMember fm = null;
      for (int i=0; i<fielddocs.length; i++)
      {
         addField(new FieldMember(fielddocs[i], this));
      }
   }
   
   public void addField(FieldMember fldMember)
   {
      fields.add(fldMember);
   }
   
   public void addConstructors(ConstructorDoc[] constructordocs)
   {
      //ConstructorMember cm = null;
      for (int i=0; i<constructordocs.length; i++)
      {
         addConstructor(new ConstructorMember(constructordocs[i], this));
      }
   }
   
   public void addConstructor(ConstructorMember constr)
   {
      constructors.add(constr);
   }
   
   public void addMethods(MethodDoc[] methoddocs)
   {
      //MethodMember mm = null;
      for (int i=0; i<methoddocs.length; i++)
      {
         addMethod(new MethodMember(methoddocs[i], this));
      }
   }
   
   public void addMethod(MethodMember method)
   {
      methods.add(method);
   }
   
   public List getFields() { return fields; }
   public void setFields(List fields) { this.fields = fields; }

   public List getConstructors() { return constructors; }
   public void setConstructors(List constructors) { this.constructors = constructors; }

   public List getMethods() { return methods; }
   public void setMethods(List methods) { this.methods = methods; }

   public boolean hasFields() { return !fields.isEmpty(); }
   public boolean hasConstructors() { return !constructors.isEmpty(); }
   public boolean hasMethods() { return !methods.isEmpty(); }
   
   public boolean hasInnerClasses() { return !innerClasses.isEmpty(); }
   
   public int getLevel() { return level; }
   public void setLevel(int level) { this.level = level; }

   
   public void addInnerClass(ClassType innerClass)
   {
      innerClasses.add(innerClass);
   }
   
   public void addInnerClasses(ClassDoc[] innerClasses)
   {
      for (int i=0; i<innerClasses.length; i++)
      {
         addInnerClass(new ClassType(innerClasses[i], getPackage(), (api==null) ? getAPI() : api));
      }
   }
   
   public List getInnerClasses() { return innerClasses; }
   public void setInnerClasses(List innerClasses) { this.innerClasses = innerClasses; }
   

   /**
    * @return type for a given classdoc (plain class, interface..)
    */
   public static int getClassType(ClassDoc classdoc)
   {
      if (classdoc.isOrdinaryClass())
      {
         return ORDINARY_CLASS;
      }
      else if (classdoc.isError())
      {
         return ERROR_CLASS;
      }
      else if (classdoc.isException())
      {
         return EXCEPTION_CLASS;
      }
      else if (classdoc.isInterface())
      {
         return INTERFACE;
      }
      else
      {
         // throw an exception
         return JDocUtil.UNKNOWN_TYPE;
      }
   }
   
   private List getDescendentList(Connection conn) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("getdescendents");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      List classes = new ArrayList();
      
      ClassType c;
      while(rset.next())
      {
         c = new ClassType(rset.getString(2));
         c.setId(rset.getInt(1));
         c.setSuperClassName(rset.getString(3));
         c.setClassType(rset.getInt(4));
         c.getDoc().setSummaryDescription(rset.getString(5));
         classes.add(c);
         log.debug(c.getQualifiedName());
      }
      
      return classes;
   }
   
   public TreeNode getSuperclassesConstrained(Connection conn, ClassType root) throws SQLException
   {
      TreeNode rootnode = getSuperclasses(conn);
      if (root.getQualifiedName().equals("java.lang.Object"))
         return rootnode;
      
      TreeNode childnode = rootnode.getOnlyChild();
      while (((ClassType) childnode.getValue()).getId() != root.getId())
         childnode = childnode.getOnlyChild();
      
      return childnode.getOnlyChild();
   }

   public TreeNode getDescendents(Connection conn) throws SQLException
   {
      if (DBMgr.getInstance().getDbtype().equals("oracle"))
      {
         return getDescendentsOra(conn);
      }
      
      List descendents = getDescendentList(conn);

      List clstreelist = new ArrayList();
      ClassType c;
      
      for (int i=0; i<descendents.size(); i++)
      {
         c = (ClassType) descendents.get(i);
         //log.debug("Target class: "+c.getQualifiedName());
         
         TreeNode clstree = c.getSuperclassesConstrained(conn, this);
         
         //log.debug(TreeNode.printTree(clstree,0));
         clstreelist.add(clstree);
      }
      
      log.debug(clstreelist.size()+" little trees to merge");

      TreeNode clsNode = new TreeNode(this);
      TreeNode clstree;

      for (int i=0; i<clstreelist.size(); i++)
      {
         clstree = (TreeNode) clstreelist.get(i);
         //log.debug(TreeNode.printTree(clstree, 0));

         String key = ((ClassType) clstree.getValue()).getQualifiedName();
         //log.debug(key);

         TreeNode node = clsNode;
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
         
         //log.debug(targetkey + " not in tree\n" + 
         //               "   adding to : "+((ClassType) node.getValue()).getQualifiedName()+ "\n" + 
         //               " ( should be : "+((ClassType)targetnode.getValue()).getSuperClassName()+")");

         node.addChild(targetkey, targetnode);

         /*
         String clsname = ((ClassType)targetnode.getValue()).getQualifiedName();
         String supercls = ((ClassType)targetnode.getValue()).getSuperClassName();
         
         if (!StringUtils.isBlank(supercls) && !clsname.equals("java.lang.Object") && 
             node.getValue() instanceof JPackage )
         {
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
          */
         
         //log.debug(TreeNode.printTree(clsNode,0));
         //if (i > 3) { break; }
      }
      
      return clsNode;

   }

   public TreeNode getDescendentsOra(Connection conn) throws SQLException
   {
      
      String sql =
         " select " +
         "  id, qualifiedname, level, superclassname, type " + 
         " from CLASSTYPE " + 
         " start with qualifiedname=? " + 
         " connect by prior qualifiedname=superclassname " +
         " order by level desc, type, qualifiedname ";
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, getQualifiedName());
      ResultSet rset = pstmt.executeQuery();
      
      List classes = new ArrayList();
      
      ClassType c;
      while(rset.next())
      {
         c = new ClassType(rset.getString(2));
         c.setId(rset.getInt(1));
         c.setLevel(rset.getInt(3));
         c.setSuperClassName(rset.getString(4));
         c.setClassType(rset.getInt(5));
         classes.add(c);
         log.debug(c.getQualifiedName() + " " + c.getLevel());
      }
      
      TreeNode root = ClassType.makeTree(classes);
      return root;
   }
   

   /**
    * @param classTree a list of entries whose level property provides
    *  tree structure information.
    * @return an actual tree data structure derived from the level information
    *   in the list
    */
   public static TreeNode makeTree(List classTree)
   {
      Map children = new HashMap();
      Map parents = new HashMap();
      TreeNode root;
      
      int level, prev = 0;
      int startat = 0;
      
      ClassType cls;
      TreeNode clsNode;
      
      for (int i=0; i<classTree.size(); i++)
      {
         cls = (ClassType) classTree.get(i);
         level = cls.getLevel();
         clsNode = new TreeNode(cls);

         if (i==0) { prev = level; }
         
         if (level != prev)
         {
            startat++;
            
            if (startat >= 2)
            {
               TreeNode child;
               ClassType childClass;
               String supercls;
               TreeNode parent = null;
               
               Iterator itr = children.values().iterator();
               while (itr.hasNext())
               {
                  child = (TreeNode) itr.next();
                  childClass = (ClassType) child.getValue();
                  supercls = childClass.getSuperClassName();
                  parent = (TreeNode) parents.get(supercls);
                  if (parent != null)
                     parent.addChild(childClass.getQualifiedName(), child);
               }
            }
            
            children = parents;
            parents = new HashMap();
         }
         
         parents.put(cls.getQualifiedName(), clsNode);
         
         prev = level;
      }

      ClassType rootClass = (ClassType) classTree.get(classTree.size()-1);
      root = new TreeNode(rootClass);
      
      root.setChildren(children);
      return root;
      
   }

   /** don't use this.  makes a false assumption about the order oracle returns entries */
   public static TreeNode makeStupidTree(List classTree)
   {
      Logger log = Logger.getInstance();
      
      TreeNode root = new TreeNode(classTree.get(0));
      TreeNode current = root;
      
      int parentLevel = 1;
      int nextLevel;
      
      ClassType next;
      TreeNode nextNode;
      LinkedList lastChildren = new LinkedList();
      
      for (int i=1; i<classTree.size(); i++)
      {
         next = (ClassType) classTree.get(i);
         nextLevel = next.getLevel();
         nextNode = new TreeNode(next);
         
         if (nextLevel - parentLevel == 1)  // add child to parent
         {
            current.addChild(next.getQualifiedName(), nextNode);
            if (!lastChildren.isEmpty()) lastChildren.removeLast();
            lastChildren.add(nextNode);
         }
         else if (nextLevel - parentLevel == 2) // add child to child
         {
            current = (TreeNode) lastChildren.getLast();
            parentLevel++;
            current.addChild(next.getQualifiedName(), nextNode);
            lastChildren.add(nextNode);
         }
         else if (nextLevel <= parentLevel)  // add child to a parent
         {
            TreeNode saved = current;
            while (nextLevel <= parentLevel)
            {
               current = current.getParent();
               if (!current.getChildren().isEmpty())
                  lastChildren.removeLast();
               parentLevel--;
               
               if (current == null)
               {
                  log.debug("Iteration #: "+i);
                  log.debug("Error Occurred");
                  log.debug("parentLevel: "+parentLevel);
                  log.debug(saved.getValue().toString());
                  log.debug(TreeNode.printTree(saved, 0));
                  System.exit(0);
               }
            }
            
            current.addChild(next.getQualifiedName(), nextNode);
            lastChildren.add(nextNode);
         }
      }
      
      return root;
   }

   
   public int compare(Object p1, Object p2)
   {
      ClassType t1, t2;
      t1 = (ClassType) p1;
      t2 = (ClassType) p2;
      return (t1.getClassType() - t2.getClassType());
   }
  
   public String toString()
   {
      return getQualifiedName();
   }

   
   public static ClassType makeClassFor(Connection conn, int clsId, boolean fetchChildren)
      throws SQLException
   {
      /*
      String sql =
      "select c.id, c.qualifiedname, c.name, " + 
      "  c.superclassname, c.version, " +
      "  c.type, c.packageid, s.superclassid, " + 
      "  d.description, d.summarydescription, d.since, d.deprecated, d.id, c.modifier " +
      " from classtype c, doc d, superclass s " +
      " where c.id=? " + 
      "  and c.docid=d.id " + 
      "  and c.id=s.classid (+)";
      */
      String sql = DBMgr.getInstance().getStatement("makeclass");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, clsId);
      ResultSet rset = pstmt.executeQuery();
      
      if (!rset.next()) return null;
      
      ClassType cls = new ClassType(rset.getString(2));
      cls.setId(rset.getInt(1));
      cls.setName(rset.getString(3));
      cls.setSuperClassName(rset.getString(4));
      cls.setVersion(rset.getString(5));
      cls.setClassType(rset.getInt(6));
      cls.getPackage().setId(rset.getInt(7));
      cls.setModifiers(rset.getString(14));
      cls.setStatic(rset.getBoolean(15));
      cls.setFinal(rset.getBoolean(16));
      cls.setAbstract(rset.getBoolean(17));
      cls.setAccessibility(rset.getInt(18));
      String containingClassName = rset.getString(19);
      if (!StringUtils.isBlank(containingClassName))
      {
         cls.setContainingClassName(containingClassName);
         cls.setContainingClass(new ClassType(containingClassName));
         cls.getContainingClass().setId(rset.getInt(20));
      }
      
      cls.getPackage().setName(rset.getString(21));
      cls.getPackage().setAPI(new API(rset.getString(22)));
      cls.getAPI().setId(rset.getInt(23));

      int superid = rset.getInt(8);
      if (superid > 0)
      {
           cls.setSuperClass(new ClassType(cls.getSuperClassName()));
           cls.getSuperClass().setId(superid);
      }
      
      cls.setDoc(new DocInfo(rset.getString(10), rset.getString(11), rset.getString(12)));
      cls.getDoc().setId(rset.getInt(13));
      cls.getDoc().setDescription(rset.getString(9));
      
      rset.close();
      pstmt.close();

      if (fetchChildren)
      {
         cls.fetchAuthors(conn);
         cls.fetchInterfaces(conn);
         cls.fetchMembers(conn);
         cls.fetchInnerClasses(conn);
         cls.getDoc().fetchRefs(conn);
      }
      
      return cls;
   }
   

   public static void main(String args[])
   {
      DBMgr mgr = DBMgr.getInstance();
      Logger log = Logger.getInstance();
      log.setTraceLevel(Logger.DEBUG);
      
      Connection conn = null;
      try
      {
         conn = mgr.getConnection();

         ClassType cls = ClassType.makeClassFor(conn, 114, false);
         //TreeNode root = cls.getDescendents(conn);
         TreeNode root = cls.getSuperclasses(conn);
         String treetext = TreeNode.printTree(root, 0);
         log.debug(treetext);

         mgr.releaseConnection(conn);
      } catch (SQLException ex)
      {
         DBUtils.logSQLException(ex);
         if (conn!=null)
            mgr.releaseConnection(conn);
      }
  }

  
  public void fetchInnerClasses(Connection conn) throws SQLException
  {
     String sql = "select id from CLASSTYPE c where containingclassname=? order by name";
     PreparedStatement pstmt = conn.prepareStatement(sql);
     pstmt.setString(1, getQualifiedName());
     ResultSet rset = pstmt.executeQuery();
     
     while (rset.next())
        addInnerClass(makeClassFor(conn, rset.getInt(1), false));
     
     rset.close();
     pstmt.close();
  }
   
   public void fetchAuthors(Connection conn) throws SQLException
   {
      String sql =
         " select AUTHOR.id, AUTHOR.name " +
         " from AUTHOR, CLASS_AUTHOR ca " +
         " where ca.classid=? and ca.authorid=AUTHOR.id " +
         " order by AUTHOR.name ";
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      Author author = null;
      while (rset.next())
      {
         author = new Author(rset.getString(2));
         author.setId(rset.getInt(1));
         addAuthor(author);
      }
      
      rset.close();
      pstmt.close();
   }
   
   public void fetchInterfaces(Connection conn) throws SQLException
   {
      String sql =
         " select ii.name, ii.interfaceid " +
         " from IMPL_INTERFACE ii " +
         " where ii.classid=? " +
         " order by ii.name ";
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      ClassType ii;
      int iid;
      
      while (rset.next())
      {
         ii = new ClassType(rset.getString(1));
         iid = rset.getInt(2);
         if (iid > 0)
            ii.setId(iid);
         interfaces.add(ii);
      }
      
      rset.close();
      pstmt.close();
   }


   public void fetchMembers(Connection conn) throws SQLException
   {
      int FIELDS = 0; int TABLES = 1; int CONDITIONS = 2;
      
      String[][] memberInfo = 
         {
            {" , f.typename, f.typedimension, f.typeid ", " , FIELD f ", " and m.id=f.id "},
            {" , ex.signature ", " , EXECMEMBER ex ", " and m.id=ex.id "},
            {" , ex.signature , meth.isabstract, meth.returntypename, meth.returntypedimension, meth.returntypeid ", 
             " , EXECMEMBER ex  , METHOD meth ", 
             " and m.id=ex.id  and m.id=meth.id "}
         };
         
      PreparedStatement pstmt = null;
      ResultSet rset = null;

      String[] sql = {"", "", ""};
      for (int memberType=0; memberType<sql.length; memberType++)
      {
         sql[memberType] = 
            " select m.id, m.qualifiedname, m.type, " +
            "       m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
            "       d.summarydescription, d.since, d.deprecated " +
            memberInfo[memberType][FIELDS] + 
            "    from CLASSTYPE c, MEMBER m, DOC d  " +
            memberInfo[memberType][TABLES] + 
            "    where c.id=? and m.type=? and m.classid=c.id and m.docid=d.id " + 
            memberInfo[memberType][CONDITIONS] + 
            "    order by m.type, m.name " ;

         log.debug("member sql statement:\n\t" + sql[memberType]);
         
         pstmt = conn.prepareStatement(sql[memberType]);
         pstmt.setInt(1, getId());
         pstmt.setInt(2, memberType+1);
         rset = pstmt.executeQuery();

         Member member = null;
         DocInfo doc;
         
         while (rset.next())
         {
            doc = new DocInfo(rset.getString(8), rset.getString(9), rset.getString(10));
            
            switch (memberType+1)
            {
               case Member.METHOD_MEMBER:
               {
                  member = new MethodMember(rset.getString(2), rset.getString(11));
                  member.setId(rset.getInt(1));
                  member.setDoc(doc);
                  member.setMemberType(rset.getInt(3));
                  member.setStatic(rset.getBoolean(4));
                  member.setFinal(rset.getBoolean(5));
                  member.setAccessibility(rset.getInt(6));
                  member.setModifiers(rset.getString(7));
                  
                  MethodMember mm = (MethodMember) member;
                  mm.setAbstract(rset.getBoolean(12));
                  mm.setReturnTypeName(rset.getString(13));
                  mm.setReturnTypeDimension(rset.getInt(14));
                  int typeid = rset.getInt(15);
                  if (typeid > 0)
                  {
                     mm.setReturnType(new ClassType(mm.getReturnTypeName()));
                     mm.getReturnType().setId(typeid);
                  }
                  addMethod(mm);
                  break;
               }  // end case
               case Member.FIELD_MEMBER:
               {
                  member = new FieldMember(rset.getString(2), rset.getString(11));
                  member.setId(rset.getInt(1));
                  member.setDoc(doc);
                  member.setMemberType(rset.getInt(3));
                  member.setStatic(rset.getBoolean(4));
                  member.setFinal(rset.getBoolean(5));
                  member.setAccessibility(rset.getInt(6));
                  member.setModifiers(rset.getString(7));
                  FieldMember fm = (FieldMember) member;
                  fm.setTypeDimension(rset.getInt(12));
                  int typeid = rset.getInt(13);
                  if (typeid > 0)
                  {
                     fm.setType(new ClassType(fm.getTypeName()));
                     fm.getType().setId(typeid);
                  }
                  addField((FieldMember) member);
                  break;
               }  // end case
               case Member.CONSTRUCTOR_MEMBER:
               {
                  member = new ConstructorMember(rset.getString(2), rset.getString(11));
                  member.setId(rset.getInt(1));
                  member.setDoc(doc);
                  member.setMemberType(rset.getInt(3));
                  member.setStatic(rset.getBoolean(4));
                  member.setFinal(rset.getBoolean(5));
                  member.setAccessibility(rset.getInt(6));
                  member.setModifiers(rset.getString(7));
                  ConstructorMember cm = (ConstructorMember) member;
                  addConstructor(cm);
                  break;
               }  // end case
            }  // end switch
         } // end while
         
         rset.close();

      }  // end for

      if (pstmt != null) pstmt.close();
      
   }  // end fetchMembers
   
         
   public TreeNode getSuperclasses(Connection conn) throws SQLException
   {
      if (!DBMgr.getInstance().getDbtype().equals("oracle"))
      {
         String sql =
               "select a.superclassid, c.qualifiedname, a.hierarchy, c.superclassname, c.type " +
               " from CLASS_ANCESTORS a, CLASSTYPE c " +
               " where a.classid=? and a.superclassid=c.id " +
               " order by a.hierarchy, c.type, c.qualifiedname";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, getId());
         ResultSet rset = pstmt.executeQuery();

         LinkedList classTree = new LinkedList();
         this.setLevel(0);
         classTree.add(this);

         ClassType tree_cls;
         int numLevels = 1;
         int level = 0;
         int num_returned = 0;
      
         while(rset.next())
         {
            num_returned++;
            tree_cls = new ClassType(rset.getString(2));
            tree_cls.setId(rset.getInt(1));
            level = rset.getInt(3) + 1;
            tree_cls.setSuperClassName(rset.getString(4));
            tree_cls.setClassType(rset.getInt(5));

            numLevels = (level>numLevels) ? level : numLevels;
            tree_cls.setLevel(level);
            
            classTree.add(tree_cls);
         }
         
         rset.close();
         pstmt.close();

         for (int j=0; j<num_returned; j++)
         {
            tree_cls = (ClassType) classTree.get(j);
            tree_cls.setLevel(numLevels - tree_cls.getLevel() + 1);
         }
         
         ClassType top = (ClassType) classTree.getLast();
         if (!StringUtils.isBlank(top.getSuperClassName()))
         {
            if (num_returned == 0)
            {
               top.setLevel(2);
            }
            tree_cls = new ClassType(top.getSuperClassName());
            tree_cls.setLevel(1);
            classTree.add(tree_cls);
         }
         
         return ClassType.makeTree(classTree);
      
      }

      /*
       * technically, can now get rid of code below and use above as unified
       * solution that does not necessitate "connect by prior" statement
       */
      String sql =
         " select " +
         "  id, qualifiedname, level, superclassname, type " + 
         " from CLASSTYPE " + 
         "  start with qualifiedname=? " + 
         " connect by qualifiedname=prior superclassname " + 
         " order by level desc, type, qualifiedname" ;

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, getQualifiedName());
      ResultSet rset = pstmt.executeQuery();
      
      LinkedList classTree = new LinkedList();
      
      ClassType tree_cls;
      int numLevels = 1;
      int level = 0;
      int num_returned = 0;
      
      while(rset.next())
      {
         num_returned++;
         tree_cls = new ClassType(rset.getString(2));
         tree_cls.setId(rset.getInt(1));
         level = rset.getInt(3);
         tree_cls.setSuperClassName(rset.getString(4));
         tree_cls.setClassType(rset.getInt(5));
         numLevels = (level>numLevels) ? level : numLevels;
         tree_cls.setLevel(level);
         classTree.addFirst(tree_cls);
      }
      
      rset.close();
      pstmt.close();
      
      for (int j=0; j<num_returned; j++)
      {
         tree_cls = (ClassType) classTree.get(j);
         tree_cls.setLevel(numLevels - tree_cls.getLevel() + 1);
         log.debug("level: "+tree_cls.getLevel());
      }
      
      return ClassType.makeTree(classTree);
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
   
   public API getAPI()
   {
      return getPackage().getAPI();
   }

}  // end class!
