/*
 * Created on Apr 10, 2005
 */
package generic;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Eitan Suez
 */
public class ConsoleInput
{
   public static void main(String[] args) throws Exception
   {
      String prompt = "Tell me your name: ";
      System.out.print(prompt);
      
      // block stdin..
      String name = new BufferedReader(new InputStreamReader(System.in)).readLine();
      
      System.out.println("Thanks, "+name);
   }
}
