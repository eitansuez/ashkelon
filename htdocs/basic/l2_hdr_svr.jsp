<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  Map tabs = (HashMap) request.getAttribute("tabs");
  List cmds = (List) request.getAttribute("cmds");
  String args = StringUtils.avoidNull(request.getParameter("args"));
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 
 |
<% for (int i=0; i<cmds.size(); i++) { %>
 <% if (cmds.get(i).toString().equals(cmd))  { %>
      <%=tabs.get(cmds.get(i))%>
 <% } else { %>
      <A HREF="index.html?cmd=<%=cmds.get(i)%>&<%=args%>"><%=tabs.get(cmds.get(i))%></A>
 <% } %>
 |
<%}%>

