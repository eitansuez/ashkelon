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
