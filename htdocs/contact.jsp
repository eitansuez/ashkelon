<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: December 2001
--%>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<title>Ashkelon - Contact Information</title>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <style type="text/css">
  li
  {
    margin: 0 0 10 0;
  }
  </style>

</head>

<body onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

<p>

<ul>
<li>If there's an open source API that you'd like to see in the Ashkelon repository; or </li>
<li>If you'd like to license Ashkelon for publishing your own docs on your company intranet; or </li>
<li>For any comments or feedback on this site or on Ashkelon; or</li>
<li>For any questions not already answered in the <a href="issues.do">FAQ</a></li>
</ul>

..please contact:
</p>

<blockquote>
<a href="mailto:eitan-keyword-deux.352d02@u2d.com">Eitan Suez</a> <br/>
UptoData, Inc. <br/>
Austin, Texas
</blockquote>

</div>

<c:import url="footer.html" />

</body>
</html>
