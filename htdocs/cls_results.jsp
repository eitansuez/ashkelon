<%@ page info="class search results component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  String searchField = ServletUtils.getRequestParam(request, "searchField");
  String emptyMsg = "Found no classes matching <I>" + searchField + "</I>";
  List found = (List) request.getAttribute("cls_list");
  request.setAttribute("classes", found);
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<div id="search_results">
  <jsp:include page="class_list.jsp" flush="true">
    <jsp:param name="caption" value="Class Search Results" />
    <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
    <jsp:param name="display_qualified" value="true" />
    <jsp:param name="classes_type" value="all" />
    <jsp:param name="div_id" value="search_results" />
  </jsp:include>
</div>

