<%@ page info="page" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon Index</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<div class="pagebody">
  <p>Use the Index to browse through packages, classes & interfaces, or members (fields, method, & constructors).</p>
  <p>Click on one of the index tabs to proceed.</p>
</div>

<jsp:include page="footer.html" flush="true"/>

</body>
</html>
