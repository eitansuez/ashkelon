<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

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

<HTML>
<HEAD>
	<TITLE>dbdoc - Settings</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<FORM METHOD="POST" ACTION="index.html">
<B>User Interface</B><BR>
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="config">
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("basic/".equals(ui)) ? "CHECKED" : "" )%> VALUE="classic">Classic (javadoc-style)<BR>
<INPUT TYPE="RADIO" NAME="ui_config" <%=(("".equals(ui)) ? "CHECKED" : "" )%> VALUE="modern">Modern/DHTML (only for Mozilla 1.0+ or IE5.0+)
<BR><BR>
<INPUT TYPE="SUBMIT" VALUE="Submit">
</FORM>


</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
