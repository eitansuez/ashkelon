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
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  function init()
  {
    if (IE)
      document.onclick = expandCollapse;
    else
      document.addEventListener("click", expandCollapse, false);
  }
  </SCRIPT>
</HEAD>

<BODY onLoad="init();loadCookies();cleanTitles();" onUnload="saveCookies();">

  <jsp:include page="main_header.jsp" flush="true"/>
  <jsp:include page="cls_header_svr.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">
    <jsp:include page="cls_info.jsp" flush="true"/>
    <jsp:include page="cls_xref.jsp" flush="true"/>
  </DIV>
  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>