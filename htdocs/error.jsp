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
      display: '';
      font-family: monospace;
    }
  </style>

  <script>
  function errorToggleDetail()
  {
    var isvisible = toggleDisplay("errorDetail");
    document.forms[0].elements[0].value = (isvisible == "hidden") ? "Hide Detail" : "Show Detail";
  }
  </script>
</head>

<body>

<jsp:include page="main_header.jsp" flush="true">
  <jsp:param name="style" value="simple"/>
</jsp:include>

<div class="PAGEBODY">

  <p class="message">An error has occurred.</p>

  Error: <%=exception%>
  
  <form name="frm">
   <button accesskey="e" onClick="errorToggleDetail();">Hide Detail</button>
  </form>
    
  <pre id="errorDetail">
    <% 
    	exception.printStackTrace(new PrintWriter(out));
    %>
  </pre>

</div>

<jsp:include page="footer.html" flush="true" />

</body>
</html>

