package org.ashkelon;

import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import org.ashkelon.util.*;
import org.ashkelon.db.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;

public class API implements Serializable
{
    private String name;
    private String summaryDescription;
    private String description;
    private String publisher;
    private String downloadURL;
    private java.util.Date releaseDate;
    private String version;
    private Collection packagenames;
    
    private List packages;

    private static String SEQUENCE = "API_SEQ";
    private static String TABLENAME = "API";
   
    private int id;
    private boolean idSet = false;
   
    private transient Logger log;
    private Unmarshaller ums = null;
    
    
    public API()
    {
       log = Logger.getInstance();
       setPackagenames(new ArrayList());
       setPackages(new ArrayList());

       try
       {
          ClassLoader loader = this.getClass().getClassLoader();
          URL resource = loader.getResource("org/ashkelon/apimapping.xml");
          Mapping mapping = new Mapping();
          mapping.loadMapping(resource);

          ums = new Unmarshaller(API.class);
          ums.setMapping(mapping);
       }
       catch (Exception ex)
       {
           System.err.println("exception: "+ex.getMessage());
           ex.printStackTrace();
       }
    }
    
    public API(String name)
    {
       this();
       setName(name);
    }
    
    public API load(Reader reader) throws MarshalException, ValidationException
    {
      return (API) ums.unmarshal(reader);
    }
    
   public int getId(Connection conn) throws SQLException
   {
      if (!idSet)
         setId(PKManager.getInstance().nextVal(SEQUENCE));
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
      String prefix = log.getPrefix();
      log.setPrefix("API");
      if (exists(conn))
      {
         log.traceln("Skipping API " + getName() + " (already in repository)");
         return;
      }

      Map fieldInfo = new HashMap(5);
      fieldInfo.put("ID", new Integer(getId(conn)));
      fieldInfo.put("NAME", StringUtils.truncate(name, 60));
      fieldInfo.put("SUMMARYDESCRIPTION", StringUtils.truncate(summaryDescription, 250));
      fieldInfo.put("PUBLISHER", StringUtils.truncate(publisher, 150));
      fieldInfo.put("DOWNLOAD_URL", StringUtils.truncate(downloadURL, 150));
      fieldInfo.put("RELEASE_DATE", new java.sql.Date(releaseDate.getTime()));
      fieldInfo.put("VERSION", StringUtils.truncate(version, 30));
      fieldInfo.put("DESCRIPTION", description);

      try
      {
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
      catch (SQLException ex)
      {
         fieldInfo.put("DESCRIPTION", StringUtils.truncate(description, 3600));
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }


      Iterator itr = getPackagenames().iterator();
      String packagename = null;
      String sql = "update PACKAGE set api_id=? where name=?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      while (itr.hasNext())
      {
         packagename = (String) itr.next();
         pstmt.setString(2, packagename);
         pstmt.executeUpdate();
      }
      pstmt.close();
      log.setPrefix(prefix);
   }
   
   public boolean exists(Connection conn) throws SQLException
   {
      Map constraints = new HashMap();
      constraints.put("NAME", getName());
      constraints.put("VERSION", getVersion());
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
   
   public boolean delete(Connection conn) throws SQLException
   {
      Iterator itr = packagenames.iterator();
      while (itr.hasNext())
      {
         JPackage.delete(conn, (String) itr.next());
      }

      // delete self
      HashMap constraint = new HashMap();
      constraint.put("NAME", name);
      DBUtils.delete(conn, TABLENAME, constraint);

      return true;
   }

   
   
   
    // accessors
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSummaryDescription() { return summaryDescription; }
    public void setSummaryDescription(String summaryDescription) { this.summaryDescription = summaryDescription; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getDownloadURL() { return downloadURL; }
    public void setDownloadURL(String downloadURL) { this.downloadURL = downloadURL; }

    public java.util.Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(java.util.Date releaseDate) { this.releaseDate = releaseDate; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public Collection getPackagenames() { return packagenames; }
    public void setPackagenames(Collection packagenames) { this.packagenames = packagenames; }
    
    public List getPackages() { return packages; }
    public void setPackages(List packages) { this.packages = packages; }
    
    public void addPackage(JPackage pkg)
    {
       packages.add(pkg);
    }
    
    public String toString()
    {
       return getName() + " v" + getVersion();
    }
    
   public static API makeAPIFor(Connection conn, int apiId) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("makeapi");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, apiId);
      ResultSet rset = pstmt.executeQuery();
      
      if (!rset.next())
         return null;
      
      API api = new API(rset.getString(2));
      api.setId(rset.getInt(1));
      api.setSummaryDescription(rset.getString(3));
      api.setDescription(rset.getString(4));
      api.setPublisher(rset.getString(5));
      api.setDownloadURL(rset.getString(6));
      api.setReleaseDate(rset.getDate(7));
      api.setVersion(rset.getString(8));
      
      rset.close();
      pstmt.close();
      
      api.getPackageInfo(conn);
      
      return api;
   }
   
   public void getPackageInfo(Connection conn) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("packageinfo");
      /*
       select p.id, p.name, d.summarydescription 
        from package p, doc d 
        where p.api_id=? and p.docid=d.id 
        order by p.name
       */
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, getId());
      ResultSet rset = pstmt.executeQuery();
      
      JPackage pkg;
      while (rset.next())
      {
         pkg = new JPackage(rset.getString(2));
         pkg.setId(rset.getInt(1));
         DocInfo doc = new DocInfo();
         doc.setSummaryDescription(rset.getString(3));
         pkg.setDoc(doc);
         
         addPackage(pkg);
      }
      
      rset.close();
      pstmt.close();
   }
   

   public static String getTableName()
   {
     return TABLENAME;
   }
   
}
