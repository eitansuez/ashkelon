<%@ page info="command not found page" import="org.ashkelon.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String title = ServletUtils.getRequestParam(request, "title");
  String description = ServletUtils.getRequestParam(request, "description");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>ashkelon - <%=title%></title>
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body>

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

  <p class="message"><%=title%></p>

  <div id="detail" style="margin-top: 2em;">
    <%=description%>
  </div>

</div>

<c:import url="footer.html" />

</body>
</html>

