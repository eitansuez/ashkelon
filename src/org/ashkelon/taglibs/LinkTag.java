package org.ashkelon.taglibs;

import org.ashkelon.ClassType;
import org.ashkelon.JPackage;
import org.ashkelon.Member;
import org.ashkelon.API;
import org.ashkelon.util.StringUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 3, 2005
 * Time: 11:54:18 PM
 */
public class LinkTag extends U2DTag
{
   private String _to;
   private Object _elem;
   private String _title;
   private String _by;

   public LinkTag() {}

   public int doStartTag()
         throws JspException
   {
      try
      {
         String titleAttribute = (StringUtils.isBlank(_title))
               ? ""
               : "title=\"" + _title + "\"";

         pageContext.getOut().print(
               "<a href=\"" +
               url(_elem) + "\"" +
               titleAttribute +
               ">");
      }
      catch (IOException ex)
      {
         throw new JspException("LinkTag: "+ex.getMessage());
      }
      return EVAL_BODY_INCLUDE;
   }

   private String url(Object elem)
   {
      String fromPath = ((HttpServletRequest) pageContext.getRequest()).getRequestURI();
      if ("api".equals(_to))
      {
         return strategy().apiUrl((API) elem, fromPath);
      }
      else if ("package".equals(_to))
      {
         return strategy().pkgUrl((JPackage) elem, fromPath);
      }
      else if ("class".equals(_to))
      {
         return strategy().clsUrl((ClassType) elem, fromPath);
      }
      else if ("source".equals(_to))
      {
         return strategy().srcUrl((ClassType) elem, fromPath);
      }
      else if ("member".equals(_to))
      {
         return strategy().memberUrl((Member) elem, fromPath);
      }
      else
      {
         return "";
      }
   }

   public int doEndTag()
         throws JspException
   {
      try
      {
         pageContext.getOut().print("</a>");
      }
      catch (IOException ex)
      {
         throw new JspException("LinkTag: "+ex.getMessage());
      }
      return EVAL_PAGE;
   }


   public Object getElem() { return _elem; }
   public void setElem(Object elem) { _elem = elem; }

   public String getTo() { return _to; }
   public void setTo(String to) { _to = to; }

   public String getTitle() { return _title; }
   public void setTitle(String title) { _title = title; }
   
   public String getBy() { return _by; }
   public void setBy(String by) { _by = by; }
   
}
