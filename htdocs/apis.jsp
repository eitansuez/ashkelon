<%@ page info="top page: api list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - API Listing</title>
  <c:import url="includes.html" />
  <style>
  table#apis
  {
    border: 1px solid gray;
    border-width: 1px 2px 2px 1px;
  }
  table#apis tr
  {
    vertical-align: top;
  }
  table#apis tr.odd
  {
    background-color: beige;
  }
  table#apis td
  {
    padding: 0.3em 0.7em;
    border-bottom: 1px dotted gray;
  }
  </style>
</head>

<body onLoad="cleanTitles();">

<jsp:include page="main_header.jsp" flush="true" />


<div class="pagebody">

<h3>Welcome to Ashkelon</h3>

<p>
Ashkelon is an online reference to Java API documentation (see the help documentation for more information).  Ashkelon is <b>open source</b> and available for download off sourceforge.net as project <a target="_new" href="http://sourceforge.net/projects/ashkelon/">ashkelon</a>.
</p>

<table id="apis" align="center" rules="rows" cellspacing="0" width="100%">
<caption>API Listing</caption>
<tbody>
 <c:forEach items="${apilist}" var="api" varStatus="status">
   <c:if test="${status.index % 2 == 1}">
     <tr class="odd">
   </c:if>
   <c:if test="${status.index % 2 == 0}">
     <tr class="even">
   </c:if>
      <td width="150">
        <c:out value="${status.index + 1}." />
        <a href="api.main.do?id=<c:out value="${api.id}" />">
          <span class="api" title="<c:out value="${api.summaryDescription}" />">
            <c:out value="${api.name}" />
          </span>
        </a>
      </td>
      <td align="right">
        <c:out value="${api.version}" />
      </td>
      <td>
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

