/*
 * Created on Aug 17, 2004
 */
package org.ashkelon.ant;

import org.apache.tools.ant.*;
import java.util.*;

/**
 * @author Eitan Suez
 */
public class SimpleAntTest
{
   public static void main(String[] args)
   {
      String rootPkg = "org.ashkelon";
      String sourcePath = "/Users/eitan/devel/sources";
      
      String[] packageNames = getPackageNames(rootPkg, sourcePath);
      
      for (int i=0; i<packageNames.length; i++)
      {
         System.out.println(packageNames[i]);
      }
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
         if (dirs[i].endsWith("CVS")) continue;
          returnList.add(dirs[i].replace('/', '.'));
      }
      return (String[]) returnList.toArray(new String[returnList.size()]);
   }
}
