<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#F9D362";

  String cmd = (String) request.getAttribute("cmd");
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

 |
<% for (int i=0; i<entries.length; i++) { %>
 <% if (entries[i].equals(selectedTab))  { %>
      <%=entries[i]%>
 <% } else { %>
      <A HREF="<%=tabs.get(entries[i])%>.do?start=<%=entries[i]%>"><%=entries[i]%></A>
 <% } %>
 |
<%}%>

<BR>
