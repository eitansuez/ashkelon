/*
 * Created on Jul 24, 2004
 */
package org.ashkelon;

import com.sun.javadoc.Tag;

/**
 * @author Eitan Suez
 */
public interface InlineTagResolver
{
   /**
    * @param tags text represented as an array of tags, as returned by doc.inlineTags()
    * @return resolved text
    */
    public String resolveDescription(DocInfo doc, Tag[] tags);
}
