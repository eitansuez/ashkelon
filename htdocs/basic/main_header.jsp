<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  Map tabs = new HashMap();
  tabs.put("apis", "APIs");
  tabs.put("pkg", "Packages");
  tabs.put("search", "Search");
  tabs.put("idx", "Index");
  tabs.put("stats.general", "Stats");
  
  String[] cmds = {"apis", "pkg", "search", "idx", "stats.general"};

  String cmd = (String) request.getAttribute("cmd");
  String area = "";
  if (cmd != null)
    area = StringUtils.split(cmd,".")[0];
%>


<TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0">
<TR>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">


    <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3" WIDTH="100%">
      <TR ALIGN="center" VALIGN="top">

      <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1" ALIGN="LEFT">
           <A HREF="index.html">Ashkelon</A>

<% for (int i=0; i<cmds.length; i++)
  { %>
  &nbsp;&nbsp;&nbsp;
        <A HREF="<%=request.getContextPath()%>/<%=cmds[i]%>.do"><FONT CLASS="NavBarFont1"><B><%=tabs.get(cmds[i])%></B></FONT></A>

<%}%>
      </TD>

        
        <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1" ALIGN="RIGHT">
          <A HREF="<%=request.getContextPath()%>/contact.do"><FONT CLASS="NavBarFont1"><B>Contact</B></FONT></A>
          &nbsp;&nbsp;&nbsp;
          <A HREF="<%=request.getContextPath()%>/config.do"><FONT CLASS="NavBarFont1"><B>Settings</B></FONT></A>
          &nbsp;&nbsp;&nbsp;
          <A HREF="<%=request.getContextPath()%>/help.do"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>
        </TD>
      </TR>
    </TABLE>
    
  </TD>
</TR>
</TABLE>


<BR>

<%-- SECTION:  NAVIGATION TRAIL --%>
<% String style = request.getParameter("style"); %>
<% if (style==null || !style.equals("simple")) { %>

<%
  LinkedList trail = (LinkedList) session.getAttribute("trail");
 %>

<TABLE WIDTH="100%" CELLSPACING="0">
<TR BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
  <TD CLASS="NavBarCell1">
  <%
  String[] uriLabelPair = null;
  String uri, caption, itemtype;
  for (int i=0; i<trail.size()-1; i++)  // don't show current page (redundant)
  {
    if (i==0) out.println("Navigation Trail:<BR>");
    uriLabelPair = (String[]) trail.get(i);
    uri = uriLabelPair[0];
    caption = uriLabelPair[1];
    itemtype = uriLabelPair[2];
  %>
    <A HREF="<%=uri%>"><%=caption%></A>
  
    <% if (i < trail.size() - 2) out.println("<IMG SRC=\""+request.getContextPath()+"/images/arrow_rt.gif\">"); %>
  <% } %>
  </TD>
  <%-- 
    <FORM METHOD="GET" ACTION="<%=request.getContextPath()%>/trail.reset.do">
  <TD CLASS="NavBarCell1" ALIGN="RIGHT">
    <% if (trail.size() > 3) { %>
      <INPUT TYPE="SUBMIT" TITLE="Reset navigation trail" VALUE="Reset Trail">
    <% } %>
  </TD>
    </FORM>
    --%>
</TR>
</TABLE>

<% } %>

<BR>

