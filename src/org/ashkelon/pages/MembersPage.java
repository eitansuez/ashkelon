package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.util.*;
import java.sql.*;

import org.apache.oro.text.perl.*;


public class MembersPage extends Page
{
   public MembersPage()
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
      
         found = getSimpleMembers(searchField);
      }
      else
      {
         found = getComplexMembers();
      }

      if (found.size()==1 || (found.size()>1 && membersSame(found)) )
      {
         Member m = (Member) found.get(0);
         int id = m.getId(null);
         request.setAttribute("member_id", new Integer(id));
         return "member.main";
         /*
         String membertype = "method";
         if (m instanceof ConstructorMember)
         {
            membertype = "constructor";
         }
         else if (m instanceof FieldMember)
         {
            membertype = "field";
         }
         String cmd = "member." + membertype;
         return cmd;
          */
      }

      request.setAttribute("display_results", new Boolean(true));
      request.setAttribute("member_list", found);
      return null;
   }
   
   /*
    * call only with members size > 1..
    */
   private boolean membersSame(List members)
   {
      if (members == null || members.size() <= 1) return true; // safeguard
      
      String firstQName = ((Member) members.get(0)).getQualifiedName();
      
      for (int i=1; i<members.size(); i++)
      {
         Member mmb = (Member) members.get(i);
         if (!firstQName.equals(mmb.getQualifiedName()))
         {
            return false;
         }
      }
      return true;
   }
   
   
   public List getComplexMembers() throws SQLException
   {
      String selectors[] = request.getParameterValues("selector");
      if (selectors == null || selectors.length == 0)
         return null;

      Map filters = new HashMap(10);
      String value;
      for (int i=0; i<selectors.length; i++)
      {
         if ("package_name".equals(selectors[i]) || "searchField".equals(selectors[i]))
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

      String sql = 
            " select m.id, m.qualifiedname, m.type, " +
            "  m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
            "  meth.isabstract, meth.returntypeid, meth.returntypename, meth.returntypedimension, " +
            "  em.signature, " +
            "  d.summarydescription, d.since, d.deprecated " +
            " from METHOD meth, MEMBER m, EXECMEMBER em, DOC d ";
      
      if (!DBMgr.getInstance().getDbtype().equals("oracle"))
      {
         sql = 
            " select m.id, m.qualifiedname, m.type, " +
            "  m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
            "  meth.isabstract, meth.returntypeid, meth.returntypename, meth.returntypedimension, " +
            "  em.signature, " +
            "  d.summarydescription, d.since, d.deprecated " +
            " from METHOD meth right outer join MEMBER m on meth.id=m.id " +
            "  left outer join EXECMEMBER em on em.id=m.id, DOC d ";
      }
         
      if (filters.get("package_name")!=null)
         sql += ", CLASSTYPE c, PACKAGE p ";

      sql += " where ";

      List whereClause = new ArrayList(10);
      if (filters.get("searchField")!=null)
      {
         String selectby = "m.qualifiedname";
         if (!JDocUtil.isQualified((String) filters.get("searchField")))
            selectby = "m.name";
         whereClause.add("lower("+selectby+") like ?");
      }
      if (filters.get("member_type")!=null)
         whereClause.add("m.type=?");

      if (filters.get("static")!=null)
         whereClause.add("m.isstatic=?");
      if (filters.get("final")!=null)
         whereClause.add("m.isfinal=?");

      if (filters.get("deprecated")!=null)
      {
         if ( ((Integer)filters.get("deprecated")).intValue() == 1)
            whereClause.add("d.deprecated != ''");
         else
            whereClause.add("d.deprecated = ''");
      }

      if (filters.get("abstract")!=null)
         whereClause.add("meth.isabstract=?");
      if (filters.get("synchronized")!=null)
         whereClause.add("em.issynchronized=?");
      if (filters.get("native")!=null)
         whereClause.add("em.isnative=?");
      if (filters.get("package_name")!=null)
         whereClause.add("m.classid=c.id and p.id=c.packageid and lower(p.name) like ?");
      whereClause.add("m.docid=d.id");

      if (DBMgr.getInstance().getDbtype().equals("oracle"))  // for oracle outer join
      {
         whereClause.add(" m.id=em.id (+) ");
         whereClause.add(" m.id=meth.id (+) ");
      }

      sql += StringUtils.join(whereClause.toArray(), " and ");

      if (DBMgr.getInstance().getDbtype().equals("oracle"))
         sql += "  and rownum<50 ";

      sql += " order by m.qualifiedname ";

      if (!DBMgr.getInstance().getDbtype().equals("oracle"))
         sql += " limit 50 ";


      PreparedStatement p = conn.prepareStatement(sql);
      int i=1;

      if (filters.get("searchField")!=null)
         p.setString(i++, (String) filters.get("searchField"));
      if (filters.get("member_type")!=null)
         p.setInt(i++, ((Integer) filters.get("member_type")).intValue());
      if (filters.get("static")!=null)
         p.setInt(i++, ((Integer) filters.get("static")).intValue());
      if (filters.get("final")!=null)
         p.setInt(i++, ((Integer) filters.get("final")).intValue());
      if (filters.get("abstract")!=null)
         p.setInt(i++, ((Integer) filters.get("abstract")).intValue());
      if (filters.get("synchronized")!=null)
         p.setInt(i++, ((Integer) filters.get("synchronized")).intValue());
      if (filters.get("native")!=null)
         p.setInt(i++, ((Integer) filters.get("native")).intValue());
      if (filters.get("package_name")!=null)
         p.setString(i++, (String) filters.get("package_name"));

      Logger.getInstance().debug("sql query for advanced search:\n\t"+sql);

      ResultSet rset = p.executeQuery();

      List found = new ArrayList();
      Member m;

      while (rset.next())
      {
        m = new Member(rset.getString(2), rset.getInt(3));
        m.setId(rset.getInt(1));
        m.setDoc(new DocInfo(rset.getString(13), rset.getString(14), rset.getString(15)));
        m.setStatic(rset.getBoolean(4));
        m.setFinal(rset.getBoolean(5));
        m.setAccessibility(rset.getInt(6));
        m.setModifiers(rset.getString(7));
        String signature = rset.getString(12);
        if (!StringUtils.isBlank(signature))
        {
           ExecMember em = new ExecMember(m, signature);
           String returntypename = rset.getString(10);
           if (!StringUtils.isBlank(returntypename))
           {
               MethodMember method = new MethodMember(m, signature);
               method.setAbstract(rset.getBoolean(8));
               method.setReturnTypeName(returntypename);
               method.setReturnTypeDimension(rset.getInt(11));
               int returntypeid = rset.getInt(9);
               if (returntypeid > 0)
               {
                  method.setReturnType(new ClassType(returntypename));
                  method.getReturnType().setId(returntypeid);
               }
              found.add(method);
           }
           else
           {
              found.add(em);
           }
        }
        else
        {
           found.add(m);
        }

      }  // end while

      return found;
   }
      
   
   public List getSimpleMembers(String searchField) throws SQLException
   {
      searchField = searchField.toLowerCase();
      String selectby = "m.qualifiedname";
      if (!JDocUtil.isQualified(searchField))
      {
         selectby = "m.name";
      }
      
      Perl5Util util = new Perl5Util();
      String pattern = "s/\\*/%/g";
      searchField = util.substitute(pattern, searchField);
      //searchField = searchField.replaceAll("\\*", "%");

      
      String sql =
            " select m.id, m.qualifiedname, m.type, " +
            "  m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
            "  meth.isabstract, meth.returntypeid, meth.returntypename, meth.returntypedimension, " +
            "  em.signature, " +
            "  d.summarydescription, d.since, d.deprecated " +
            " from METHOD meth right outer join MEMBER m on meth.id=m.id " +
            "  left outer join EXECMEMBER em on em.id=m.id, DOC d " +
            " where lower("+selectby+") like ? and m.docid=d.id  " +
            " order by m.qualifiedname limit 50 ";
      
      if (DBMgr.getInstance().getDbtype().equals("oracle"))
      {
         sql =
         "select m.id, m.qualifiedname, m.type, " +
         "       m.isstatic, m.isfinal, m.accessibility, m.modifier, " +
         "       meth.isabstract, meth.returntypeid, meth.returntypename, meth.returntypedimension, " +
         "       em.signature, " + 
         "       d.summarydescription, d.since, d.deprecated " +
         " from MEMBER m, DOC d, METHOD meth, EXECMEMBER em " + 
         " where lower("+selectby+") like ? " + 
         "    and m.docid = d.id " + 
         "    and m.id = meth.id (+) " +
         "    and m.id = em.id (+) " +
         "  and rownum<50 " + 
         " order by m.qualifiedname";
      }
      
      
      PreparedStatement p = conn.prepareStatement(sql);
      p.setString(1, searchField);
      ResultSet rset = p.executeQuery();

      List found = new ArrayList();
      Member m;
      
      while (rset.next())
      {
        m = new Member(rset.getString(2), rset.getInt(3));
        m.setId(rset.getInt(1));
        m.setDoc(new DocInfo(rset.getString(13), rset.getString(14), rset.getString(15)));
        m.setStatic(rset.getBoolean(4));
        m.setFinal(rset.getBoolean(5));
        m.setAccessibility(rset.getInt(6));
        m.setModifiers(rset.getString(7));
        String signature = rset.getString(12);
        if (!StringUtils.isBlank(signature))
        {
           ExecMember em = new ExecMember(m, signature);
           String returntypename = rset.getString(10);
           if (!StringUtils.isBlank(returntypename))
           {
               MethodMember method = new MethodMember(m, signature);
               method.setAbstract(rset.getBoolean(8));
               method.setReturnTypeName(returntypename);
               method.setReturnTypeDimension(rset.getInt(11));
               int returntypeid = rset.getInt(9);
               if (returntypeid > 0)
               {
                  method.setReturnType(new ClassType(returntypename));
                  method.getReturnType().setId(returntypeid);
               }
              found.add(method);
           }
           else
           {
              found.add(em);
           }
        }
        else
        {
           found.add(m);
        }
        
      }  // end while
      
      return found;
   }
   
   
}
