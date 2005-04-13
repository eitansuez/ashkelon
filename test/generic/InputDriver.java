/*
 * Created on Apr 10, 2005
 */
package generic;

import java.io.File;

import org.ashkelon.util.StreamInteractor;

/**
 * @author Eitan Suez
 */
public class InputDriver
{
   public static void main(String[] args) throws Exception
   {
      File dir = new File("/Users/eitan/projects/ashkelon/build/classes");
      Process process = Runtime.getRuntime().exec("java generic.ConsoleInput", null, dir);
      
      new StreamInteractor(process.getInputStream(), process.getOutputStream(),
            "Tell me your name: ", "Eitan");
      
      int exitValue = process.waitFor();
      
   }
}
