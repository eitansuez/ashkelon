/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;


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
