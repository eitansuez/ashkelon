<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
%>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=cls.getName()%> Cross References</TITLE>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY BGCOLOR="white">

  <jsp:include page="main_header.jsp" flush="true"/>
  <jsp:include page="cls_header_svr.jsp" flush="true"/>
  
  <jsp:include page="cls_info.jsp" flush="true"/>
  <jsp:include page="doc_footer.jsp" flush="true"/>
  <HR>

  <jsp:include page="cls_inner.jsp" flush="true"/>
  <BR>
  <jsp:include page="cls_member.jsp" flush="true"/>

  <BR>
  <jsp:include page="cls_xref.jsp" flush="true"/>
  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>