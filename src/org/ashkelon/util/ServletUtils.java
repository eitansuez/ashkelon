package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

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
