package org.ashkelon.pages;

import org.ashkelon.util.*;
import java.util.*;
import javax.servlet.http.*;

public class Trail extends Page
{
   public Trail()
   {
      super();
      log.setPrefix("trail");
   }
   
   public String handleRequest()
   {
      String cmd = ServletUtils.getCommand(request);
      if (cmd.equals("trail.reset"))
      {
         return resetTrail(request);
      }
      return null;
   }
   
   private String resetTrail(HttpServletRequest request)
   {
      HttpSession session = request.getSession(true);
      LinkedList trail = (LinkedList) session.getAttribute("trail");
      if (trail == null || trail.isEmpty())
         return "pkg";
      
      String[] lastPair = (String[]) trail.getLast();
      String last_uri = lastPair[0];
      
      String cmd = "pkg";
      if (last_uri.indexOf("?") > 0)
      {
         String queryString = StringUtils.split(last_uri, "?")[1];
         String[] pairs = StringUtils.split(queryString, "&");
         String[] name_value;
         for (int i=0; i<pairs.length; i++)
         {
            name_value = StringUtils.split(pairs[i], "=");
            request.setAttribute(name_value[0], name_value[1]);
            if ("cmd".equals(name_value[0]))
               cmd = name_value[1];
         }
      }
      session.setAttribute("trail", new LinkedList());
      log.traceln("trail has been reset");
      return cmd;
   }
  
}
