/*
 * Created on Dec 10, 2004
 */
package org.ashkelon;

import junit.framework.TestCase;
import org.jibx.runtime.*;
import java.io.*;

/**
 * @author Eitan Suez
 */
public class JibxTest extends TestCase
{
   private API _api = null;
   private IBindingFactory _bfact = null;
   private static final String FILENAME = "test-api.xml";
   private static final int INDENT = 2;

   protected void setUp() throws Exception
   {
      _bfact = BindingDirectory.getFactory(API.class);
      
      _api = new API("ETTA");
      _api.setSummaryDescription("Eitan's Test Test API");
      _api.setDescription("A Ficticious API, used for testing marshalling using JiBX");
      _api.setDownloadURL("blah");
      _api.setVersion("1.0");
      _api.setPublisher("Eitan");
      _api.setReleaseDate(new java.util.Date());
      _api.addPackagename("org.eitan.test");
      _api.addPackagename("org.eitan");
      _api.addPackagename("org.eitan.testing");
   }
   protected void tearDown() throws Exception
   {
      _api = null;
      _bfact = null;
   }
   
   public void testMarshalUnmarshal() throws Exception
   {
      IMarshallingContext imctxt = _bfact.createMarshallingContext();
      imctxt.setIndent(INDENT);
      IUnmarshallingContext umctxt = _bfact.createUnmarshallingContext();
      
      imctxt.marshalDocument(_api, "UTF-8", null, 
            new FileOutputStream(FILENAME));
      API api = (API) 
            umctxt.unmarshalDocument(new FileInputStream(FILENAME), null);
//      assertEquals("APIs should match", _api, api);
      
      assertEquals("API Names should match", _api.getName(), api.getName());
      assertEquals("Versions should match", _api.getVersion(), api.getVersion());
      assertEquals("Publishers should match", _api.getPublisher(), api.getPublisher());
      assertEquals("Release dates should match", _api.getReleaseDate(), api.getReleaseDate());
      assertEquals("URLs should match", _api.getDownloadURL(), api.getDownloadURL());
      assertEquals("Descriptions should match", _api.getDescription(), api.getDescription());
      assertEquals("Summaries should match", _api.getSummaryDescription(),
            api.getSummaryDescription());
      assertEquals("PackageNames Listings should match", _api.getPackagenames(),
            api.getPackagenames());
      System.out.println("package name counts: " + _api.getPackagenames().size() + 
            ", " + api.getPackagenames().size());
   }
   
   public void testUnmarshalMarshalAntXmlAPI() throws Exception
   {
      IMarshallingContext imctxt = _bfact.createMarshallingContext();
      imctxt.setIndent(INDENT);
      IUnmarshallingContext umctxt = _bfact.createUnmarshallingContext();
      
      API antApi = (API)
         umctxt.unmarshalDocument(new FileInputStream("apis/ant.xml"), null);
      
      assertEquals("name should be Apache Ant", "Apache Ant", antApi.getName());
      
      imctxt.marshalDocument(antApi, "UTF-8", null, 
            new FileOutputStream(FILENAME));
   }
   
}
