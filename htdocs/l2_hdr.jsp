<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  Map tabs = (HashMap) request.getAttribute("tabs");
  List cmds = (List) request.getAttribute("cmds");
  String titlePrefix = request.getParameter("title_prefix");
%>

<link rel="stylesheet" TYPE="text/css" HREF="l2_hdr.css" />

<script src="page.js"></script>
<script>
  var cmds = new Array();
  <% for (int i=0; i<cmds.size(); i++) { %>
  cmds[<%=i%>] = "<%=cmds.get(i)%>";
  <% } %>
</script>

<table width="100%" cellpadding="1" cellspacing="0" style="empty-cells: show;">
  <tr>
    <td class="tab_pad" width="10">&nbsp;</td>
    <% for (int i=0; i<cmds.size(); i++) { %>
    <td id="<%=cmds.get(i)%>_tabchild" class="tab_tab" disabled="true" onClick="togglePage('<%=cmds.get(i)%>', '<%=titlePrefix%> <%=tabs.get(cmds.get(i))%>');">
      <%=tabs.get(cmds.get(i))%>
    </td>
    <% if (i < cmds.size() - 1)  { %>
    <td class="tab_buffer" width="5">&nbsp;</td>
    <% } %>
    <%}%>
    <td class="tab_pad" width="10">&nbsp;</td>
  </tr>
  <tr><td class="bottomrule" colspan="<%=cmds.size()*2+1%>" height="4"></td></tr>
</table>

