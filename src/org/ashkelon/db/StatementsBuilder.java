/*
 * Created on Aug 18, 2004
 */
package org.ashkelon.db;

import java.util.*;
import java.io.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * @author Eitan Suez
 */
public class StatementsBuilder extends Task
{
   /*
    * currently, each database supported by ashkelon has its own
    * statements.properties file.  this is fine.  the problem is
    * that the vast majority of these statements are actually the
    * same.  only a few are different, necessary to accomodate a
    * specific database.  so i have duplication.  if i want to change
    * a query, i have to do it in n places, n being the number of 
    * databases i want to support.  so to resolve this problem, what
    * i need is a separation of general statements from db-specific ones
    * and a build-time merge of the general and db-specific files into
    * a final statements.properties file customized for 
    * 
    * one way to accomplish this is to simply take advantage of hashtables.
    * i can load the general statements.properties and then the specific
    * statements.properties into the same Properties object.  the nature of
    * the hash will ensure that specific statements replace general ones.
    * the resulting hash is then written out to the final statements.properties
    * file
    * 
    * the most ideal way to use this code would be as an ant task, coming up.
    *  
    * <taskdef name="build-statements"
    *   classname="org.ashkelon.db.StatementsBuilder" classpath="?">
    * <build-statements
    *   dbtype="${dbtype}" 
    *   tofile="${build.classes.dir}/org/ashkelon/db/statements.properties" />
    */
   
   private static final String[] DBTYPES = {"postgres", "mysql"};
   private String _dbtype = null;
   private File _tofile = null;
   
   public void setDbtype(DbType dbtype) { _dbtype = dbtype.getValue(); }
   public void setTofile(File tofile) { _tofile = tofile; }
   
   public void execute()
   {
      try
      {
         File general = new File("etc/db/statements.properties");
         Properties statements = new Properties();
         statements.load(new FileInputStream(general));
         
         File specific = new File("etc/db/statements-"+_dbtype+".properties");
         if (!specific.exists())
         {
            statements.store(new FileOutputStream(_tofile), 
                  "statements file for "+_dbtype);
            return;
         }
         
         statements.load(new FileInputStream(specific));
         statements.store(new FileOutputStream(_tofile), 
               "merged statements file for "+_dbtype);
      }
      catch (IOException ex)
      {
         throw new BuildException(ex);
      }
   }
   
   public static class DbType extends EnumeratedAttribute
   {
      public String[] getValues() { return DBTYPES; }
   }

}
