/*
 * Created on Aug 2, 2004
 */
package org.ashkelon.util;

import junit.framework.TestCase;

/**
 * @author Eitan Suez
 */
public class TestJDocUtil extends TestCase
{
   public void testUnqualify()
   {
      String nestedClassName = "java.applet.Applet.AccessibleApplet";
      String result = null;
      
      result = JDocUtil.unqualify(nestedClassName, true);
      assertEquals("Unqualify produces wrong result for nested classes", 
            "Applet.AccessibleApplet", result);
      
      String className = nestedClassName = "java.applet.Applet";
      result = JDocUtil.unqualify(className);
      assertEquals("Unqualify produces wrong result for classes", 
            "Applet", result);
   }
}
