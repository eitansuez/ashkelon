<%@ page info="error page" isErrorPage="true" import="java.io.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Ashkelon - Error: <%=exception%></TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
    #errorDetail
    {
      display: '';
      font-family: monospace;
    }
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  function errorToggleDetail()
  {
    var isvisible = toggleDisplay("errorDetail");
    document.forms[0].elements[0].value = (isvisible == "hidden") ? "Hide Detail" : "Show Detail";
  }

  </SCRIPT>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true">
  <jsp:param name="style" value="simple"/>
</jsp:include>

<DIV CLASS="PAGEBODY">

  <P CLASS="message">An error has occurred.</P>

  Error: <%=exception%>
  
  <FORM NAME="frm">
  <BUTTON ACCESSKEY="e" onClick="errorToggleDetail();">Hide Detail</BUTTON>
  </FORM>
    
  <PRE ID="errorDetail">
    <% 
    	exception.printStackTrace(new PrintWriter(out));
    %>
  </PRE>

</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
