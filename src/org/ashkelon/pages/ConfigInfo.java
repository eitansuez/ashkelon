/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

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
