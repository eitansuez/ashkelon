<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

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
<tbody>
<tr>
<td class="listing">
<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
%>
<%=i+1%>. 
<a href="cls.main.do?cls_id=<%=cls.getId()%>" CLASS="<%=cls.getClassTypeName()%> %>"><%=cls.getQualifiedName()%></a> <br/>
  <% if (i==(int) classes.size()/2 + 1) { %>
   </td><td class="listing">
  <% } %>
<% } %>
</td>
</tr>
</tbody>
</table>


</div>

<jsp:include page="footer.html" flush="true"/>

</body>
</html>
