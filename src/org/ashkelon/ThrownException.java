package org.ashkelon;
/**
 *  Ashkelon
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import org.ashkelon.util.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Part of Persistable javadoc object model
 * Analog of com.sun.javadoc.ThrowsTag
 *
 * @author Eitan Suez
 * @version 2.0
 */
public class ThrownException implements Serializable
{
   private ExecMember thrower;
   private ClassType exception;
   private String name;
   private String description;
   
   private static String TABLENAME = "THROWNEXCEPTION";
   
   public ThrownException(String name)
   {
      setName(name);
      setDescription("");
   }
   
   public ThrownException(String name, String description, ExecMember thrower)
   {
      setName(name);
      setDescription(description);
      setThrower(thrower);
   }
   
   public void store(Connection conn) throws SQLException
   {
      Map fieldInfo = new HashMap(5);
      fieldInfo.put("NAME", StringUtils.truncate(getName(), 150));
      fieldInfo.put("DESCRIPTION", getDescription());
      fieldInfo.put("THROWERID", new Integer(getThrower().getId(conn)));
      
      try
      {
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
      catch (SQLException ex)
      {
         fieldInfo.put("DESCRIPTION", StringUtils.truncate(getDescription(), 350));
         DBUtils.insert(conn, TABLENAME, fieldInfo);
      }
   }
   
   public ExecMember getThrower() { return thrower; }
   public void setThrower(ExecMember thrower) { this.thrower = thrower; }
   
   public ClassType getException() { return exception; }
   public void setException(ClassType exception) { this.exception = exception; }
   
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
   
   public String getDescription() { return description; }
   public void setDescription(String description) { this.description = description; }
  
}