<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  Map tabs = new HashMap();
  tabs.put("apis", "APIs");
  tabs.put("pkg", "Packages");
  tabs.put("search", "Search");
  tabs.put("idx", "Index");
  tabs.put("stats.general", "Stats");
  tabs.put("authors", "Authors");
  
  String[] cmds = {"apis", "pkg", "search", "idx", "stats.general", "authors"};

  String cmd = ServletUtils.getCommand(request);
  String area = "";
  if (cmd != null)
    area = StringUtils.split(cmd,".")[0];
%>

<style type="text/css">
.menuitem
{
  padding: 0 10 0 10;
  font-weight: bold;
  color: black;
}
</style>


<table width="100%" cellpadding="3" cellspacing="0">
<tr style="background-color: #cdcdcd;">
<td align="left">
<% for (int i=0; i<cmds.length; i++)
  { %>
      <a class="menuitem" href="<%=request.getContextPath()%>/<%=cmds[i]%>.do"><%=tabs.get(cmds[i])%></a>
<%}%>
</td>
<td align="right">

<a class="menuitem" href="contact.do">Contact</a>
<a class="menuitem" href="config.do">Settings</a>
<a class="menuitem" href="help.do">Help</a>

</td>
</tr>
</table>

<div style="border-bottom: 1px solid black;"></div>


<%-- SECTION:  NAVIGATION TRAIL --%>
<% String style = request.getParameter("style"); %>
<% if (style==null || !style.equals("simple")) { %>

<%
  LinkedList trail = (LinkedList) session.getAttribute("trail");
 %>

<div style="background-color: beige; padding: 3 3 3 3; border-bottom: 1px solid black;" title="Navigation Trail">
  <%
  String[] uriLabelPair = null;
  String uri, caption, itemtype;
  for (int i=0; i<trail.size()-1; i++)
  {
    if (i==0) out.println("Trail: ");
    uriLabelPair = (String[]) trail.get(i);
    uri = uriLabelPair[0];
    caption = uriLabelPair[1];
    itemtype = uriLabelPair[2];
  %>
  <% if (StringUtils.isBlank(itemtype)) { %>
    <a href="<%=uri%>"><%=caption%></a>
  <% } else { %>
    <a href="<%=uri%>"><span class="<%=itemtype%>"><%=caption%></span></a>
  <% } // end if %>
  
    <% if (i < trail.size() - 2) out.println("<img src=\""+request.getContextPath()+"/images/arrow_rt.gif\">"); %>
  <% } %>

  <%-- 
    <form method="GET" action="<%=request.getContextPath()%>/trail.reset.do">
  <td align="right">
    <% if (trail.size() > 3) { %>
      <button type="submit" class="footer" title="Reset navigation trail" STYLE="background-color: #dddddd;">Reset Trail</button>
    <% } %>
  </td>
    </form>
   --%>
</div>

<% } %>

<br />

<div style="float: right;">
  <jsp:include page="legend.html" />
</div>
<div style="float: right;">
  <% if (!("/search.jsp".equals(request.getServletPath()))) { %>
    <jsp:include page="search_form_plug.jsp" flush="true" />
  <% } %>
</div>
<div style="clear: both;">&nbsp;</div>

