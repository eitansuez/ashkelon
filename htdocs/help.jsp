<%@ page info="help documentation" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: May 2001
--%>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon Help</title>
  <link rel="stylesheet" type="text/css" href="global.css" />
  <jsp:include page="includes.html" flush="true"/>
</head>

<body>
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <div class="pagebody">
    <iframe src="help/main.html" width="100%" height="70%"></iframe>
  </div>

  <c:import url="footer.html" />

</body>
</html>
