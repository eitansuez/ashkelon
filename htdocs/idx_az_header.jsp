<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#F9D362";

  String selectedTab = (String) ServletUtils.getRequestParam(request, "start");
  String elementType = ServletUtils.getRequestParam(request, "element_type");
  
  Map tabs = new HashMap();
  String[] entries = new String[26];
  for (char c='A'; c<='Z'; c++)
  {
    entries[c-'A'] = (new Character(c)).toString();
    tabs.put(entries[c-'A'], "idx."+elementType);
  }
%>

<!-- <LINK REL="stylesheet" TYPE="text/css" HREF="l2_hdr.css"></LINK> -->
<jsp:include page="l2_hdr_style.jsp" flush="true">
  <jsp:param name="selected_color" value="<%=selectedColor%>" />
</jsp:include>


<TABLE CLASS="tab2" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
  <TR>
     <TD CLASS="tab_pad2"></TD>
<% for (int i=0; i<entries.length; i++) { %>
 <% if (entries[i].equals(selectedTab))  { %>
    <TD ID="<%=entries[i]%>_tabchild" CLASS="tab_selected_tab2">
      <%=entries[i]%>
    </TD>
 <% } else { %>
    <TD ID="<%=entries[i]%>_tabchild" CLASS="tab_tab2" onClick="location.href='<%=tabs.get(entries[i])%>.do?start=<%=entries[i]%>';">
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

<br/>

