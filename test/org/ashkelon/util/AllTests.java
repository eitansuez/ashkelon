/*
 * Created on Aug 2, 2004
 */
package org.ashkelon.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eitan Suez
 */
public class AllTests
{
   public static Test suite()
   {
      TestSuite suite = new TestSuite("Test for org.ashkelon.util");
      //$JUnit-BEGIN$
      suite.addTestSuite(TestJDocUtil.class);
      //$JUnit-END$
      return suite;
   }
}
