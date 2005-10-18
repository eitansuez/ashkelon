package org.ashkelon.manager.staticdoclet;

import org.ashkelon.JPackage;
import org.ashkelon.ClassType;
import org.ashkelon.Member;
import org.ashkelon.API;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 5, 2005
 * Time: 10:41:25 PM
 */
public class FileNaming
{
   File _baseDir;

   public FileNaming(File baseDir)
   {
      _baseDir = baseDir;
   }

   public String apiFilename(API api) { return filename(apiURI(api)); }
   public String apiURI(API api) { return api.getName() + ".html"; }


   public String pkgFilename(JPackage pkg) { return filename(pkgURI(pkg)); }
   public String pkgURI(JPackage pkg)
   {
      String path = pkg.getName().replace('.', File.separatorChar);
      return combine(path, "package.html");
   }


   public String clsFilename(ClassType cls) { return filename(clsURI(cls)); }
   public String clsURI(ClassType cls)
   {
      String pkgpath = cls.getPackage().getName().replace('.', File.separatorChar);
      return combine(pkgpath, cls.getName() + ".html");
   }

   public String memberFilename(Member member) { return filename(memberURI(member)); }
   public String memberURI(Member member)
   {
      String clsQname = member.getContainingClass().getQualifiedName();
      String clspath = clsQname.replace('.', File.separatorChar);
      if (member.getMemberType() == Member.CONSTRUCTOR_MEMBER)
         return combine(clspath, "constructor.html");
      return combine(clspath, member.getName() + ".html");
   }

   public String sourceFilename(ClassType cls) { return filename(sourceURI(cls)); }
   public String sourceURI(ClassType cls)
   {
      String clsQname = cls.getQualifiedName();
      String clspath = clsQname.replace('.', File.separatorChar);
      return combine(clspath, "source.html");
   }

   public String sourceFilenameWrapped(ClassType cls)
   {
      return filename(sourceWrappedURI(cls));
   }
   public String sourceWrappedURI(ClassType cls)
   {
      String clsQname = cls.getQualifiedName();
      String clspath = clsQname.replace('.', File.separatorChar);
      return combine(clspath, "source_wrap.html");
   }
   
   private String filename(String uri)
   {
      String filename = combine(_baseDir.getAbsolutePath(), uri);
      new File(filename).getParentFile().mkdirs();
      return filename;
   }

   public static String combine(String path, String filename)
   {
      return path + File.separator + filename;
   }
}
