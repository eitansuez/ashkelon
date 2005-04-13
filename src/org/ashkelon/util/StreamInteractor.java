/*
 * Created on Apr 11, 2005
 */
package org.ashkelon.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Eitan Suez
 */
public class StreamInteractor extends Thread
{
   InputStream _is;
   OutputStream _os;
   String _cue, _outputString;
   Logger log = Logger.getInstance();
   
   public StreamInteractor(InputStream is, OutputStream os, String cue, 
         String outputString)
   {
      _is = is;
      _os = os;
      _cue = cue;
      _outputString = outputString;
   }
   
   public void run()
   {
      BufferedReader reader = null;
      PrintWriter writer = null;
      try
      {
         boolean sentOutput = false;
         reader = new BufferedReader(new InputStreamReader(_is));
         writer = new PrintWriter(new BufferedOutputStream(_os));
         
         String line = reader.readLine();
         while (line != null)
         {
            log.traceln(line);
            if (line.indexOf(_cue) >= 0 && !sentOutput)
            {
               try
               {  // wait for prompt to be spit out
                  Thread.sleep(500);
               } catch (InterruptedException ex) {}
               
               writer.println(_outputString);
               writer.flush();
               
               sentOutput = true;
            }
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
            if (writer != null) writer.close();
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
