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

  String cmd = (String) request.getAttribute("cmd");
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

<script>
// sole purpose of this is to enable 'esc' key hiding of legend if visible (convenience)
// should really be in an htc but i don't believe mozilla supports that
function esc(evt)
{
  if (!evt) evt = window.event;
  var code = evt.keyCode;
  if (!code) code = evt.which;
  if (code != 27) return;
  setVisible("legend", false, "visibility", false);
}
document.onkeypress = esc;
</script>


<table width="100%" cellpadding="3" cellspacing="0">
<tr style="background-color: #cdcdcd;">
<td align="left">
<% for (int i=0; i<cmds.length; i++)
  { %>
      <A CLASS="menuitem" HREF="<%=request.getContextPath()%>/<%=cmds[i]%>.do"><%=tabs.get(cmds[i])%></A>
<%}%>
</td>
<td align="right">

<A CLASS="menuitem" HREF="contact.do">Contact</A>
<A CLASS="menuitem" HREF="config.do">Settings</A>
<A CLASS="menuitem" HREF="help.do">Help</A>

</td>
</tr>
</table>

<DIV STYLE="border-bottom: 1px solid black;"></DIV>


<%-- SECTION:  NAVIGATION TRAIL --%>
<% String style = request.getParameter("style"); %>
<% if (style==null || !style.equals("simple")) { %>

<%
  LinkedList trail = (LinkedList) session.getAttribute("trail");
 %>

<DIV STYLE="background-color: beige; padding: 3 3 3 3; border-bottom: 1px solid black;" TITLE="Navigation Trail">
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
  
    <% if (i < trail.size() - 2) out.println("<IMG SRC=\""+request.getContextPath()+"/images/arrow_rt.gif\">"); %>
  <% } %>

  <%-- 
    <FORM METHOD="GET" ACTION="<%=request.getContextPath()%>/trail.reset.do">
  <TD ALIGN="RIGHT">
    <% if (trail.size() > 3) { %>
      <BUTTON TYPE="SUBMIT" CLASS="footer" TITLE="Reset navigation trail" STYLE="background-color: #dddddd;">Reset Trail</BUTTON>
    <% } %>
  </TD>
    </FORM>
   --%>
</DIV>

<% } %>

<br />

<% if (!("/search.jsp".equals(request.getServletPath()))) { %>
<jsp:include page="search_form_plug.jsp" flush="true" />
<% } %>

<br />
