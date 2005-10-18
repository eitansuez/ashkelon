package org.ashkelon.taglibs;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 12:22:47 AM
 */
public class CSSRefTag extends U2DTag
{
   private String ref;

   public int doStartTag()
         throws JspException
   {
      try
      {
         pageContext.getOut().print(
               "<link rel=\"stylesheet\" type=\"text/css\" " +
               "href=\"" + processStaticRef(ref) + "\">");
      }
      catch (IOException ex)
      {
         throw new JspException("CSSRefTag: "+ex.getMessage());
      }

      return SKIP_BODY;
   }

   public int doEndTag() throws JspException { return EVAL_PAGE; }

   public String getRef() { return ref; }
   public void setRef(String ref) { this.ref = ref; }
}
