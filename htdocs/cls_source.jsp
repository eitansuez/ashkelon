<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,org.ashkelon.pages.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cls_name = (String) request.getAttribute("cls_name");
  String source_file = (String) request.getAttribute("source_file");
  String html_file = (String) request.getAttribute("html_file");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Source file: <%=cls_name%></title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body>
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <div class="pagebody">

  <% if (StringUtils.isBlank(source_file)) { %>
    <p class="error-msg">Unable to locate source file for class <%=cls_name%></p>
  <% } else { %>
    <iframe class="source-box" src="<%=html_file%>" />
 <% } %>
  
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>

