<%@ page info="page" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getCommand(request);
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc Index</TITLE>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY BGCOLOR="white">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<P>Use the Index to browse through packages, classes & interfaces, or members (fields, method, & constructors).</P>

<P>Click on one of the index tabs to proceed.</P>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
