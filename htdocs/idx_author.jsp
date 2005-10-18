<%@ page info="page" import="java.util.*, org.ashkelon.util.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

 
<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<title>Ashkelon Author Index</title>
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true">
  <jsp:param name="element_type" value="author" />
</jsp:include>

<div class="pagebody">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  String startFrom = request.getParameter("startFrom");
  String emptyMsg = "";
  if (StringUtils.isBlank(startFrom))
  {
    emptyMsg = "No authors found";
  } else
  {
    emptyMsg =   "Found no authors passed "+startFrom;
  }
  List authors = (List) request.getAttribute("results");
%>
 
<div id="author_index">
<ol>
<% Author author;
   for (int i=0; i<authors.size(); i++)
   {
     author = (Author) authors.get(i);
   %>
  <li><a href="author.do?id=<%=author.getId()%>" class="author"><%=author.getName()%></a></li>
<% } %>
</ol>

</div>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<form method="get" action="idx.author.do">
  <input type="hidden" name="start" value="<%=request.getAttribute("next")%>">
  <button type="submit"
          style="background-color: #dddddd; font-size: 8pt;"
          accesskey="N"><u>N</u>ext &gt;</button>
</form>
<% } // end if %>

<% } else { %>

<p><b>Author Index Page.</b></p>

<p>Use the A-Z buttons above to browse authors alphabetically by name.</p>

<% } %>

</div>

<c:import url="footer.html" />

</body>
</html>
