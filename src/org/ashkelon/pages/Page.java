package org.ashkelon.pages;

import javax.servlet.*;
import javax.servlet.http.*;

import org.ashkelon.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;

import java.sql.*;
import java.util.*;

public abstract class Page
{
   Logger log;
   Connection conn;
   HttpServletRequest request;
   ServletContext app;
   HttpSession session;
   
   public Page()
   {
      log = Logger.getInstance();
      log.setPrefix("Page");
   }
   
   public Page(Connection conn, HttpServletRequest request)
   {
      this();
      setConnection(conn);
      setRequest(request);
      session = request.getSession();
   }
   
   /**
    * init method can optionally return the name of a valid page
    * which the flowcontroller will use to forward the request to
    * instead of the default associated page name.
    */
   public abstract String init() throws Exception;
   
   public void setConnection(Connection conn)
   {
      this.conn = conn;
   }
   public void setRequest(HttpServletRequest request)
   {
      this.request = request;
      this.session = request.getSession();
   }
   public void setApplication(ServletContext context)
   {
      this.app = context;
   }
   
   protected void finalize() throws Throwable
   {
      DBMgr.getInstance().releaseConnection(conn);
      conn = null;
      request = null;
      log.debug("page finalized");
      log = null;
   }
 
   
   public static String printTree(TreeNode node, String pkgname)
   {
      Object o = node.getValue();
      if (o==null) return "";
      ClassType cls = null;
      String name = "";
      String indent = "";
      int level = 0;
      String cmd = "cls.main.do?cls_id=";
      String parent = "";
      String typename = "package";
      String modifiers = "";
      String descr;
      boolean nohref = false;
      if (o instanceof JPackage)
      {
         JPackage pkg = (JPackage) o;
         name = pkg.getName();
         descr = pkg.getSummaryDescription();
         cmd = "pkg.main.do?pkg_id="+pkg.getId();
      }
      else
      {
         cls = (ClassType) o;
         name = cls.getQualifiedName();
         descr = cls.getSummaryDescription();
         typename = cls.getClassTypeName();
         modifiers = cls.getModifiers();
         level =  cls.getLevel();
         indent = StringUtils.join(" ","",cls.getLevel()*3);
         cmd += cls.getId();
         parent = (level == 1) ? pkgname : cls.getSuperClassName();
         if (cls.getId()==0)
         {
            nohref = true;
         }
      }
      
      descr = HtmlUtils.cleanAttributeText(descr);
      
      String output = indent + "<UL ID=\""+parent+".child\">\n";
      
      String collapsible = node.isEmpty() ? "CLASS=\"leafnode\"" : "CLASS=\"collapsible\"";
      output += indent + "  <LI "+collapsible+" CHILD=\""+name+".child\">\n";
      if (nohref)  // unknown or unavailable
      {
         output += indent + "    <SPAN CLASS=\""+typename+" "+modifiers+"\">" + name + "</SPAN>\n";
         output += indent + "  </LI>\n";
      }
      else
      {
         output += indent + "    <A HREF=\""+cmd+"\"><SPAN CLASS=\""+typename+" "+modifiers+"\" TITLE=\""+descr+"\">" + name + "</SPAN></A>\n";
         output += indent + "  </LI>\n";
      }
      
      Map children = (Map) node.getChildren();
      Collection values = children.values();
      if (values != null)
      {
         Iterator itr = values.iterator();
         TreeNode childNode;
         while (itr.hasNext())
         {
            childNode = (TreeNode) itr.next();
            if (childNode != null)
               output += printTree(childNode, pkgname);
         }
      }
      
      output += indent + "</UL>\n";
      return output;
   }
   
}

