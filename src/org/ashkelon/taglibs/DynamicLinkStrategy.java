package org.ashkelon.taglibs;

import org.ashkelon.*;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 4, 2005
 * Time: 12:12:23 AM
 */
public class DynamicLinkStrategy implements LinkStrategy
{
   public String apiUrl(API api, String fromPath) { return href(api, "main"); }
   public String pkgUrl(JPackage pkg, String fromPath) { return href(pkg, "main"); }
   public String clsUrl(ClassType cls, String fromPath) { return href(cls, "main"); }
   public String memberUrl(Member member, String fromPath) { return href(member, "main"); }
   public String srcUrl(ClassType cls, String fromPath)
   {
      return base(cls, "source") + "?name=" + cls.getQualifiedName();
   }
   public String staticRef(String to, String fromPath) { return to; }

   // in truth it can't all be generalized like this
   // e.g. source is always by name, not by id
   private String href(Persistable type, String pageType)
   {
     return base(type, pageType) + 
            ((type.isResolved()) ? "?id=" + type.getId()
                                 : "?name=" + type.getQualifiedName());
   }
   private String base(Persistable type, String pageType)
   {
      return type.key() + "." + pageType + ".do";
   }
   
   public String toString() { return "Dynamic Link Strategy"; }
}
