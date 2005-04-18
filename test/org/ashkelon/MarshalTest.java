/*
 * Created on Apr 14, 2005
 */
package org.ashkelon;

import java.sql.Connection;
import java.sql.SQLException;

import org.ashkelon.db.DBMgr;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * @author Eitan Suez
 */
public class MarshalTest
{

   public MarshalTest() throws JiBXException, SQLException, ClassNotFoundException
   {
      Connection conn = DBMgr.getInstance().getConnection();
      int clsid = 38833;
      ClassType type = ClassType.makeClassFor(conn, clsid, false);
      
      IBindingFactory bfact = BindingDirectory.getFactory(ClassType.class);
      IMarshallingContext ctxt = bfact.createMarshallingContext();
      ctxt.setIndent(3);
      ctxt.marshalDocument(type, "UTF-8", null, System.out);
   }
   
   public static void main(String[] args) throws Exception
   {
      new MarshalTest();
   }
   
}
