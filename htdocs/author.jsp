<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  Author author = (Author) request.getAttribute("author");
  List classes = author.getClasses();
  ClassType cls = null;
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Author: <%=author.getName()%></title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body>

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

<h2 class="author"><%=author.getName()%></h2>

<p>
<%=author.getName()%> has authored the following classes and/or interfaces:
</p>

<div style="margin: 1em 3em;">
<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
%>
<a href="cls.main.do?cls_id=<%=cls.getId()%>" class="<%=cls.getClassTypeName()%> %>"><%=cls.getQualifiedName()%></a><% if (i + 1 < classes.size()) { %>, <% } %>
<% } %>
</div>

<% if (!StringUtils.isBlank(author.getEmail())) { %>
<p>
Email Author at <a href="mailto:<%=author.getEmail()%>"><%=author.getEmail()%></a>
</p>
<% } %>


</div>

<c:import url="footer.html" />

</body>
</html>
