package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

import org.apache.oro.text.perl.*;

public class ClassesPage extends Page
{
   public ClassesPage() throws SQLException
   {
      super();
   }
   
   public String init() throws SQLException
   {
      boolean simpleSearch = (Boolean.valueOf((String) ServletUtils.getRequestParam(request, "simple"))).booleanValue();
      
      List found = null;
      
      if (simpleSearch)
      {
         String searchField = ServletUtils.getRequestParam(request, "searchField");
         if (StringUtils.isBlank(searchField))
            return null;

         found = getSimpleClasses(searchField);
      }
      else
      {
         found = getComplexClasses();
      }
      
      if (found.size()==1)
      {
        int id = ((ClassType)found.get(0)).getId(null);
        request.setAttribute("cls_id", ""+id); // must pass parms as strings!!
        request.setAttribute("cmd", "cls.main");
        return "cls.main";
      }

      request.setAttribute("display_results", new Boolean(true));
      request.setAttribute("cls_list", found);
      return null;

   }

   public List getComplexClasses() throws SQLException
   {
      String selectors[] = request.getParameterValues("selector");
      if (selectors == null || selectors.length == 0)
         return null;

      Map filters = new HashMap(10);
      String value;
      for (int i=0; i<selectors.length; i++)
      {
         if ("author".equals(selectors[i]) || "package_name".equals(selectors[i]) 
               || "searchField".equals(selectors[i]))
         {
            value = ServletUtils.getRequestParam(request, selectors[i]);
            value = value.replace('*', '%').toLowerCase();
            filters.put(selectors[i], value);
         }
         else
         {
            filters.put(selectors[i], new Integer(ServletUtils.getIntParam(request, selectors[i])));
         }
      }

      String sql = " select " +
           " c.id, c.qualifiedname, c.type, c.isstatic, c.isfinal, c.isabstract, " + 
           " c.accessibility, c.modifier, d.summarydescription, d.since, d.deprecated " +
         " from ";

      List fromClause = new ArrayList(10);
      fromClause.add("CLASSTYPE c, DOC d");
      if (filters.get("author")!=null)
         fromClause.add("CLASS_AUTHOR ca, AUTHOR a");
      if (filters.get("package_name")!=null)
         fromClause.add("PACKAGE p");
      
      sql += StringUtils.join(fromClause.toArray(), ",") + 
         " where ";

      List whereClause = new ArrayList(10);
      if (filters.get("searchField")!=null)
      {
         String selectby = "c.qualifiedname";
         if (!JDocUtil.isQualified((String) filters.get("searchField")))
            selectby = "c.name";
         whereClause.add("lower("+selectby+") like ?");
      }
      if (filters.get("class_type")!=null)
         whereClause.add("c.type=?");
      if (filters.get("abstract")!=null)
         whereClause.add("c.isabstract=?");
      if (filters.get("author")!=null)
         whereClause.add("c.id=ca.classid and ca.authorid=a.id and lower(a.name) like ?");
      if (filters.get("package_name")!=null)
         whereClause.add("c.packageid=p.id and lower(p.name) like ?");
      whereClause.add("c.docid=d.id");
      if (filters.get("deprecated")!=null)
      {
         if ( ((Integer)filters.get("deprecated")).intValue() == 1)
            whereClause.add("d.deprecated != ''");
         else
            whereClause.add("d.deprecated = ''");
      }
      
      sql += StringUtils.join(whereClause.toArray(), " and ");

      if (DBMgr.getInstance().getDbtype().equals("oracle"))
         sql += "  and rownum<50 ";
      
      sql += " order by c.qualifiedname, c.type ";
      
      if (!DBMgr.getInstance().getDbtype().equals("oracle"))
         sql += "limit 50";

      PreparedStatement p = conn.prepareStatement(sql);
      int i=1;
      if (filters.get("searchField")!=null)
         p.setString(i++, (String) filters.get("searchField"));
      if (filters.get("class_type")!=null)
         p.setInt(i++, ((Integer) filters.get("class_type")).intValue());
      if (filters.get("abstract")!=null)
         p.setInt(i++, ((Integer) filters.get("abstract")).intValue());
      if (filters.get("author")!=null)
         p.setString(i++, (String) filters.get("author"));
      if (filters.get("package_name")!=null)
         p.setString(i++, (String) filters.get("package_name"));
      
      Logger.getInstance().debug("sql query for advanced search:\n\t"+sql);
      
      ResultSet rset = p.executeQuery();

      List found = new ArrayList();
      ClassType c;
      DocInfo doc;
      while (rset.next())
      {
        c = new ClassType(rset.getString(2));
        c.setId(rset.getInt(1));
        c.setClassType(rset.getInt(3));
        c.setStatic(rset.getBoolean(4));
        c.setFinal(rset.getBoolean(5));
        c.setAbstract(rset.getBoolean(6));
        c.setAccessibility(rset.getInt(7));
        c.setModifiers(rset.getString(8));
        c.setDoc(new DocInfo(rset.getString(9), rset.getString(10), rset.getString(11)));
        found.add(c);
      }
      
      return found;
      
   }
   
   
   public List getSimpleClasses(String searchField) throws SQLException
   {
      searchField = searchField.toLowerCase();
      String selectby = "c.qualifiedname";
      if (!JDocUtil.isQualified(searchField))
      {
         selectby = "c.name";
      }

      Perl5Util util = new Perl5Util();
      String pattern = "s/\\*/%/g";
      searchField = util.substitute(pattern, searchField);
      //searchField = searchField.replaceAll("\\*", "%");

      String sql =
         " select c.id, c.qualifiedname, c.type, " + 
         " c.isstatic, c.isfinal, c.isabstract, c.accessibility, c.modifier, " +
         " d.summarydescription, d.since, d.deprecated " + 
         " from CLASSTYPE c, DOC d " + 
         " where lower("+selectby+") like ? " + 
         "  and c.docid=d.id " +
         " order by c.qualifiedname, c.type " +
         " limit 50 ";
      
      if (DBMgr.getInstance().getDbtype().equals("oracle"))
      {
         sql =
         " select c.id, c.qualifiedname, c.type, " + 
         " c.isstatic, c.isfinal, c.isabstract, c.accessibility, c.modifier, " +
         " d.summarydescription, d.since, d.deprecated " + 
         " from CLASSTYPE c, DOC d " + 
         " where lower("+selectby+") like ? " + 
         "  and c.docid=d.id " +
         "  and rownum<50 " + 
         " order by c.qualifiedname, c.type";
      }
      
      PreparedStatement p = conn.prepareStatement(sql);
      p.setString(1, searchField);
      ResultSet rset = p.executeQuery();

      List found = new ArrayList();
      ClassType c;
      DocInfo doc;
      while (rset.next())
      {
        c = new ClassType(rset.getString(2));
        c.setId(rset.getInt(1));
        c.setClassType(rset.getInt(3));
        c.setStatic(rset.getBoolean(4));
        c.setFinal(rset.getBoolean(5));
        c.setAbstract(rset.getBoolean(6));
        c.setAccessibility(rset.getInt(7));
        c.setModifiers(rset.getString(8));
        c.setDoc(new DocInfo(rset.getString(9), rset.getString(10), rset.getString(11)));
        found.add(c);
      }
      
      return found;
   }
   
   
}
