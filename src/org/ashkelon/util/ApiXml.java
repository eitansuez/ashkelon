package org.ashkelon.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Eitan Suez
 *
 * a utility to write xml skeleton file from package-list file
 * to take away drudgery of creating xml files that are inputs
 * into the repository manager
 */

public class ApiXml
{
  public static void main(String args[]) throws IOException
  {
    if (args.length == 0 || args[0] == null)
    {
      System.out.println("Usage: java org.ashkelon.util.ApiXml <plist_filename>");
      return;
    }
    System.out.println("<?xml version=\"1.0\" ?>");
    System.out.println("<api>");
    System.out.println("<name></name>");
    System.out.println("<summarydescription></summarydescription>");
    System.out.println("<description></description>");
    System.out.println("<publisher></publisher>");
    System.out.println("<download_url></download_url>");
    System.out.println("<release_date>2001-07-03T08:00:00.000</release_date>");
    System.out.println("<version></version>");
    
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    String line = null;
    while ( (line=br.readLine()) != null )
    {
      System.out.println("<package>"+line+"</package>");
    }
    br.close();
    System.out.println("</api>");
  }
}

