<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>Ashkelon - Settings</TITLE>
  <jsp:include page="includes.html" flush="true"/>
</HEAD>

<BODY onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true" />

<div CLASS="pagebody">

<FORM METHOD="POST" ACTION="config.do">
<B>User Interface</B><BR>
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("basic/".equals(ui)) ? "CHECKED" : "" )%> VALUE="classic">Classic (javadoc-style)<BR>
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("".equals(ui)) ? "CHECKED" : "" )%> VALUE="modern">Modern/DHTML (only for Mozilla 1.x, IE5.x or IE6.x)
<br/>
<input type="radio" name="ui_config" <%=(("xul/".equals(ui)) ? "CHECKED" : "")%> VALUE="xul" />XUL (requires mozilla 1.x) (not working just yet -- only one page ported and not well)
<BR><BR>
<INPUT TYPE="SUBMIT" VALUE="Submit">
</FORM>


</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
