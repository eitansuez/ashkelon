<%@ page info="top page: api list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - API Listing</title>
  <c:import url="includes.html" />
</head>

<body onLoad="cleanTitles();">

<c:import url="main_header.jsp" />

<div class="pagebody">

<h3>Welcome to Ashkelon</h3>

<p>
Ashkelon is an online reference to Java API documentation (see the help documentation for more information).  Ashkelon is <b>open source</b> and available for download off sourceforge.net as project <a target="_new" href="http://sourceforge.net/projects/ashkelon/">ashkelon</a>.
</p>

<table BORDER="1" CELLPADDING="5" CELLSPACING="0" RULES="rows" ALIGN="CENTER" bordercolor="black">
<caption>API Listing</caption>
<tbody>
 <c:forEach items="${apilist}" var="api">
    <tr>
      <td VALIGN="TOP">
        <a href="api.main.do?id=<c:out value="${api.id}" />">
          <span class="api" title="<c:out value="${api.summaryDescription}" />">
            <c:out value="${api.name}" />
          </span>
        </a>
      </td>
      <td VALIGN="TOP">
        <c:out value="${api.version}" />
      </td>
      <td VALIGN="TOP">
        <c:out value="${api.summaryDescription}" />
      </td>
    </tr>
  </c:forEach>
</tbody>
</table>

</div>

<c:import url="footer.html" />


</body>
</html>

