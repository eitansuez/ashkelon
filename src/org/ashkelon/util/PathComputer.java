package org.ashkelon.util;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 4:57:04 PM
 */
public class PathComputer
{
   public String computeRelativePath(String from, String to)
   {
      Pair reduced = reduce(from, to);
      return rewind(reduced.first) + reduced.second;
   }
   
   private Pair reduce(String one, String two)
   {
      String leastCommonBasePath = leastCommonBasePath(one, two);
      int index = one.indexOf(leastCommonBasePath);
      one = one.substring(index+leastCommonBasePath.length());
      two = two.substring(index+leastCommonBasePath.length());
      return new Pair(one, two);
   }
   
   class Pair
   {
      String first, second;
      Pair(String one, String two) { first = one; second = two; }
   }

   private String rewind(String path)
   {
      StringTokenizer tokenizer = new StringTokenizer(path, "/");
      String rewound = "";
      
      if (tokenizer.hasMoreTokens())
         tokenizer.nextToken();  // skip one
      
      while (tokenizer.hasMoreTokens())
      {
         tokenizer.nextToken();
         rewound += "../";
      }
      
      return rewound;
   }
   
   private String leastCommonBasePath(String one, String two)
   {
      StringTokenizer tokenizer = new StringTokenizer(one, "/");
      StringTokenizer tokenizer2 = new StringTokenizer(two, "/");
      String token, token2;
      String path = "";
      while (tokenizer.hasMoreTokens())
      {
         token = tokenizer.nextToken();
         token2 = tokenizer2.nextToken();
         if (token.equals(token2))
         {
            path += token + "/";
         }
         else
         {
            break;
         }
      }
      return path;
   }
}
