<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - Settings</title>
  <c:import url="includes.html" />
</head>

<body onLoad="cleanTitles();">
<c:import url="main_header.jsp" />

<div class="pagebody">

<form method="POST" action="config.do">
<b>User Interface</b><br/>
<input type="radio" NAME="ui_config" <%=(("basic/".equals(ui)) ? "CHECKED" : "" )%> VALUE="classic">Classic ("javadoc" look & feel)<br/>
<input type="radio" NAME="ui_config" <%=(("".equals(ui)) ? "CHECKED" : "" )%> VALUE="modern">Modern/DHTML (only Mozilla and IE for now)
<br/>
<input type="radio" name="ui_config" <%=(("xul/".equals(ui)) ? "CHECKED" : "")%> VALUE="xul" />XUL (requires Mozilla) (experimental pre-alpha)
<br/><br/>
<input type="SUBMIT" value="Submit">
</form>


</div>

<c:import url="footer.html" />

</body>
</html>

