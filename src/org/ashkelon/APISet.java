/*
 * Created on Mar 30, 2005
 */
package org.ashkelon;

import java.sql.*;
import java.util.*;
import org.ashkelon.db.DBMgr;
import org.ashkelon.util.*;
import org.jibx.runtime.*;

/**
 * @author Eitan Suez
 */
public class APISet
{
   private ArrayList _apis = new ArrayList();
   private Logger log = Logger.getInstance();
   
   public APISet() {}
   
   public void dump(Connection conn) throws SQLException, JiBXException
   {
      getAPIList(conn);
      
      IBindingFactory bfact = BindingDirectory.getFactory(APISet.class);
      IMarshallingContext ctxt = bfact.createMarshallingContext();
      ctxt.setIndent(3);
      ctxt.marshalDocument(this, "UTF-8", null, System.out);
   }
    
   
   // export means:
   //  fetch apis from db
   //  add them to the apis list and marshal this guy out to xml
   

   public void getAPIList(Connection conn) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("exportapiset");

      Statement stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      
      API api;
      while (rset.next())
      {
         api = new API(rset.getString(1));
         api.setSummaryDescription(rset.getString(2));
         api.setDescription(rset.getString(3));
         api.setPublisher(rset.getString(4));
         api.setDownloadURL(rset.getString(5));
         api.setReleaseDate(rset.getDate(6));
         api.setVersion(rset.getString(7));
         
         String pkgnames = rset.getString(8);
         String[] pkgs = StringUtils.split(pkgnames, " ");
         api.setPackagenames(Arrays.asList(pkgs));
         
         String type = rset.getString(9);
         String url = rset.getString(10);
         String module = rset.getString(11);
         String tag = rset.getString(12);
         String srcpath = rset.getString(13);
         Repository repos = new Repository(type, url, module, tag, srcpath);
         
         if (repos.isSpecified())
         {
            api.setRepository(repos);
         }
         else
         {
            api.setRepository(null);
         }
         _apis.add(api);
      }
      
      rset.close();
      stmt.close();
   }
   
   
}
