package org.ashkelon.pages;

import java.util.*;
import java.io.*;
import org.jibx.runtime.*;
import org.ashkelon.*;

/**
 * @author Eitan Suez
 */
public class ConfigInfo
{
   ArrayList commandList;
   String defaultCmd;
   String defaultPkg;
   int maxTrailLength;
   int traceLevel;
   String traceFile;
   int pageSize;
   String inlineTagResolver;
   
   private static ConfigInfo instance = null;
   public static ConfigInfo getInstance()
   {
      if (instance == null)
      {
         try
         {
            instance = load();
         }
         catch (JiBXException ex)
         {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load ashkelon viewer " + 
                  "configuration information: "+ex.getMessage());
         }
      }
      return instance;
   }
   
   
   private static ConfigInfo load() throws JiBXException
   {
      ClassLoader loader = ConfigInfo.class.getClassLoader();
      InputStream is = loader.getResourceAsStream("org/ashkelon/pages/configinfo.xml");
      Reader reader = new InputStreamReader(is);
      
      IBindingFactory fact = BindingDirectory.getFactory(ConfigInfo.class);
      IUnmarshallingContext uctxt = fact.createUnmarshallingContext();
      ConfigInfo info = (ConfigInfo) uctxt.unmarshalDocument(reader);
      
      info.loadCmdMap();
      info.resolveInlineTagResolverClass();
      
      return info;
   }
   
   Map commandMap;

   private void loadCmdMap()
   {
      commandMap = new HashMap();
      Iterator itr = commandList.iterator();
      CommandInfo cmdinfo;
      while (itr.hasNext())
      {
         cmdinfo = (CommandInfo) itr.next();
         commandMap.put(cmdinfo.getCommand(), cmdinfo);
      }
   }
   
   InlineTagResolver resolver = null;
   public InlineTagResolver getResolver() { return resolver; }
   
   private void resolveInlineTagResolverClass()
   {
      try
      {
         Class resolverClass = Class.forName(inlineTagResolver);
         resolver = (InlineTagResolver) resolverClass.newInstance(); 
      }
      catch (Exception ex)
      {
         System.err.println("Failed to create/instantiate inline tag resolver class");
         System.err.println("Exception: " + ex.getMessage());
         ex.printStackTrace();
         throw new RuntimeException(ex.getMessage());
      }
      
   }
   
}

