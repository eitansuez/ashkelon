package org.ashkelon.pages;

import java.util.*;
import java.io.*;
import java.net.*;

import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;

public class ConfigInfo
{
   private Collection commandList;
   private String defaultCmd;
   private String defaultPkg;
   private Map commandMap;
   private int maxTrailLength;
   private int traceLevel;
   private String traceFile;
   
   public ConfigInfo()
   {
   }
   
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

   public static void main(String args[]) throws Exception
   {
      (new ConfigInfo()).load();
   }
}
