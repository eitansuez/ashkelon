<%@ page info="error page" isErrorPage="true" import="java.io.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - Error: <%=exception%></title>
  <jsp:include page="includes.html" flush="true"/>

  <style type="text/css">
    #errorDetail
    {
      display: block;
      font-family: monospace;
    }
  </style>

  <script>
  function errorToggleDetail()
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

<jsp:include page="main_header.jsp" flush="true">
  <jsp:param name="style" value="simple"/>
</jsp:include>

<div class="pagebody">

  <p class="message">An error has occurred:</p>

  <blockquote>
    <%=exception%>
  </blockquote>
  
  <button id="toggleBtn" accesskey="e" onClick="errorToggleDetail();">Hide Detail</button>
    
  <pre id="errorDetail">
    <% 
    	exception.printStackTrace(new PrintWriter(out));
    %>
  </pre>

</div>

<jsp:include page="footer.html" flush="true" />

</body>
</html>

