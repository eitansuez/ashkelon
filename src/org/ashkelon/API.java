package org.ashkelon;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.ashkelon.db.*;
import org.ashkelon.manager.*;
import org.ashkelon.util.*;
import org.jibx.runtime.*;

/**
 * Represents a Java API (a collection of related packages)
 *
 * @author Eitan Suez
 */
public class API implements JDoc, Serializable
{
    private String name = "";
    private String summaryDescription = "";
    private String description = "";
    private String publisher = "";
    private String downloadURL = "";
    private java.util.Date releaseDate = new java.util.Date();
    private String version = "";
    private ArrayList packagenames = new ArrayList();
    private Repository repository = new Repository();
    private boolean populated = false;
    
    private List packages;

    private static String SEQUENCE = "API_SEQ";
    private static String TABLENAME = "API";
   
    private int id;
    private boolean idSet = false;
   
    private transient Logger log;

    
    public API()
    {
       log = Logger.getInstance();
       setPackages(new ArrayList());
    }
    
    public API(String name)
    {
       this();
       setName(name);
    }
    
    public static API unmarshal(String filename, String sourcepath) 
      throws FileNotFoundException, java.text.ParseException
    {
       try
       {
          return unmarshal(new FileReader(filename));
       }
       catch (JiBXException ex)
       {
          // see if xml file matches maven POM format..
          System.out.println("let's see if file matches maven pom format..");
          return MavenPOMAdapter.read(new File(filename), sourcepath);
       }
    }
    
    public static API unmarshal(Reader reader) throws JiBXException
    {
       IBindingFactory bfact = BindingDirectory.getFactory(API.class);
       IUnmarshallingContext umctxt = bfact.createUnmarshallingContext();
       return (API) umctxt.unmarshalDocument(reader);
    }
    
    private API readMavenPOM(Reader reader)
    {
       return null;
    }
    
   public int getId(Connection conn) throws SQLException
   {
      if (!idSet)
      {
         PKManager pkmgr = PKManager.getInstance();
         int nextVal = pkmgr.nextVal(SEQUENCE);
         setId(nextVal);
      }
      return id;
   }
   
   public int getId() { return id; }
   
   public void setId(int id)
   {
      this.id = id;
      idSet = true;
   }

   public void update(Connection conn) throws SQLException
   {
      String updateSql = 
      "update " + TABLENAME + " set NAME=?, SUMMARYDESCRIPTION=?, " +
      " PUBLISHER=?, DOWNLOAD_URL=?, RELEASE_DATE=?, VERSION=?, " + 
      " DESCRIPTION=?, POPULATED=?, REPOSITORY_TYPE=?, REPOSITORY_URL=?, " + 
      " REPOSITORY_MODULE=?, REPOSITORY_TAGNAME=?, REPOSITORY_SRCPATH=?, " +
      " PACKAGENAMES=? where ID=?";
      
      PreparedStatement pstmt = conn.prepareStatement(updateSql);
      pstmt.setString(1, name);
      pstmt.setString(2, summaryDescription);
      pstmt.setString(3, publisher);
      pstmt.setString(4, downloadURL);
      pstmt.setDate(5, new java.sql.Date(releaseDate.getTime()));
      pstmt.setString(6, version);
      pstmt.setString(7, description);
      pstmt.setInt(8, populatedVal().intValue());
      pstmt.setString(9, repository.getType());
      pstmt.setString(10, repository.getUrl());
      pstmt.setString(11, repository.getModulename());
      pstmt.setString(12, repository.getTagname());
      pstmt.setString(13, repository.getSourcepath());
      pstmt.setString(14, StringUtils.join(packagenames.toArray(), " "));
      
      pstmt.setInt(15, id);
      
      pstmt.executeUpdate();
      pstmt.close();
      
   }
   
   public void store(Connection conn) throws SQLException
   {
      if (exists(conn))
         update(conn);
      else
         insert(conn);
      
      if (!populated)
         state = SINGLE_STATE;
   }
   
   public void insert(Connection conn) throws SQLException
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
      fieldInfo.put("POPULATED", populatedVal());
      fieldInfo.put("REPOSITORY_TYPE", repository.getType());
      fieldInfo.put("REPOSITORY_URL", repository.getUrl());
      fieldInfo.put("REPOSITORY_MODULE", repository.getModulename());
      fieldInfo.put("REPOSITORY_TAGNAME", repository.getTagname());
      fieldInfo.put("REPOSITORY_SRCPATH", repository.getSourcepath());
      
