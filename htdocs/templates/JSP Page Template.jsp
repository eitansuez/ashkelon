<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Untitled</TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="global.css"></LINK>
  <SCRIPT SRC="global.js"></SCRIPT>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>

<DIV CLASS="PAGEBODY">

<jsp:include page="main_header.jsp" flush="true"/>
<%-- <jsp:include page="sub_header.jsp" flush="true"/> --%>

Welcome to dbdoc.

</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>