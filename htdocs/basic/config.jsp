<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>dbdoc - Settings</TITLE>
</HEAD>

<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>

<FORM METHOD="POST" ACTION="config.do">
<B>User Interface</B><BR>
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("basic/".equals(ui)) ? "CHECKED" : "" )%> VALUE="classic">Classic (javadoc-style)<BR>
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("".equals(ui)) ? "CHECKED" : "" )%> VALUE="modern">Modern/DHTML (only for Mozilla 1.0+ or IE5.0+)
<br/>
<input type="radio" name="ui_config" <%=(("xul/".equals(ui)) ? "CHECKED" : "")%> value="xul"/>XUL (a work in progress; requires mozilla)
<BR><BR>
<INPUT TYPE="SUBMIT" VALUE="Submit">
</FORM>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
