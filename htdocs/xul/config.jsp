<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"
  contentType="application/vnd.mozilla.xul+xml" %><?xml version="1.0"?>
<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href="xul/global.css" type="text/css"?>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<window
  xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul"
  xmlns:html="http://www.w3.org/1999/xhtml"
  title="Ashkelon: Settings" orient="vertical">

 <script type="application/x-javascript" src="xul/global.js" />

<jsp:include page="header.jsp" flush="true"/>

<vbox id="body" flex="1">

<html:div style="padding: 10px;">
<html:form method="POST" action="index.html">
<html:b>User Interface</html:b><html:br/>
<html:input type="hidden" name="cmd" value="config" />
<html:input type="radio" name="ui_config" <%=(("basic/".equals(ui)) ? "checked=\"true\"" : "" )%> value="classic"/>Classic (javadoc-style)<html:br/>
<html:input type="radio" name="ui_config" <%=(("".equals(ui)) ? "checked=\"true\"" : "" )%> value="modern"/>Modern/DHTML (only for Mozilla 1.x, IE5.x or IE6.x)
<html:br/>
<html:input type="radio" name="ui_config" <%=(("xul/".equals(ui)) ? "checked=\"true\"" : "")%> value="xul"/>XUL (requires mozilla 1.x) (not working just yet -- only one page ported and not well)
<html:br/><html:br/>
<html:input type="submit" value="Submit" />
</html:form>
</html:div>

</vbox>

<jsp:include page="footer.jsp" flush="true" />

</window>
