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
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

/**
 * A class that can parse a .sql script into a list of database statements
 * (ignores -- comments, blank lines, and properly handle multi-line statements
 * terminated by ';'
 *
 * @author Eitan Suez
 */
public class ScriptParser
{
   public ScriptParser()
   {
   }
   
   public static List parse(String script)
   {
     return parse(script, true);
   }

   /**
    * @param script the fully qualified path & name of the .sql script file
    * to parse
    * @return the list of sql statements in the script file, as strings
    */
   public static List parse(String script, boolean warn)
   {
      List commands = new ArrayList();
      try
      {
         InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(script);
         if (is == null)
         {
            if (warn)
              Logger.getInstance().brief("could not find resource:  "+script);
            return commands;
         }
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         String line = "";
         while ((line = br.readLine()) != null)
         {
            line = clean_line(line);
            if (StringUtils.isBlank(line))
               continue;
            int end_idx = line.indexOf(";");
            while (end_idx == -1)
            {
               String nextline = br.readLine();
               if (nextline == null) break;
               line += clean_line(nextline);
               end_idx = line.indexOf(";");
            }
            line = line.substring(0, end_idx);
            commands.add(line);
         }
         br.close();
         is.close();
      } catch (IOException ex)
      {
         Logger.getInstance().error("Unable to print usage!");
         Logger.getInstance().error("IOException: "+ex.getMessage());
      }
      
      return commands;
    }
    
    private static String clean_line(String line)
    {
       line = line.trim();
       if (StringUtils.isBlank(line) || line.startsWith("--"))
          return "";
       
       int comment_idx = line.indexOf("--");
       if (comment_idx > 0)
          line = line.substring(0, comment_idx).trim();
       
       return line + " ";  // add space because will likely be joined with next line
    }
    
    public static void main(String[] args)
    {
       /*
       Connection conn = null;
       DBMgr mgr = DBMgr.getInstance();
       try
       {
          List commands = ScriptParser.parse("org/ashkelon/ashkelon.sql");
          conn = mgr.getConnection();
          DBUtils.submitBatch(conn, commands);
       } catch (SQLException ex)
       {
          DBUtils.logSQLException(ex);
       }
       finally
       {
          if (conn!=null)
             mgr.releaseConnection(conn);
       }
        */
    }

}
