<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

outstanding tasks:
  parametrize: colors, their links (associated commands)
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#F9D362";

  String cmd = ServletUtils.getRequestParam(request, "cmd");
  String[] parts = StringUtils.split(cmd,".");
  String element_type = parts[1];
  String selectedTab = (String) ServletUtils.getRequestParam(request, "start");
  
  Map tabs = new HashMap();
  String[] entries = new String[26];
  for (char c='A'; c<='Z'; c++)
  {
    entries[c-'A'] = (new Character(c)).toString();
    tabs.put(entries[c-'A'], "idx."+element_type);
  }
  
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 

 |
<% for (int i=0; i<entries.length; i++) { %>
 <% if (entries[i].equals(selectedTab))  { %>
      <%=entries[i]%>
 <% } else { %>
      <A HREF="index.html?cmd=<%=tabs.get(entries[i])%>&start=<%=entries[i]%>"><%=entries[i]%></A>
 <% } %>
 |
<%}%>

<BR>