      String packagenamelist = StringUtils.join(getPackagenames().toArray(), " ");
      fieldInfo.put("PACKAGENAMES", packagenamelist);

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
//      constraints.put("VERSION", getVersion());
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
   public boolean existsPopulated(Connection conn) throws SQLException
   {
      String sql = "select count(*) from api where name=? and populated=1";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, name);
      ResultSet rset = pstmt.executeQuery();
      rset.next();
      int count = rset.getInt(1);
      return count > 0;
   }
   
   public boolean delete(Connection conn) throws SQLException
   {
      return delete(conn, false); // default - leave api record intact
   }
   
   public boolean delete(Connection conn, boolean withself) throws SQLException
   {
      getPackageInfoByAPIName(conn);
      Iterator itr = packages.iterator();
      JPackage pkg = null;
      log.traceln("(num packages to delete: "+packages.size()+")");
      while (itr.hasNext())
      {
         pkg = (JPackage) itr.next();
         log.traceln("deleting package "+pkg.getName());
         JPackage.delete(conn, pkg.getName());
      }

      if (withself)
      {
         //delete self
         HashMap constraint = new HashMap();
         constraint.put("NAME", name);
         DBUtils.delete(conn, TABLENAME, constraint);
      }
      else
      {
         setPopulated(false);
         String sql = "update API set populated=0 where name=?";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, getName());
         pstmt.executeUpdate();
         pstmt.close();
      }

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
    public void setPackagenames(Collection packagenames)
    {
       this.packagenames = new ArrayList();
       this.packagenames.addAll(packagenames);
    }
    
    public List getPackages() { return packages; }
    public void setPackages(List packages) { this.packages = packages; }
    
    public void addPackage(JPackage pkg)
    {
       packages.add(pkg);
    }
    public void addPackagename(String packagename)
    {
       packagenames.add(packagename);
    }
    
    public boolean isPopulated() { return populated; }
    public void setPopulated(boolean populated)
    {
       this.populated = populated;
       if (populated) state = POPULATED_STATE;
    }
    public Integer populatedVal()
    {
       int val = (populated) ? 1 : 0;
       return new Integer(val);
    }
    public void setPopulated(int populatedVal)
    {
       this.populated = (populatedVal == 0) ? false : true;
       if (populated) state = POPULATED_STATE;
    }
    
    public Repository getRepository() { return repository; }
    public void setRepository(Repository repository)
    {
       this.repository = repository;
    }
    
    public String toString()
    {
       return getName() + " v" + getVersion();
    }
    
    public String longToString()
    {
       StringBuffer text = new StringBuffer(toString());
       text.append("; Packages: ");
       Iterator itr = packagenames.iterator();
       if (itr.hasNext())
          text.append((String) itr.next());
       while (itr.hasNext())
       {
          text.append(", ").append((String) itr.next());
       }
       return text.toString();
    }
    
   public static API makeAPIFor(Connection conn, String name) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("makeapibyname");
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, name);
      
      return makeIt(pstmt, conn);
   }
   public static API makeAPIFor(Connection conn, int apiId) throws SQLException
   {
      String sql = DBMgr.getInstance().getStatement("makeapi");

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, apiId);
      
      return makeIt(pstmt, conn);
   }
   
   private static API makeIt(PreparedStatement pstmt, Connection conn) throws SQLException
   {
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
      api.setPopulated(rset.getInt(9));
      
      Repository repository = new Repository(rset.getString(10),
            rset.getString(11), rset.getString(12), rset.getString(13), rset.getString(14));
      api.setRepository(repository);
      
      String packagenamesString = rset.getString(15);
      String[] packageNames = StringUtils.split(packagenamesString, " ");
      api.setPackagenames(Arrays.asList(packageNames));
      
      rset.close();
      pstmt.close();
      
      api.getPackageInfo(conn);
      
      if (!api.isPopulated())
         api.state = SINGLE_STATE;
      
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
   
   public void getPackageInfoByAPIName(Connection conn) throws SQLException
   {
      packages = new ArrayList();
      String sql = DBMgr.getInstance().getStatement("packageinfobyname");
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, getName());
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
   
   public static String getTableName() { return TABLENAME; }
   
   public DocInfo getDoc() { return null; }  // this is crummy.  TODO: fix
   public String getStyle() { return "api"; }  // maybe getstyle should have a separate interface
   
   
   public void fetch(File base) { repository.fetch(base); }
   public String sourcepath() { return repository.sourcepath(); }
   
   
   public APIState state = TRANSIENT_STATE;

   public static APIState TRANSIENT_STATE = new TransientAPIState();
   public static APIState SINGLE_STATE = new SingleAPIState();
   public static APIState EXPECTING_STATE = new ExpectingAPIState();
   public static APIState POPULATED_STATE = new PopulatedAPIState();
   
   public Set getActions()
   {
      return state.getActions(this);
   }
   
   static class TransientAPIState implements APIState
   {
      public Set getActions(API api)
      {
         return new HashSet();
      }
}
   static class SingleAPIState implements APIState
   {
      public Set getActions(API api)
      {
         Set actions = new HashSet();
//         Action action = new Action("Test Source Repository", "api.testrepos.do");
//         actions.add(action);
//         action = new Action("Process API Docs", "api.populate.do");
//         actions.add(action);
         return actions;
      }
}
   static class ExpectingAPIState implements APIState
   {
      public Set getActions(API api)
      {
         return new HashSet();
      }
   }
   static class PopulatedAPIState implements APIState
   {
      public Set getActions(API api)
      {
         Set actions = new HashSet();
         Action action = new Action("Go to API Doc Page", "api.main.do?id="+api.getId());
         actions.add(action);
         return actions;
      }
   }
   
}
