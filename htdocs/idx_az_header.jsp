<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String unselectedColor = "#cdcdcd";
  String selectedColor = "#f9d362";

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


<table class="tab2" border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
  <td class="tab_pad2"></td>
<% for (int i=0; i<entries.length; i++) { %>
 <% if (entries[i].equals(selectedTab))  { %>
  <td id="<%=entries[i]%>_tabchild" class="tab_selected_tab2">
    <%=entries[i]%>
  </td>
 <% } else { %>
  <td id="<%=entries[i]%>_tabchild" class="tab_tab2" onClick="location.href='<%=tabs.get(entries[i])%>.do?start=<%=entries[i]%>';">
    <%=entries[i]%>
  </td>
 <% } %>
 <% if (i < entries.length - 1)  { %>
  <td class="tab_buffer2"></td>
 <% } %>
<%}%>
  <td class="tab_pad2" width="*%"></td>
</tr>
<tr><td class="tab2" colspan="<%=entries.length*2+1%>" height="4"></td></tr>
</table>

<br/>

