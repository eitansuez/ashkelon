<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% if (needToDisplayResults.booleanValue()) { %>
  <title>Ashkelon - Member Search Results</title>
<% } else { %>
  <title>Ashkelon - Lookup Members</title>
<% } %>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="cleanTitles();">

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

<% if (needToDisplayResults.booleanValue()) { %>
  <jsp:include page="member_results.jsp" flush="true"/>
<% } else { %>
  <!-- <P>Or if you're _really_ lazy, just type "java.util.Map" or just "Map" in the search field below:</P> -->
  <jsp:include page="search_form.jsp" flush="true"/>
<!--   <jsp:include page="member_form_1.jsp" flush="true"/> -->
<% } %>

</div>

<c:import url="footer.html" />

</body>
</html>

