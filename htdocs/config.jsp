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
<jsp:include page="main_header.jsp" flush="true" />


<div class="pagebody">

<form method="POST" action="config.do">
<b>User Interface</b><br/>
<input type="radio" name="ui_config" <%=(("basic/".equals(ui)) ? "checked" : "" )%> value="classic">Classic ("javadoc" look & feel)<br/>
<input type="radio" name="ui_config" <%=(("".equals(ui)) ? "checked" : "" )%> value="modern">Modern/dhtml
<br/>
<input type="radio" name="ui_config" <%=(("xul/".equals(ui)) ? "checked" : "")%> value="xul" />XUL (requires Mozilla) (experimental pre-alpha)
<br/><br/>
<input type="SUBMIT" value="Submit">
</form>


</div>

<c:import url="footer.html" />

</body>
</html>

