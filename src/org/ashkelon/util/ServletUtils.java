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

package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Contains various utility methods for dealing with http stuff
 * using the servlet api
 *
 * @author Eitan Suez
 */
public class ServletUtils
{
   public static int getIntParam(ServletRequest request, String name)
   {
      // if name is null or blank, set to 0
      String value = getRequestParam(request, name);
      if (StringUtils.isBlank(value))
         value = "0";
      try
      {
         return Integer.parseInt(value);
      } catch (NumberFormatException ex)
      {
         return -1;
      }
   }
   
   public static String getRequestParam(ServletRequest request, String key)
   {
      Object val = request.getAttribute(key);
      String value;
      if (val == null)
         value = StringUtils.avoidNull(request.getParameter(key));
      else
         value = val.toString();
      return value;
   }
   
   public static String getQueryString(ServletRequest request)
   {
      Enumeration e = request.getParameterNames();
      Map pairs = new HashMap(6);
      String name;
      while (e.hasMoreElements())
      {
         name = (String) e.nextElement();
         pairs.put(name, request.getParameter(name));
      }
      e = request.getAttributeNames();
      Object attr_value;
      while (e.hasMoreElements())
      {
         name = (String) e.nextElement();
         attr_value = request.getAttribute(name);
         if (attr_value instanceof String)
            pairs.put(name, attr_value);
      }
      
      String[] strpairs = new String[pairs.size()];
      Set entrySet = pairs.entrySet();
      Iterator itr = entrySet.iterator();
      Map.Entry entry;
      int i=0;
      while (itr.hasNext())
      {
         entry = (Map.Entry) itr.next();
         strpairs[i++] = (String) entry.getKey() + "=" + entry.getValue();
      }
      return StringUtils.join(strpairs, "&");
   }

   public static String getCommand(HttpServletRequest request)
   {
       String path = request.getServletPath();
       int len = path.length();
       String cmd = path.substring(1,len-3); // trim prefixed / and suffixed .do
       System.err.println("cmd is " +cmd);
       return cmd;
   }
}
