/*
 * Created on Jul 24, 2004
 */
package org.ashkelon.manager;

import org.ashkelon.DocInfo;
import org.ashkelon.Reference;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

/**
 * @author Eitan Suez
 */
public class AshkelonTagResolver implements InlineTagResolver
{
   public AshkelonTagResolver() {}

   /**
    * resolves all inline tags in a description into html links
    * @param tags text represented as an array of tags, as returned by doc.inlineTags()
    * @return resolved text
    */
   public String resolveDescription(DocInfo sourcedoc, Tag[] tags)
   {
      Logger log = Logger.getInstance();
      
      StringBuffer text = new StringBuffer("");
      for (int i=0; i<tags.length; i++)
      {
         log.debug("tag 'kind': "+tags[i].kind());
         
         // thanks Matt for catching this:
         if ("@return".equals(tags[i].kind()) && (tags[i].inlineTags() != null && tags[i].inlineTags().length > 0))
         {
             return resolveDescription(sourcedoc, tags[i].inlineTags());
         }

         if ("@see".equals(tags[i].kind()))  // javadoc docs say 'link' - liars
         {
            Reference ref = new Reference(sourcedoc, (SeeTag) tags[i]);
            String cmd = "";
            if (ref.getRefDocType() == DocInfo.PACKAGE_TYPE)
            {
               cmd = "pkg";
            }
            else if (ref.getRefDocType() == DocInfo.CLASS_TYPE)
            {
               cmd = "cls";
            }
            else if (ref.getRefDocType() == DocInfo.MEMBER_TYPE || ref.getRefDocType() == DocInfo.EXECMEMBER_TYPE)
            {
               cmd = "member";
               String name = ref.getRefDocName();
               int idx = name.indexOf("(");
               if (idx > 0)
                  name = name.substring(0, idx);
               ref.setRefDocName(name);
            }
            String var = cmd + "_name";
            String value = ref.getRefDocName();
            String label = ref.getLabel();
            if (StringUtils.isBlank(label))
            {
               label = ref.getRefDocName();
            }
            
            // <a href="{cmd}.main.do?{var}=$value">$caption</a>
            text.append("<a href=\"").append(cmd).append(".main.do?");
            text.append(var).append("=").append(value).append("\">");
            text.append(label).append("</a>");
            
            //log.debug("Resolved inline tag: "+text);
         }
         else  // otherwise usually will be kind 'Text'
         {
            //log.debug("Tag text: "+tags[i].text());
            text.append(tags[i].text());
         }
         text.append(" ");
      }
      return text.toString().trim();
   }
}
