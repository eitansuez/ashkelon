package org.ashkelon;
/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Feb 3, 2006
 * Time: 1:03:37 PM
 */

import junit.framework.TestCase;

public class AuthorTest
      extends TestCase
{
   public void testAuthorParseText1()
   {
      Author author = new Author("  Eitan Suez  ");
      assertEquals("Eitan Suez", author.getName());
      assertEquals("", author.getEmail());
   }
   public void testAuthorParseText2()
   {
      new Author(" this is complete gibberish ... ");
      // make sure you get no exception..don't care what's in name
   }
   public void testAuthorParseText3()
   {
      Author author = new Author("<a href=\"mailto:eitan@u2d.com\">Eitan Suez</a>");
      assertEquals("Eitan Suez", author.getName());
      assertEquals("eitan@u2d.com", author.getEmail());
   }
}
