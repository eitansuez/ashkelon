<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  List numByPackage = (List) request.getAttribute("classCounts");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Class Stats</title>
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<div class="pagebody">

<table class="columnar" align="center">
<caption>Class count by package</caption>
<tr valign="top">
<%  
  int n = numByPackage.size();
  int numCols = 3;
  int modulus = n%numCols;
  int numRows = n/numCols + ((modulus==0) ? 0 : 1);

  String numPkgClasses;
  JPackage pkg;
  
  int start, end, numtoskip;
  for (int col=0; col<numCols; col++)
  {
    start = numRows * col;
    end = start + numRows;
    numtoskip = numCols - n%numCols;
    if (col == numCols-1 && modulus>0)
      end = end - numtoskip;
  %>
  
  <!-- COLUMN <%=col%> -->
  <td style="padding: 0.5em 1em;">
   <table rules="rows" cellspacing="0" cellpadding="3">
  
  <% 
  for (int i=start; i<end; i++)
   {
    pkg = (JPackage) ((List) numByPackage.get(i)).get(0);
    numPkgClasses = (String) ((List) numByPackage.get(i)).get(1);
 %>
    <% if (Integer.parseInt(numPkgClasses)>50)
    { %>
  <tr class="highlight">
    <% } else { %>
  <tr>
    <% } %>
    <td><a href="pkg.main.do?id=<%=pkg.getId()%>"><%=pkg.getName()%></a></td>
    <td align="right"><%=numPkgClasses%></td>
  </tr>
<% } %>
 
  </table>
 </td>
  
<% } %>
  
</tr>
</table>

</div>

<c:import url="footer.html" />

</body>
</html>
