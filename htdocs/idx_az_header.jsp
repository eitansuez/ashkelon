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

<%-- SECTION: COMPONENT STYLES --%>
<!-- <LINK REL="stylesheet" TYPE="text/css" HREF="l2_hdr.css"></LINK> -->
<jsp:include page="l2_hdr_style.jsp" flush="true">
  <jsp:param name="selected_color" value="<%=selectedColor%>" />
</jsp:include>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<TABLE CLASS="tab2" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
  <TR>
     <TD CLASS="tab_pad2"></TD>
<% for (int i=0; i<entries.length; i++) { %>
 <% if (entries[i].equals(selectedTab))  { %>
    <TD ID="<%=entries[i]%>_tabchild" CLASS="tab_selected_tab2">
      <%=entries[i]%>
    </TD>
 <% } else { %>
    <TD ID="<%=entries[i]%>_tabchild" CLASS="tab_tab2" onClick="location.href='index.html?cmd=<%=tabs.get(entries[i])%>&start=<%=entries[i]%>';">
      <%=entries[i]%>
    </TD>
 <% } %>
 <% if (i < entries.length - 1)  { %>
    <TD CLASS="tab_buffer2"></TD>
 <% } %>
<%}%>
     <TD CLASS="tab_pad2" WIDTH="*%"></TD>
  </TR>
  <TR><TD CLASS="tab2" COLSPAN=<%=entries.length*2+1%> HEIGHT="4"></TD></TR>
</TABLE>

<BR>