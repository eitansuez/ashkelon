<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  String cmd = ServletUtils.getCommand(request);
  Map tabs = (HashMap) request.getAttribute("tabs");
  List cmds = (List) request.getAttribute("cmds");
  String titlePrefix = request.getParameter("title_prefix");
%>

 <tabs>
<% for (int i=0; i<cmds.size(); i++) { %>
    <tab label="<%=tabs.get(cmds.get(i))%>" />
<% } %>
 </tabs>

