package org.ashkelon;

import junit.framework.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.io.*;

public class SimpleTest extends TestCase
{
   public void testSerializeClass() throws Exception
   {
      DBMgr mgr = DBMgr.getInstance();
      Connection conn = mgr.getConnection();
      ClassType someclass = ClassType.makeClassFor(conn, 434, true);
      
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("someclass.ser"));
      oos.writeObject(someclass);
      oos.close();
   }
}
