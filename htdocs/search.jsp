<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
 
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
  String srch_type = "";
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% if (needToDisplayResults.booleanValue()) { %>
  <title>Ashkelon - Search Results</title>
<% } else { %>
  <title>Ashkelon - Search</title>
<% } %>
  <jsp:include page="includes.jsp" flush="true" />
</head>

<body>

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

<%-- the following if clause is useless; when search form submits, it reroutes to a different page --%>
<% if (needToDisplayResults.booleanValue()) { %>
  <% if (srch_type.equals("class")) { %>
    <jsp:include page="cls_results.jsp" flush="true"/>
  <% } else { %>
    <jsp:include page="member_results.jsp" flush="true"/>
  <% } %>
<% } else { %>
  <!-- <P>Or if you're _really_ lazy, just type "java.util.Map" or just "Map" in the search field below:</P> -->
  <jsp:include page="search_form.jsp" flush="true"/>
<% } %>

</div>

<c:import url="footer.html" />

</body>
</html>

