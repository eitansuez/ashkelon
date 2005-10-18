package org.ashkelon.taglibs;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 12:22:53 AM
 */
public class JSRefTag extends U2DTag
{
   private String ref;

   public int doStartTag()
         throws JspException
   {
      try
      {
         pageContext.getOut().print(
               "<script type=\"text/javascript\" language=\"JavaScript\" " +
               "src=\"" + processStaticRef(ref) + "\">");
      }
      catch (IOException ex)
      {
         throw new JspException("JSRefTag: "+ex.getMessage());
      }
      return SKIP_BODY;
   }

   public int doEndTag()
         throws JspException
   {
      try
      {
         pageContext.getOut().print("</script>");
      }
      catch (IOException ex)
      {
         throw new JspException("JSRefTag: "+ex.getMessage());
      }
      return EVAL_PAGE;
   }

   public String getRef() { return ref; }
   public void setRef(String ref) { this.ref = ref; }
}
