/*
 * Created on Mar 19, 2005
 */
package org.ashkelon.manager;

import java.io.InputStream;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

/**
 * @author Eitan Suez
 */
public class Config
{
   private static Config instance = null;
   
   public static Config getInstance()
   {
      if (instance == null)
      {
         instance = new Config();
      }
      return instance;
   }
   
   private Config()
   {
      loadInfo();
   }
   
   
   private String sourcepathBase;
   
   public String getSourcePathBase()
   {
      return sourcepathBase;
   }
   
   
   private void loadInfo() throws RuntimeException
   {
      try
      {
         ClassLoader loader = Config.class.getClassLoader();
         InputStream is = loader.getResourceAsStream("org/ashkelon/manager/ashkelon-config.xml");
         
         SAXReader reader = new SAXReader();
         Document document = reader.read(is);
         
         String xpath = "//ashkelon-config/sourcecache-basepath";
         Element element = (Element) document.selectSingleNode(xpath);
         sourcepathBase = element.getTextTrim();
      }
      catch (DocumentException ex)
      {
         ex.printStackTrace();
         throw new RuntimeException();
      }
   }
   
}
