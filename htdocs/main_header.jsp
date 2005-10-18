<%@ page info="tabbed heading" import="java.util.*" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%
  Map tabs = new HashMap();
  tabs.put("apis", "APIs");
  tabs.put("pkg", "Packages");
  tabs.put("search", "Search");
  tabs.put("idx", "Index");
  tabs.put("stats.general", "Stats");
  tabs.put("authors", "Authors");
  
  String[] cmds = {"apis", "pkg", "search", "idx", "stats.general", "authors"};
%>

<style type="text/css">
.menuitem
{
  padding: 0 0.75em;
  font-weight: bold;
  color: black;
}
</style>

<div style="background-color: #cdcdcd; border-bottom: 1px solid black;">
<div style="float: left; padding: 0.25em;">
<% for (int i=0; i<cmds.length; i++)
  { %>
      <a class="menuitem" href="<%=request.getContextPath()%>/<%=cmds[i]%>.do"><%=tabs.get(cmds[i])%></a>
<%}%>
</div>
<div style="float: right; padding: 0.25em;">
<a class="menuitem" href="contact.do">Contact</a>
<a class="menuitem" href="config.do">Settings</a>
<a class="menuitem" href="help.do">Help</a>
</div>

<div style="clear: both;"></div>
</div>

<u2d:include page="trail.jsp" dynamic="true" />

<div style="float: left;">
 <img src="images/logo32.gif" style="padding: 0.5em 1.5em; margin-top: 0; vertical-align: top;" />
</div>
<br />
<div style="float: right;">
  <jsp:include page="legend.html" />
</div>


<div style="float: right;">
  <u2d:include page="search_form_plug.jsp" dynamic="true" />
</div>


<div style="clear: both;">&nbsp;</div>

