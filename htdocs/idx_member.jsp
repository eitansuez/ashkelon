<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
    needToDisplayResults = new Boolean(false);
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon Member Index</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true">
  <jsp:param name="element_type" value="member" />
</jsp:include>

<div class="pagebody">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  List results = (List) request.getAttribute("results");
  request.setAttribute("members", results);
  String emptyMsg = "No members found";
 %>

<div id="member_index">
<jsp:include page="member_list.jsp" flush="true">
  <jsp:param name="caption" value="Member Index" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="true" />
  <jsp:param name="members_type" value="all" />
  <jsp:param name="div_id" value="member_index" />
</jsp:include>
</div>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<form method="get" action="idx.member.do">
  <input type="hidden" name="start" value="<%=request.getAttribute("next")%>">
  <button type="submit"
          style="background-color: #dddddd; font-size: 8pt;"
          accesskey="N"><u>N</u>ext &gt;</button>
</form>
<% } // end if %>


<% } else { %>

<p><b>Member Index Page.</b></p>

<p>Use the A-Z buttons above to browse packages alphabetically by name.</p>

<% } %>


</div>


<c:import url="footer.html" />

</body>
</html>
