<%@ page info="help documentation" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: May 2001
--%>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>dbdoc Help</TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="global.css"></LINK>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">
    <IFRAME SRC="help/main.html" WIDTH="100%" HEIGHT="70%"></IFRAME>
  </DIV>


  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
