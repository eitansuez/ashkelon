<%@ page info="eitan's test page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>Legend</TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="../global.css"></LINK>
  <STYLE TYPE="text/css">
  .hover
  {
    background-color: #89B8A5;
    cursor: pointer;
    border-style: outset;
    border-width: 2;
    padding: 3 5 3 5;
    font-weight: bold;
  }
  .normal
  {
    background-color: #89B8A5;
    cursor: pointer;
    border-style: solid;
    border-width: 2;
    border-color: #89B8A5;
    padding: 3 5 3 5;
  }
  </STYLE>
  <jsp:include page="includes.html" flush="true"/>
  <SCRIPT>
  function hover(item)
  {
    item.className = "hover";
  }
  </SCRIPT>
</HEAD>

<BODY onLoad="loadCookies();" onUnload="saveCookies();">

<DIV CLASS="PAGEBODY">

<SPAN CLASS="normal" onMouseOver="hover(this);" onMouseOut="this.className='normal';"><U>T</U>est</SPAN>

<% boolean ismethod = true;
   int numcols2 = (ismethod) ? (3) : (2);
 %>
<TABLE BORDER="1" CLASS="columnar">
<CAPTION>Members</CAPTION>
<COLGROUP SPAN="7" WIDTH="15" ALIGN="CENTER">
</COLGROUP>
<COLGROUP SPAN="<%=numcols2%>">
  <% if (ismethod) { %>
  <COL WIDTH="100" ALIGN="LEFT">
  <% } %>
  <COL WIDTH="100">
  <COL WIDTH="300">
</COLGROUP>
<THEAD CLASS="table_header">
<TR TITLE="Click on one of the modifiers to filter the table by modifier type">
<TD STYLE="font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onClick="filterRows('targettbl', 'reset');" TITLE="Reset table to include all rows">R<BR>e<BR>s<BR>e<BR>t</TD>
<% if (ismethod) { %>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onClick="filterRows('targettbl', 'abstract');" TITLE="Show only abstract members">a<BR>b<BR>s<BR>t<BR>r<BR>a<BR>c<BR>t</TD>
<% } %>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('targettbl', 'public');" TITLE="Show only public members">p<BR>u<BR>b<BR>l<BR>i<BR>c</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('targettbl', 'static');" TITLE="Show only static members">s<BR>t<BR>a<BR>t<BR>i<BR>c</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('targettbl', 'final');" TITLE="Show only final members">f<BR>i<BR>n<BR>a<BR>l</TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;"" TITLE="Version of API that this member was introduced">s<BR>i<BR>n<BR>c<BR>e</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('targettbl', 'deprecated');" TITLE="Show only deprecated members">d<BR>e<BR>p<BR>r<BR>e<BR>c<BR>a<BR>t<BR>e<BR>d</TD>
<% if (ismethod) { %>
<TD STYLE="font-size: x-small; font-variant:small-caps;">Returns</TD>
<% } %>
<TD STYLE="font-size: x-small; font-variant:small-caps;">Member Name</TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;">Summary Description</TD>
</TR>
</THEAD>
<TBODY ID="targettbl">
<% for (int i=0; i<4; i++) { %>
<TR CLASS="static final deprecated" STYLE="display:;">
  <TD>&nbsp;</TD>
  <TD><IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0"></TD>
  <TD><IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0"></TD>
  <TD><IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0"></TD>
  <TD><IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0"></TD>
  <TD>1.1</TD>
  <TD><IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0"></TD>
  <% if (ismethod) { %>
  <TD>int</TD>
  <% } %>
  <TD>method1</TD>
  <TD>xyz</TD>
</TR>
<% } %>
<TR CLASS="abstract since" STYLE="display:;">
  <TD>&nbsp;</TD>
  <TD>x</TD>
  <TD>&nbsp;</TD>
  <TD>&nbsp;</TD>
  <TD>&nbsp;</TD>
  <TD>1.2</TD>
  <TD>&nbsp;</TD>
  <TD>method2</TD>
  <TD>xyz</TD>
</TR>

<TR CLASS="public since" STYLE="display:;">
  <TD>&nbsp;</TD>
  <TD>&nbsp;</TD>
  <TD>x</TD>
  <TD>&nbsp;</TD>
  <TD>&nbsp;</TD>
  <TD>1.0</TD>
  <TD>&nbsp;</TD>
  <TD>method3</TD>
  <TD>xyz</TD>
</TR>

<TR CLASS="public abstract since" STYLE="display:;">
  <TD>&nbsp;</TD>
  <TD>x</TD>
  <TD>x</TD>
  <TD>&nbsp;</TD>
  <TD>&nbsp;</TD>
  <TD>1.0</TD>
  <TD>&nbsp;</TD>
  <TD>method4</TD>
  <TD>xyz</TD>
</TR>

</TBODY>
</TABLE>
<HR>


<PRE>
<FORM>
cookie name: <INPUT NAME="t">
value: <INPUT NAME="s">
<BUTTON onClick="setCookie(this.form.t.value, this.form.s.value);">Set Cookie</BUTTON>
<BUTTON onClick="this.form.s.value=readCookie(this.form.t.value);">Read Cookie</BUTTON>
<BUTTON onClick="showCookies();">Show Cookies</BUTTON>
<BUTTON onClick="resetCookies();">Reset Cookies</BUTTON>
</FORM>
</PRE>


<HR>
<PRE STYLE="margin: 20px;">
visibility hide (vishide) <SPAN ID="vishide" STYLE="visibility:hidden;">visibility: hidden</SPAN>
visibility show (visshow) <SPAN ID="visshow" STYLE="visibility:visible;">visibility: visible</SPAN>
display hide (dishide) <SPAN ID="dishide" STYLE="display:none;">display hidden</SPAN>
display show (disshow) <SPAN ID="disshow" STYLE="display:;">display visible</SPAN>

<FORM>
spanid: <INPUT NAME="t">
visibility attribute (visibility or display)<INPUT NAME="s" VALUE="visibility">
<BUTTON onClick="alert(isVisible(this.form.t.value, this.form.s.value));">testvisibility</BUTTON>
<BUTTON onClick="toggleVisibility(this.form.t.value, this.form.s.value)">toggle visibility</BUTTON>
</FORM>
</PRE>

<HR>


<PRE>
Request context path: <%=request.getContextPath() %>
PathInfo:  <%=request.getPathInfo() %>
PathTranslated:  <%=request.getPathTranslated() %>
QueryString:  <%=request.getQueryString() %>
getRequestURI() : <%=request.getRequestURI() %>

query string: <%=ServletUtils.getQueryString(request)%>

</PRE>

<PRE>
<% Enumeration hdrs = request.getHeaderNames();
   String name;
   while (hdrs.hasMoreElements())
   {
     name = (String) hdrs.nextElement(); %>
     <%=name%>: <%=request.getHeader(name)%>
<% } %>
</PRE>


</DIV>

<jsp:include page="footer.html" flush="true"/>
</BODY>
</HTML>


