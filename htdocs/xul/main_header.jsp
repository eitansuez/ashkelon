<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
  Copyright UptoData Inc 2001
  Author: Eitan Suez
  Date: March 2001
  
  outstanding tasks:
    parametrize: colors, their links (associated commands)
--%>

<%-- SECTION: PAGE CODE --%>
<%
  Map tabs = new HashMap();
  tabs.put("apis", "APIs");
  tabs.put("pkg", "Packages");
  tabs.put("search", "Search");
  tabs.put("idx", "Index");
  tabs.put("stats.general", "Stats");
  
  String[] cmds = {"apis", "pkg", "search", "idx", "stats.general"};

  String cmd = ServletUtils.getCommand(request);
  String area = "";
  if (cmd != null)
    area = StringUtils.split(cmd,".")[0];
%>

<%-- SECTION: PAGE STYLES --%>
<STYLE TYPE="text/css">
.menuitem
{
  padding: 0 10 0 10;
  font-weight: bold;
}
</STYLE>

<%-- SECTION: BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
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
</SCRIPT>


<%-- SECTION: TEMPLATE --%> 
<DIV STYLE="float: left;">

<% for (int i=0; i<cmds.length; i++)
  { %>
      <A CLASS="menuitem" HREF="<%=request.getContextPath()%>/index.html?cmd=<%=cmds[i]%>"><%=tabs.get(cmds[i])%></A>
<%}%>

</DIV>

<DIV STYLE="float: right;">

<A CLASS="menuitem" HREF="index.html?cmd=contact">Contact</A>
<A CLASS="menuitem" HREF="index.html?cmd=config">Settings</A>
<A CLASS="menuitem" HREF="index.html?cmd=help">Help</A>

</DIV>
<BR>
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
    <A HREF="<%=uri%>"><%=caption%></A>
  <% } else { %>
    <A HREF="<%=uri%>"><SPAN CLASS="<%=itemtype%>"><%=caption%></SPAN></A>
  <% } // end if %>
  
    <% if (i < trail.size() - 2) out.println("<IMG SRC=\""+request.getContextPath()+"/images/arrow_rt.gif\">"); %>
  <% } %>

  <%-- 
    <FORM METHOD="GET" ACTION="<%=request.getContextPath()%>/index.html">
      <INPUT TYPE="HIDDEN" NAME="cmd" VALUE="trail.reset">
  <TD ALIGN="RIGHT">
    <% if (trail.size() > 3) { %>
      <BUTTON TYPE="SUBMIT" CLASS="footer" TITLE="Reset navigation trail" STYLE="background-color: #dddddd;">Reset Trail</BUTTON>
    <% } %>
  </TD>
    </FORM>
   --%>
</DIV>

<% } %>

<BR>
