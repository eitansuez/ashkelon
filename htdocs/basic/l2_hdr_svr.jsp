<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  String cmd = (String) request.getAttribute("cmd");
  Map tabs = (HashMap) request.getAttribute("tabs");
  List cmds = (List) request.getAttribute("cmds");
  String args = StringUtils.avoidNull(request.getParameter("args"));
%>

 |
<% for (int i=0; i<cmds.size(); i++) { %>
 <% if (cmds.get(i).toString().equals(cmd))  { %>
      <%=tabs.get(cmds.get(i))%>
 <% } else { %>
      <A HREF="<%=cmds.get(i)%>.do?<%=args%>"><%=tabs.get(cmds.get(i))%></A>
 <% } %>
 |
<%}%>

