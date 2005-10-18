package org.ashkelon.pages;

import java.util.*;
import java.io.*;
import org.jibx.runtime.*;

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
   
}

