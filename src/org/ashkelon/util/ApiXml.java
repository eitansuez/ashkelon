package org.ashkelon.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.ashkelon.API;
import org.jibx.runtime.*;

/**
 * A utility to write xml skeleton file from package-list file
 * to take away drudgery of creating xml files that are inputs
 * into the repository manager
 *
 * @author Eitan Suez
 */
public class ApiXml
{
  public static void main(String args[]) throws IOException, JiBXException
  {
    if (args.length == 0 || args[0] == null)
    {
      System.out.println("Usage: java org.ashkelon.util.ApiXml <plist_filename>");
      return;
    }

    API api = new API();
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    String packagename = null;
    while ( (packagename=br.readLine()) != null )
    {
       packagename = packagename.trim();
       if ("".equals(packagename)) continue;  // skip any blank lines (precaution)
       api.addPackagename(packagename);
    }
    br.close();
    
    // marshal api:
    IBindingFactory factory = BindingDirectory.getFactory(API.class);
    IMarshallingContext ctxt = factory.createMarshallingContext();
    ctxt.setIndent(3);
    ctxt.marshalDocument(api, "UTF-8", null, System.out);
    
  }
}

