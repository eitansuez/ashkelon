package org.ashkelon.taglibs;

import org.ashkelon.ClassType;
import org.ashkelon.JPackage;
import org.ashkelon.Member;
import org.ashkelon.API;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 4, 2005
 * Time: 12:12:14 AM
 */
public interface LinkStrategy
{
   public String apiUrl(API api, String fromPath);
   public String pkgUrl(JPackage pkg, String fromPath);
   public String clsUrl(ClassType cls, String fromPath);
   public String srcUrl(ClassType cls, String fromPath);
   public String memberUrl(Member member, String fromPath);
   public String staticRef(String to, String fromPath);
}
