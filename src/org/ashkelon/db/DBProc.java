/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

package org.ashkelon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ashkelon.util.Logger;

public class DBProc
{
   static Map scriptMap = null;
   private Logger log;
   
   public DBProc()
   {
      if (scriptMap == null)
      {
         scriptMap = new HashMap();
         scriptMap.put("reset", "org/ashkelon/db/ashkelon.sql");
         scriptMap.put("reset_seqs", "org/ashkelon/db/init.sql");
         scriptMap.put("add_idx", "org/ashkelon/db/ashkelon_addidx.sql");
         scriptMap.put("del_idx", "org/ashkelon/db/ashkelon_dropidx.sql");
      }
      log = Logger.getInstance();
      //log.setPrefix("DBProc");
      //log.setTraceLevel(Logger.DEBUG);
   }
   
   // for 8i lite, signature must be:
   //public static void entryPoint(Connection conn, String action)
   // for 8i standard/enterprise, signature must be:
   public static void entryPoint(String action)
   {
      DBProc proc = new DBProc();
      String script = (String) scriptMap.get(action);
      proc.log.debug("Script is: "+script);
      if (script == null) return;
      boolean warn = (action.indexOf("idx") > -1) ? false : true;
      List commands = ScriptParser.parse(script, warn);
      proc.log.debug("Commands length: "+commands.size());

      try
      {
         // this is how to get ref to conn using 8i standard/enterprise:
         Connection conn = DriverManager.getConnection("jdbc:default:connection:");
         
         Statement stmt = conn.createStatement();
         String command = "";
         for (int i=0; i<commands.size(); i++)
         {
            command = (String) commands.get(i);
            proc.log.debug("Cmd: "+command);
            try {  // desired behavior: if one statement fails, don't block rest
               stmt.execute(command);
            } catch (SQLException ex)
            {
               proc.log.error("Cmd Failed: "+command);
            }
         }
         stmt.close();
      } catch (SQLException ex)
      {
         proc.log.error("Failed: "+ex.getMessage());
         DBUtils.logSQLException(ex);
      }
   }
   
   public void doAction(Connection conn, String action) throws SQLException
   {
      String script = (String) scriptMap.get(action);
      if (script == null)
      {
         log.brief("cannot fulfill request to "+action);
         return;
      }
      // script = StringUtils.substitute(script, "\\$dbtype", DBMgr.getInstance().getDbtype());
      boolean warn = (action.indexOf("idx") > -1) ? false : true;
      List commands = ScriptParser.parse(script, warn);
      DBUtils.submitBatch(conn, commands);
   }
   
   public static void main(String args[]) throws SQLException
   {
      //DBMgr mgr = DBMgr.getInstance();
      //Connection conn = mgr.getConnection();
      if (args.length == 0)
      {
         Logger.getInstance().traceln("Must provide an action");
         return;
      }
      //entryPoint(conn, args[0]);
      entryPoint(args[0]);
      //mgr.releaseConnection(conn);
   }
   
}
