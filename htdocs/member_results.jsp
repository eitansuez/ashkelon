<%@ page info="class search results component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  List found = (List) request.getAttribute("member_list");
  String searchField = ServletUtils.getRequestParam(request, "searchField");
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (found.size() > 30) { %>
<p><%=found.size()%><%=found.size()==99 ? "+" : ""%> matching entries.  Try to narrow your search.</p>
<% } %>


<%
  request.setAttribute("members", found);
  String emptyMsg = "Found no members matching <I>"+ searchField +"</I>";
 %>
   

<div id="search_results">
<jsp:include page="member_list.jsp" flush="true">
  <jsp:param name="caption" value="Member Search Results" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="true" />
  <jsp:param name="members_type" value="all" />
  <jsp:param name="div_id" value="search_results" />
</jsp:include>
</div>


