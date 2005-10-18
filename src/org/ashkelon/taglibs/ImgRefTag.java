package org.ashkelon.taglibs;

import org.ashkelon.util.StringUtils;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 12:22:47 AM
 */
public class ImgRefTag extends U2DTag
{
   private String ref;
   private String border;

   public int doStartTag()
         throws JspException
   {
      try
      {
         pageContext.getOut().print(
               "<img src=\"" + processStaticRef(ref) + "\"" + 
                     border() + ">");
      }
      catch (IOException ex)
      {
         throw new JspException("ImgRefTag: "+ex.getMessage());
      }

      return SKIP_BODY;
   }
   
   private String border()
   {
      if (StringUtils.isBlank(border)) return "";
      return " border=\"" + border + "\" ";
   }
   
   public int doEndTag() throws JspException { return EVAL_PAGE; }

   public String getRef() { return ref; }
   public void setRef(String ref) { this.ref = ref; }

   public String getBorder() { return border; }
   public void setBorder(String border) { this.border = border; }
}
