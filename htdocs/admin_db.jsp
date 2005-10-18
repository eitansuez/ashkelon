<%@ page info="admin page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  DBMgr dbmgr = DBMgr.getInstance();
  String message = ServletUtils.getRequestParam(request, "message");
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Admin Page</title>
  <jsp:include page="includes.jsp" flush="true" />
</head>

<body>

<div class="pagebody">

<jsp:include page="main_header.jsp" flush="true"/>
<%-- <jsp:include page="sub_header.jsp" flush="true"/> --%>

<p>Welcome to Ashkelon...eitan.</p>

<fieldset>
<legend><b>Database Connection Pooling</b></legend>
<p>Pool Status: <%=dbmgr.getPoolStatus()%></p>
<p class="message"><%=message%></p>

<form method="POST" action="admin.db.do">
  <input type="HIDDEN" name="command" value="resetconns">
  <button type="SUBMIT">Reset Connections</button>
</form>
</fieldset>

<fieldset>
<legend>Cache</legend>

<ul>
<%
  Enumeration attributes = application.getAttributeNames();
  String att;
  Object val;
  int size;
  while (attributes.hasMoreElements())
  {
    att = (String) attributes.nextElement();
    val = application.getAttribute(att);
    if (val instanceof List)
      size = ((List) val).size();
    else if (val instanceof Map)
      size = ((Map) val).size();
    else
      continue;
 %>
   <li><b><%=att%></b>: <%=size%></li>
 <%
  }
 %>
</ul>

<form method="POST" action="admin.db.do">
  <input type="hidden" name="command" value="resetcache">
  <button type="submit">Reset Cache</button>
</form>
</fieldset>

</div>

<c:import url="footer.html" />

</body>
</html>
