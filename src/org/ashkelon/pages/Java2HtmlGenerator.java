/*
 * Created on Jul 29, 2004
 */
package org.ashkelon.pages;

import java.io.*;
import de.java2html.converter.*;
import de.java2html.javasource.*;
import de.java2html.options.*;

/**
 * @author Eitan Suez
 */
// implementation based on the tool "Java2Html" ;  see http://www.java2html.de/
class Java2HtmlGenerator implements HtmlGenerator
{
   private JavaSourceParser _parser = null;
   private Java2HtmlConversionOptions _options = null;

   public void initialize(File srcHtmlDir)
   {
      _options = Java2HtmlConversionOptions.getDefault();
      _options.setTabSize(3);
      _options.setShowLineNumbers(true);
      _options.setShowJava2HtmlLink(false);
      _options.setShowFileName(true);
      
      _parser = new JavaSourceParser(_options);
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
     JavaSource jsource = _parser.parse(sourceFile);
     JavaSourceConverter converter = new JavaSource2HTMLConverter(jsource);
     converter.setConversionOptions(_options);
     htmlFile.getParentFile().mkdirs();
     converter.convert(new FileWriter(htmlFile));
   }
   
}
