package org.ashkelon.util;
/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 6, 2005
 * Time: 4:50:36 PM
 */

import junit.framework.TestCase;
import org.ashkelon.util.PathComputer;

public class PathTest
      extends TestCase
{

   public void testPath()
   {
      String fromPath, toPath, expectedResult, link;

      PathComputer computer = new PathComputer();

      fromPath = "junit/extensions/ActiveTestSuite.html";
      toPath = "images/check_sm.gif";
      expectedResult = "../../images/check_sm.gif";

      link = computer.computeRelativePath(fromPath, toPath);
      assertEquals(expectedResult, link);

      fromPath = "junit/extensions/ActiveTestSuite/constructor.html";
      toPath = "junit/extensions/package.html";
      expectedResult = "../package.html";

      link = computer.computeRelativePath(fromPath, toPath);
      assertEquals(expectedResult, link);

      fromPath = "junit/extensions/ActiveTestSuite.html";
      toPath = "junit/extensions/package.html";
      expectedResult = "package.html";

      link = computer.computeRelativePath(fromPath, toPath);
      assertEquals(expectedResult, link);

      fromPath = "junit/extensions/ActiveTestSuite/run.html";
      toPath = "junit/framework/Protectable/protect.html";
      expectedResult = "../../framework/Protectable/protect.html";

      link = computer.computeRelativePath(fromPath, toPath);
      assertEquals(expectedResult, link);

      fromPath = "junit/extensions/package.html";
      toPath = "junit/extensions/ActiveTestSuite/run.html";
      expectedResult = "ActiveTestSuite/run.html";

      link = computer.computeRelativePath(fromPath, toPath);
      assertEquals(expectedResult, link);
   }
}
