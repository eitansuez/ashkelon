/*
 * Created on Dec 10, 2004
 */
package org.ashkelon;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eitan Suez
 */
public class AshkelonSuite
{

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Test for org.ashkelon");
      //$JUnit-BEGIN$
      suite.addTestSuite(JibxTest.class);
      //$JUnit-END$
      return suite;
   }
}
