<%@ page info="command not found page" import="java.io.*,org.ashkelon.util.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc - Command Not Found</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
    #errorDetail
    {
      display: '';
    }
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  function notFoundToggleDetail()
  {
    var isvisible = toggleDisplay("errorDetail");
    document.forms[0].elements[0].value = (isvisible == "hidden") ? "Hide Detail" : "Show Detail";
  }
  </SCRIPT>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

  <P CLASS="message">An error has occurred</P>

  <FORM>
  <BUTTON ACCESSKEY="e" onClick="notFoundToggleDetail();">Hide Detail</BUTTON>
  </FORM>
    
  <SPAN ID="errorDetail">
    The requested command: <B><%=cmd%></B> was not found
  </SPAN>

</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>