package org.ashkelon.pages;

import java.util.*;
import java.io.*;
import java.net.*;

import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;
import org.ashkelon.*;

/**
 * @author Eitan Suez
 */
public class ConfigInfo
{
   private Collection commandList;
   private String defaultCmd;
   private String defaultPkg;
   private Map commandMap;
   private int maxTrailLength;
   private int traceLevel;
   private String traceFile;
   private String inlineTagResolver;
   
   public ConfigInfo() {}
   
   public ConfigInfo load() throws Exception
   {
      ClassLoader loader = this.getClass().getClassLoader();

      URL resource = loader.getResource("org/ashkelon/pages/configmapping.xml");
      Mapping mapping = new Mapping(loader);
      mapping.loadMapping(resource);

      InputStream is = loader.getResourceAsStream("org/ashkelon/pages/configinfo.xml");
      Reader reader = new InputStreamReader(is);
      Unmarshaller ums = new Unmarshaller(ConfigInfo.class);
      ums.setMapping(mapping);
      ConfigInfo info = (ConfigInfo) ums.unmarshal(reader);
      
      Map commandMap = new HashMap();
      Iterator itr = info.getCommandList().iterator();
      CommandInfo cmdinfo;
      while (itr.hasNext())
      {
         cmdinfo = (CommandInfo) itr.next();
         commandMap.put(cmdinfo.getCommand(), cmdinfo);
      }
      info.setCommandMap(commandMap);
      return info;
   }
   
   public String getDefaultCmd()
   {
      return defaultCmd;
   }
   public void setDefaultCmd(String defaultCmd)
   {
      this.defaultCmd = defaultCmd;
   }
   
   public String getDefaultPkg()
   {
      return defaultPkg;
   }
   public void setDefaultPkg(String defaultPkg)
   {
      this.defaultPkg = defaultPkg;
   }
   
   public Collection getCommandList()
   {
      return commandList;
   }
   public void setCommandList(Collection commandList)
   {
      this.commandList = commandList;
   }
   
   public Map getCommandMap()
   {
      return commandMap;
   }
   public void setCommandMap(Map commandMap)
   {
      this.commandMap = commandMap;
   }
   
   public int getMaxTrailLength() { return maxTrailLength; }
   public void setMaxTrailLength(int length) { this.maxTrailLength = length; }
   
   public int getTraceLevel() { return traceLevel; }
   public void setTraceLevel(int traceLevel) { this.traceLevel = traceLevel; }

   public String getTraceFile() { return traceFile; }
   public void setTraceFile(String traceFile) { this.traceFile = traceFile; }

   public String getInlineTagResolver() { return inlineTagResolver; }
   public void setInlineTagResolver(String resolver) { inlineTagResolver = resolver; }

   private static InlineTagResolver _resolver = null;
   private static ConfigInfo _cInfo = null;
   public static InlineTagResolver getResolver()
   {
      if (_resolver == null)
      {
         try
         {
            _cInfo = new ConfigInfo().load();
         }
         catch (Exception ex)
         {
            System.err.println("Failed to load config info");
            System.err.println(ex.getMessage());
            System.exit(0);
         }
         
         try
         {
            Class resolverClass = Class.forName(_cInfo.getInlineTagResolver());
            _resolver = (InlineTagResolver) resolverClass.newInstance(); 
         }
         catch (ClassNotFoundException ex)
         {
            System.err.println("Failed to instantiate inline tag resolver class");
            System.err.println("ClassNotFoundException: " + ex.getMessage());
            System.exit(0);
         }
         catch (IllegalAccessException ex)
         {
            System.err.println("Failed to instantiate inline tag resolver class");
            System.err.println("IllegalAccessException: " + ex.getMessage());
            System.exit(0);
         }
         catch (InstantiationException ex)
         {
            System.err.println("Failed to instantiate inline tag resolver class");
            System.err.println("InstantiationException: " + ex.getMessage());
            System.exit(0);
         }
      }
      return _resolver;
   }

   public static void main(String args[]) throws Exception
   {
      (new ConfigInfo()).load();
   }
}

