<%@ page info="main class view" import="java.util.*,java.io.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,org.ashkelon.pages.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String clsName = (String) request.getAttribute("cls_name");
  File sourceFile = (File) request.getAttribute("source_file");
  String htmlFile = (String) request.getAttribute("html_file");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Source file: <%=clsName%></title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body>
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <div class="pagebody">

  <% if (sourceFile == null || !sourceFile.exists()) { %>
    <p class="error-msg">Unable to locate source file for class <%=clsName%></p>
  <% } else { %>
    <iframe class="source-box" src="<%=htmlFile%>" />
 <% } %>
  
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>

