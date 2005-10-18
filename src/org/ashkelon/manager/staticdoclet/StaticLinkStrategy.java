package org.ashkelon.manager.staticdoclet;

import org.ashkelon.*;
import org.ashkelon.util.PathComputer;
import org.ashkelon.taglibs.LinkStrategy;

/*

$apiname.html
images/
*.css
*.js
$pkgpath/
 package.html
 $classname.html
 $classname/
   constructor.html
   source.html
   source_wrap.html
   $membername.html

*/

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 4, 2005
 * Time: 12:25:37 AM
 */
public class StaticLinkStrategy implements LinkStrategy
{
   PathComputer pathComputer = new PathComputer();
   FileNaming naming = new FileNaming(null);
   
   public StaticLinkStrategy() {}

   public String apiUrl(API api, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, naming.apiURI(api));
   }

   public String clsUrl(ClassType cls, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, naming.clsURI(cls));
   }

   public String srcUrl(ClassType cls, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, naming.sourceWrappedURI(cls));
   }

   public String pkgUrl(JPackage pkg, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, naming.pkgURI(pkg));
   }

   public String memberUrl(Member member, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, naming.memberURI(member));
   }
   
   public String staticRef(String to, String fromPath)
   {
      return pathComputer.computeRelativePath(fromPath, to);
   }
   
   public String toString() { return "Static Link Strategy"; }
}

