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
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
<% if (needToDisplayResults.booleanValue()) { %>
	<TITLE>Ashkelon - Class Search Results</TITLE>
<% } else { %>
	<TITLE>Ashkelon - Lookup Classes</TITLE>
<% } %>
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="cleanTitles();">

<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<% if (needToDisplayResults.booleanValue()) { %>
  <jsp:include page="cls_results.jsp" flush="true"/>
<% } else { %>
  <!-- <P>Or if you're _really_ lazy, just type "java.util.Map" or just "Map" in the search field below:</P> -->
  <jsp:include page="class_form_1.jsp" flush="true"/>
<% } %>

</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
