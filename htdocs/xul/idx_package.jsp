<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getCommand(request);
  
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc Package Index</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  List results = (List) request.getAttribute("results");
  JPackage pkg;
 %>
<TABLE CLASS="columnar">
 <% for (int i=0; i<results.size(); i++) { %>
 <%  pkg = (JPackage) results.get(i); %>
    <TR>
      <TD>
        <A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A>
      </TD>
    </TR>
 <% } %>
</TABLE>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<FORM METHOD="GET" ACTION="index.html">
  <INPUT TYPE="HIDDEN" NAME="cmd" VALUE="idx.package">
  <INPUT TYPE="HIDDEN" NAME="start" VALUE="<%=request.getAttribute("next")%>">
  <BUTTON TYPE="SUBMIT"
          STYLE="background-color: #dddddd; font-size: 8 pt;"
          ACCESSKEY="N"><U>N</U>ext &gt;</BUTTON>
</FORM>
<% } // end if %>

<% } else { %>

<P><B>Package Index Page.</B></P>

<P>Use the A-Z buttons above to browse packages alphabetically by name.</P>

<% } %>


</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
