/*
 * Created on Mar 23, 2005
 */
package org.ashkelon;

/**
 * @author Eitan Suez
 */
public class Action
{
   public String caption;
   public String href;
   
   public Action(String caption, String href)
   {
      this.caption = caption;
      this.href = href;
   }
   
   public String getCaption() { return caption; }
   public String getHref() { return href; }
   
}
