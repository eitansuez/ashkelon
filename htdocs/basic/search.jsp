<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
    
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
  String srch_type = "";
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
<% if (needToDisplayResults.booleanValue()) { %>
	<TITLE>dbdoc - Search Results</TITLE>
<% } else { %>
	<TITLE>dbdoc - Search</TITLE>
<% } %>

</HEAD>

<BODY BGCOLOR="white">

<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

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

</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>