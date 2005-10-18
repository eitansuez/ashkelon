<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon Package Index</title>
  <jsp:include page="includes.jsp" flush="true"/>
  <style>
  ol#pkg-list
  {
  }
  ol#pkg-list li
  {
    border-bottom: 1px dotted gray;
  }
  </style>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true">
  <jsp:param name="element_type" value="package" />
</jsp:include>

<div class="pagebody">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  List results = (List) request.getAttribute("results");
  JPackage pkg;
 %>
<ol id="pkg-list">
 <% for (int i=0; i<results.size(); i++) { %>
 <%  pkg = (JPackage) results.get(i); %>
   <li> 
     <a href="pkg.main.do?id=<%=pkg.getId()%>">
      <span class="package"><%=pkg.getName()%></span>
     </a>
   </li>
 <% } %>
</ol>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<form method="get" action="idx.package.do" style="text-align: right;">
  <input type="hidden" name="start" value="<%=request.getAttribute("next")%>">
  <button type="submit"
          style="background-color: #dddddd; font-size: 8pt;"
          accesskey="n"><U>N</U>ext &gt;</button>
</form>
<% } // end if %>

<% } else { %>

<p><b>Package Index Page.</b></p>

<p>Use the A-Z buttons above to browse packages alphabetically by name.</p>

<% } %>


</div>

<c:import url="footer.html" />

</body>
</html>
