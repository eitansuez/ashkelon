/*
 * Created on Apr 11, 2005
 */
package org.ashkelon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Eitan Suez
 */
public class StreamConsumer extends Thread
{
   private InputStream _stream;
   private Logger log = Logger.getInstance();
   
   public StreamConsumer(InputStream stream)
   {
      _stream = stream;
   }
   
   public void run()
   {
      BufferedReader reader = null;
      try
      {
         reader = new BufferedReader(new InputStreamReader(_stream));
         String line = reader.readLine();
         while (line != null)
         {
            log.traceln(line);
            line = reader.readLine();
         }
      }
      catch (IOException ex)
      {
         System.err.println("IOException: "+ex.getMessage());
         ex.printStackTrace();
      }
      finally
      {
         try
         {
            if (reader != null) reader.close();
            if (_stream != null) _stream.close();
         }
         catch (IOException ex)
         {
            System.err.println("Exception attempting to closer stream/reader");
            System.err.println("IOException: "+ex.getMessage());
            ex.printStackTrace();
         }
      }
   }
   
}
