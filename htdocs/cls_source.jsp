<%@ page info="main class view" import="java.io.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Source file: <c:out value="${name}" /></title>
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body>
  <u2d:include page="main_header.jsp" dynamic="true" />
  
  <div class="pagebody">

  <% File sourceFile = (File) request.getAttribute("source_file"); %>
  <% if (sourceFile == null || !sourceFile.exists()) { %>
    <p class="error-msg">Unable to locate source file for class <c:out value="${name}" /></p>
  <% } else { %>
    <iframe class="source-box" src="<c:out value="${html_file}" />" />
  <% } %>
  
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>

