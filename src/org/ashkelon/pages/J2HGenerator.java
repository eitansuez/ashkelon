/*
 * Created on Jul 29, 2004
 */
package org.ashkelon.pages;

import java.io.File;
import java.io.IOException;
import com.java2html.Java2HTML;

/**
 * @author Eitan Suez
 */
// implementation based on the tool "j2h" ; see http://www.java2html.com/
class J2HGenerator implements HtmlGenerator
{
   private Java2HTML j2h = null;

   public void initialize(File srcHtmlDir)
   {
      j2h = new Java2HTML();
      j2h.setMarginSize(4);
      j2h.setTabSize(3);
      j2h.setSimple(true);

      try
      {
         j2h.setDestination(srcHtmlDir.getCanonicalPath());
      }
      catch (IOException ex)
      {
         System.err.println("Failed to obtain canonical path for "+srcHtmlDir.toString());
         System.err.println("IO Exception: "+ex.getMessage());
         ex.printStackTrace();
      }
   }
   
   public void produceHtml(String sourceFile, String realHtmlFile)
   {
      // problem with jar and obfuscation hiding method even though in javadocs:
      //j2h.setJavaFileSource( new String[] {sourceFile} );
      try
      {
         j2h.setJavaDirectorySource(new String[] {dirof(sourceFile)} );
         j2h.buildJava2HTML();
      }
      catch (Exception ex)
      {
         System.err.println("Failed to build html from java source");
         System.err.println(ex.getMessage());
         ex.printStackTrace();
      }
   }

   private String dirof(String file)
   {
      return new File(file).getParent();
   }
}

