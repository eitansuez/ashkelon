/*
 * Created on Jul 29, 2004
 */
package org.ashkelon.pages;

import java.io.File;

/**
 * @author Eitan Suez
 */
interface HtmlGenerator
{
   public void initialize(File srcHtmlDir);
   public void produceHtml(String sourceFile, String realHtmlFile);
}
