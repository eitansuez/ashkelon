<%@ page info="page" import="java.util.*, org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
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
  <title>Ashkelon Class Index</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true" />
<jsp:include page="idx_header.jsp" flush="true" />

<jsp:include page="idx_az_header.jsp" flush="true">
  <jsp:param name="element_type" value="class" />
</jsp:include>

<div class="pagebody">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  String startFrom = request.getParameter("startFrom");
  String emptyMsg = "";
  if (StringUtils.isBlank(startFrom))
  {
    emptyMsg = "No classes found";
  } else
  {
    emptyMsg =   "Found no classes passed "+startFrom;
  }
  List found = (List) request.getAttribute("results");
  request.setAttribute("classes", found);
%>
 
<div id="class_index">
  <jsp:include page="class_list.jsp" flush="true">
    <jsp:param name="caption" value="Class Index" />
    <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
    <jsp:param name="display_qualified" value="true" />
    <jsp:param name="classes_type" value="all" />
    <jsp:param name="div_id" value="class_index" />
  </jsp:include>
</div>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<form method="get" action="idx.class.do">
  <input type="hidden" name="start" value="<c:out value="${next}" />" />
  <button type="submit"
          style="background-color: #dddddd; font-size: 8 pt;"
          accesskey="N"><U>N</U>ext &gt;</button>
</form>
<% } // end if %>

<% } else { %>

<p><b>Class Index Page.</b></p>

<p>Use the A-Z buttons above to browse classes alphabetically by name.</p>

<% } %>

</div>

<c:import url="footer.html" />

</body>
</html>
