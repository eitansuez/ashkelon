package org.ashkelon.taglibs;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 12:39:51 AM
 */
public class U2DTag extends TagSupport
{
   private LinkStrategy _strategy;
   protected LinkStrategy strategy()
   {
      if (_strategy == null)
      {
         _strategy = (LinkStrategy)
                     pageContext.getServletContext().
                     getAttribute("link-strategy");
      }
      return _strategy;
   }

   protected String processStaticRef(String ref)
   {
      String frompath = ((HttpServletRequest) pageContext.getRequest()).getRequestURI();
      return strategy().staticRef(ref, frompath);
   }

}
