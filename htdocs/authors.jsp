<%@ page info="page" import="java.util.*, org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon Author Index</title>
  <c:import url="includes.html" />
</head>

<body>

<c:import url="main_header.jsp" />

<div class="pagebody">

<%
  List authors = (List) request.getAttribute("authors");
%>

<style>
td.listing
{
  border-right: 1px solid black;
  border-bottom: 1px solid black;
  vertical-align: top;
  width: 33%;
}
</style>

<table width="100%" style="border: thin solid black;" cellpadding="3" cellspacing="0">
<caption>Authors</caption>
<tbody>
<tr>
<td class="listing">
<% Author author;
   int rows = (int) authors.size() / 3 + 1;
   for (int i=0; i<authors.size(); i++)
   {
     author = (Author) authors.get(i);
   %>
   <%=i+1%>.
  <a href="author.do?id=<%=author.getId()%>" class="author"><%=author.getName()%></a> <br/>
  <% if ((i == rows) || (i == rows*2)) { %>
   </td><td class="listing">
  <% } %>
<% } %>
</td>
</tr>
</tbody>
</table>

</div>

<c:import url="footer.html" />


</body>
</html>
