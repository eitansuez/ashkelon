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

package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.io.PrintWriter;

/**
 * Basic facility for generating manageable log information for a program.
 * Provides 5 different logging (or tracing) levels and the ability to 
 * filter log messages above a specified trace level.  This class is a 
 * singleton.
 *
 * @author Eitan Suez
 */
public class Logger
{
   public static final int ERROR = 10;
   public static final int BRIEF = 20;
   public static final int NORMAL = 30;
   public static final int VERBOSE = 40;
   public static final int FULL = 50;
   public static final int DEBUG = 60;
   
   private static Logger _instance = null;
   private int traceLevel;
   private PrintWriter writer, writer2;
   private String prefix;
   
   private Logger()
   {
      traceLevel = NORMAL;
      setWriter(new PrintWriter(System.out));
      setWriter2(null);
      setPrefix("");
   }
   
   public static Logger getInstance()
   {
      if (_instance == null)
      {
         _instance = new Logger();
      }
      return _instance;
   }
   
   /**
    * set the trace level
    */
   public void setTraceLevel(int traceLevel) { this.traceLevel = traceLevel; }
   public int getTraceLevel() { return traceLevel; }
   
   public void setPrefix(String prefix) { this.prefix = StringUtils.avoidNull(prefix); }
   public String getPrefix() { return prefix; }
   
   /**
    * set the print writer to send log output to
    */
   public void setWriter(PrintWriter writer)
   {
      if (this.writer != null)
      {
         //try
         //{
            this.writer.close();
         //} catch (IOException ex)
         //{}
      }
      this.writer = writer;
   }
   public PrintWriter getWriter() { return writer; }
   
   /**
    * optionally set a second print writer to send log output to
    * (e.g. log to both a file and to stdout)
    */
   public void setWriter2(PrintWriter writer2)
   {
      if (this.writer2 != null)
      {
         //try
         //{
            this.writer2.close();
         //} catch (IOException ex)
         //{}
      }
      this.writer2 = writer2;
   }
   public PrintWriter getWriter2() { return writer2; }
   
   /**
    * trace text (no carriage return)
    */
   public void trace(String text)
   {
      trace(text, traceLevel);
   }
   /**
    * trace text with carriage return
    */
   public void traceln(String text)
   {
      trace(text + "\n");
   }
   
   /**
    * trace text with given traceLevel
    */
   public void trace(String text, int traceLevel)
   {
      if (traceLevel <= this.traceLevel)
      {
         if (!StringUtils.isBlank(prefix))
         {
            text = prefix + ": " + text;
         }
         if (writer != null)
         {
            writer.print(text);
            writer.flush();
         }
         if (writer2 != null)
         {
            writer2.print(text);
            writer2.flush();
         }
         if (traceLevel==ERROR)
         {  // will show duplicate message if writer or writer 2 are system.err
            System.err.print(text);
            System.err.flush();
         }
         
      }
   }
   
   /**
    * trace text (with carriage return) using given trace level
    */
   public void traceln(String text, int traceLevel)
   {
      trace(text + "\n", traceLevel);
   }
   
   // facility methods
   
   public void error(String text)
   {
      traceln(text, ERROR);
   }
   public void brief(String text)
   {
      traceln(text, BRIEF);
   }
   public void verbose(String text)
   {
      traceln(text, VERBOSE);
   }
   public void full(String text)
   {
      traceln(text, FULL);
   }
   public void debug(String text)
   {
      traceln(text, DEBUG);
   }
   
   
}