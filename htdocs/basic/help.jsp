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
  
    <!-- the hope here is that opera & netscape 6 will process the iframe tag and ignore the ilayer tag
        while netscape 4 will process the ilayer tag instead -->
        
    <IFRAME SRC="help/ns_main.html" WIDTH="100%" HEIGHT="80%"></IFRAME>
    
    <ILAYER SRC="help/ns_help.html" WIDTH="100%" HEIGHT="80%"></ILAYER>
    
  </DIV>
  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>

