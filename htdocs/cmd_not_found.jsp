<%@ page info="command not found page" import="java.io.*,org.ashkelon.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - Command Not Found</title>
  <jsp:include page="includes.html" flush="true"/>

  <style type="text/css">
    #errorDetail
    {
      display: block;
    }
  </style>

  <script>
  function toggleDetail()
  {
    var detail = document.getElementById("errorDetail");
    var toggleBtn = document.getElementById("toggleBtn");
    var hidden = (detail.style.display == "none");
    if (hidden)
    {
       detail.style.display = "block"; // show
       toggleBtn.innerHTML = "Hide Detail"; // toggle button caption
    }
    else
    {
       detail.style.display = "none";  // hide
       toggleBtn.innerHTML = "Show Detail";  // toggle button caption
    }
  }
  </script>
</head>

<body>

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

  <p class="message">An error has occurred</p>

  <button id="toggleBtn" accesskey="e" onClick="toggleDetail();">Hide Detail</button>

  <div id="errorDetail" style="margin-top: 2em;">
    The requested command: <b><%=cmd%></b> was not found
  </div>

</div>

<c:import url="footer.html" />

</body>
</html>

