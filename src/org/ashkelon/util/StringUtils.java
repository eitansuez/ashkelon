package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.*;
import org.apache.oro.text.regex.*;


/**
 * Various string utilities.  Mainly split & join.
 *
 * @author Eitan Suez
 */
public class StringUtils
{
   public static boolean isBlank(java.lang.String text)
   {
      return (text == null || text.trim().equals(""));
   }
   
   public static String avoidNull(String text)
   {
      return (text==null) ? "" : text;
   }
   
   public static String wrap(String text, String wrap)
   {
      return wrap + text + wrap;
   }

   /**
    * analog to perl or javascript join() method
    */
   public static String join(Object[] text, String concatenator)
   {
      if (text.length == 0) { return ""; }
      String result = (text[0] == null) ? "" : (String) text[0];
      for (int i=1; i<text.length; i++)
      {
         result += concatenator + ((text[i] == null) ? "" : (String) text[i]);
      }
      return result;
   }
   
   /**
    * produce text x numtimes, concatenated by concatenator text
    * e.g. "123" x 3 with concat = "," procudes: "123,123,123"
    */
   public static String join(String text, String concatenator, int numtimes)
   {
      if(text==null) { return ""; }
      if(numtimes <= 0) { return ""; }
      String result = text;
      for (int i=1; i<numtimes; i++)
      {
         result += concatenator + text;
      }
      return result;
   }
   
   /**
    * the analog of perl's or javascript's split() method
    */
   public static String[] split(String text, String delimiter)
   {
      ArrayList list = new ArrayList(10);
      StringTokenizer stk = new StringTokenizer(text, delimiter);
      while (stk.hasMoreTokens())
      {
         list.add(stk.nextToken().trim());
      }
      String[] results = new String[list.size()];
      list.toArray(results);
      return results;
   }
   
   public static void main(String args[])
   {
      String[] list = StringUtils.split("eitan", ",");
      for (int i=0; i<list.length; i++)
      {
         Logger.getInstance().traceln(list[i]);
      }
   }

   
   public static boolean getCommandLineOption(String option, String[][] options)
   {
      boolean value = false;
      for (int i = 0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            value = true;
         }
      }
      return value;
   }
   
   public static int getCommandLineOption(String option, String[][] options, int defaultValue)
   {
      int value = defaultValue;
      for (int i = 0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            try
            {
               value = Integer.parseInt(opt[1]);
            } catch (NumberFormatException ex)
            {
               value = defaultValue;
            }
         }
      }
      return value;
   }
   
   public static String getStringCommandLineOption(String option, String[][] options)
   {
      for (int i = 0; i < options.length; i++)
      {
         String[] opt = options[i];
         if (opt[0].equals(option))
         {
            return opt[1];
         }
      }
      return "";
   }
   
   
   public static String truncate(String text, int length)
   {
      if (text.length() > length)
      {
         StringBuffer buff = new StringBuffer(text);
         buff.setLength(length);
         return buff.toString();
      } // else..
      return text;
   }

   public static String substitute(String input, String pattern, String replacement)
   {
     try
     {
        return Util.substitute(new Perl5Matcher(), 
                               new Perl5Compiler().compile(pattern), 
                               new Perl5Substitution(replacement),
                               input, Util.SUBSTITUTE_ALL);
     }
     catch (MalformedPatternException ex)
     {
       return input;
     }
   }


  /**
   * strips out any null characters from string
   * @return string less any null characters in it
   */
  public static String stripNull(String input)
  {
    StringBuffer result = new StringBuffer();
    char c;
    for (int i=0; i<input.length(); i++)
    {
      c = input.charAt(i);
      if (c != 0)
      {
        result.append(c);
      }
    }
    return result.toString();
  }

}
