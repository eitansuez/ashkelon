package org.ashkelon.util;


import org.apache.oro.text.perl.*;


public class HtmlUtils
{
   /**
    *  makes text suitable for inclusion in a javascript string
    *  by escaping double quotes.
    */
   public static String cleanText(String text)
   {
      if (text == null) return "";
      //return text.replaceAll("\"", "\\\\\"");
      Perl5Util util = new Perl5Util();
      String pattern = "s/\"/\\\"/g";
      return util.substitute(pattern, text);
   }

   /**
    * for tag attributes, double quotes are replaced with single quotes
    */
   public static String cleanAttributeText(String text)
   {
      if (text == null) return "";
      //return text.replaceAll("\"","'");
      Perl5Util util = new Perl5Util();
      String pattern = "s/\"/'/g";
      return util.substitute(pattern, text);
   }
   
   /**
    * removes html tags for tooltips as currently do not interpret them
    */
   public static String getBareText(String text)
   {
      //Perl5Util util = new Perl5Util();
      //String pattern = "s/???/???/g";
      //return util.substitute(pattern, text);
      return text;
   }
   
}