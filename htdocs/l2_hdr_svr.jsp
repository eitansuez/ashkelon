<%@ page info="component" import="java.util.*,org.ashkelon.util.*" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  Map tabs = (HashMap) request.getAttribute("tabs");
  List cmds = (List) request.getAttribute("cmds");
  String args = StringUtils.avoidNull(request.getParameter("args"));
%>

<link rel="stylesheet" type="text/css" href="l2_hdr.css" />

<table cellspacing="0" cellpadding="1" width="100%" style="empty-cells: show;">
  <tr>
     <td class="tab_pad" width="10">&nbsp;</td>
<% for (int i=0; i<cmds.size(); i++) { %>
 <% if (cmds.get(i).toString().equals(cmd))  { %>
    <td id="<%=cmds.get(i)%>_tabchild" CLASS="tab_selected_tab">
      <%=tabs.get(cmds.get(i))%>
    </td>
 <% } else { %>
 
    <td id="<%=cmds.get(i)%>_tabchild" CLASS="tab_tab" onClick="setCookie('pagecontext',location.search);location.href='<%=cmds.get(i)%>.do?<%=args%>';">
      <%=tabs.get(cmds.get(i))%>
    </td>
 <% } %>
 <% if (i < cmds.size() - 1)  { %>
    <td class="tab_buffer" width="5">&nbsp;</td>
 <% } %>
<%}%>
     <td class="tab_pad" width="10">&nbsp;</td>
  </tr>
  <tr><td class="bottomrule" colspan="<%=cmds.size()*2+1%>" height="4"></td></tr>
</table>

