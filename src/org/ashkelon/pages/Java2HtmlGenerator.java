/*
 * Created on Jul 29, 2004
 */
package org.ashkelon.pages;

import java.io.*;
import de.java2html.converter.*;
import de.java2html.javasource.*;
import de.java2html.options.*;

/**
 * implementation based on the tool "Java2Html" ;  see http://www.java2html.de/
 * 
 * @author Eitan Suez
 */
public class Java2HtmlGenerator implements HtmlGenerator
{
   private Java2HtmlConversionOptions _options = null;

   public void initialize(File srcHtmlDir)
   {
      _options = Java2HtmlConversionOptions.getDefault();
      _options.setTabSize(3);
      _options.setShowLineNumbers(true);
      _options.setShowJava2HtmlLink(false);
      _options.setShowFileName(true);
   }
   
   public void produceHtml(String sourceFile, String realHtmlFile)
   {
      try
      {
         java2HtmlConvert(new File(sourceFile), new File(realHtmlFile));
      }
      catch (IOException ex)
      {
         System.err.println("Failed to build html from java source");
         System.err.println("IO Exception: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   private void java2HtmlConvert(File sourceFile, File htmlFile) throws IOException
   {
     htmlFile.getParentFile().mkdirs();

     JavaSourceConverter converter = new JavaSource2HTMLConverter();
     converter.setConversionOptions(_options);
     JavaSourceParser parser = new JavaSourceParser(_options);
     JavaSource jsource = parser.parse(sourceFile);
     converter.convert(jsource, new FileWriter(htmlFile));
   }
   
}
