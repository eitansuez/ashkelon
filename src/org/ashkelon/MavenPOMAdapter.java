/*
 * Created on Aug 16, 2004
 */
package org.ashkelon;

import java.io.*;
import org.dom4j.*;
import org.dom4j.io.*;

import java.text.ParseException;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * @author Eitan Suez
 */
public class MavenPOMAdapter
{
   
   public static API read(File file, String sourcepath) throws ParseException
   {
      // pseudocode:  read xml file
      // here's the mapping: (use dom4j, do it manually, easy enough)
      //   name: project/name
      //   summaryDescription: project/shortDescription
      //   description: project/description
      //   publisher: project/organization/name
      //   downloadUrl: project/url
      //   version: project/currentVersion
      //   releaseDate (approximated as): project/inceptionYear
      
      try
      {
         SAXReader reader = new SAXReader();
         Document doc = reader.read(file);
         API api = new API();
         api.setName(xpath(doc, "//project/name"));
         api.setSummaryDescription(xpath(doc, "//project/shortDescription"));
         api.setDescription(xpath(doc, "//project/description"));
         api.setPublisher(xpath(doc, "//project/organization/name"));
         api.setDownloadURL(xpath(doc, "//project/url"));
         api.setVersion(xpath(doc, "//project/currentVersion"));
         
         String year = xpath(doc, "//project/inceptionYear");
         Calendar cal = Calendar.getInstance();
         cal.set(Calendar.YEAR, Integer.parseInt(year));
         api.setReleaseDate(cal.getTime());
         
         // now need to resolve and add the package names
         String rootPkgName = xpath(doc, "//project/package");
         String[] packageNames = getPackageNamesFromSourcePath(rootPkgName, sourcepath);
         for (int i=0; i<packageNames.length; i++)
         {
            api.addPackagename(packageNames[i]);
         }
         
         return api;
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         throw new ParseException(
               "Failed to parse api file as a maven pom-formatted file", 0);
      }
   }
   
   private static String xpath(Document doc, String expression)
   {
      Element element = (Element) doc.selectSingleNode(expression);
      return element.getTextTrim();
   }
   
   public static String[] 
            getPackageNamesFromSourcePath(String rootPackageName, String sourcepath)
   {
      Set packages = new HashSet();
      Path path = new Path(new Project(), sourcepath);
      String[] pathParts = path.list();
      for (int i=0; i<pathParts.length; i++)
      {
         String[] packagenames = getPackageNames(rootPackageName, pathParts[i]);
         for (int j=0; j<packagenames.length; j++)
         {
            packages.add(packagenames[j]);
         }
      }
      return (String[]) packages.toArray(new String[packages.size()]);
   }

   public static String[] getPackageNames(String rootPackageName, String baseDir)
   {
      String input = rootPackageName.replace('.', '/') + "/**";
      DirectoryScanner scanner = new DirectoryScanner();
      scanner.setIncludes(new String[] {input});
      
      scanner.setBasedir(baseDir);
      scanner.scan();
      
      String[] dirs = scanner.getIncludedDirectories();
      List returnList = new ArrayList(dirs.length);
      for (int i=0; i<dirs.length; i++)
      {
         if (dirs[i].endsWith("/CVS")) continue;
          returnList.add(dirs[i].replace('/', '.'));
      }
      return (String[]) returnList.toArray(new String[returnList.size()]);
   }
}
