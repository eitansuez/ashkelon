<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  List numByAuthor = (List) request.getAttribute("classCounts");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Class Stats</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<div class="pagebody" align="center">

<table class="columnar">
<caption>Class count by Author</caption>
<tr>
<%  
  int n = numByAuthor.size();
  int numCols = 3;
  int modulus = n%numCols;
  int numRows = n/numCols + ((modulus==0) ? 0 : 1);

  String numAuthorClasses;
  Author author;
  
  int start, end, numtoskip;
  for (int col=0; col<numCols; col++)
  {
    start = numRows * col;
    end = start + numRows;
    numtoskip = numCols - modulus;
    if (col == numCols-1 && modulus > 0)
      end = end - numtoskip;
  %>
  
  <!-- COLUMN <%=col%> -->
  <td valign="top">
   <table rules="rows" cellspacing="0" cellpadding="3" bordercolor="white">
  
    <%  
    for (int i=start; i<end; i++)
     {
      author = (Author) ((List) numByAuthor.get(i)).get(0);
      numAuthorClasses = (String) ((List) numByAuthor.get(i)).get(1);
   %>
  <tr>
    <td><a href="author.do?id=<%=author.getId()%>" class="author"><%=author.getName()%></a></td>
    <td align="right"><%=numAuthorClasses%></td>
  </tr>

  <% } %>
 
    </table>
   </td>
  
    <%
     if (col < numCols - 1)
     { %>  
      <!-- padding -->
      <td width="20"></td>
  <% } %>
 
<% } %>
  
</tr>
</table>

</div>

<c:import url="footer.html" />

</body>
</html>

