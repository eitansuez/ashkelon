/*
 * Created on Aug 17, 2004
 */
package org.ashkelon.util;

import java.util.*;

/**
 * @author Eitan Suez
 */
public class HashtableTest
{
   public static void main(String[] args)
   {
      Properties props = new Properties();
      props.put("One", "Ehad");
      props.put("One", "Un");
      System.out.println(props.get("One"));  // produces "Un", meaning that
       // putting values with an existing key overwrites old value with new one
   }

}
