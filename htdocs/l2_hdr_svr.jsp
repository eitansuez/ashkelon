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

<%-- SECTION: COMPONENT STYLES --%>
<LINK REL="stylesheet" TYPE="text/css" HREF="l2_hdr.css"></LINK>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<TABLE CLASS="tab" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
  <TR>
     <TD CLASS="tab_pad"></TD>
<% for (int i=0; i<cmds.size(); i++) { %>
 <% if (cmds.get(i).toString().equals(cmd))  { %>
    <TD ID="<%=cmds.get(i)%>_tabchild" CLASS="tab_selected_tab">
      <%=tabs.get(cmds.get(i))%>
    </TD>
 <% } else { %>
 
    <TD ID="<%=cmds.get(i)%>_tabchild" CLASS="tab_tab" onClick="setCookie('pagecontext',location.search);location.href='index.html?cmd=<%=cmds.get(i)%>&<%=args%>';">
      <%=tabs.get(cmds.get(i))%>
    </TD>
 <% } %>
 <% if (i < cmds.size() - 1)  { %>
    <TD CLASS="tab_buffer"></TD>
 <% } %>
<%}%>
     <TD CLASS="tab_pad" WIDTH="*%"></TD>
  </TR>
  <TR><TD CLASS="tab" COLSPAN=<%=cmds.size()*2+1%> HEIGHT="4"></TD></TR>
</TABLE>
