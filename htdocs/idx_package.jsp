<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <title>Ashkelon Package Index</title>
  <jsp:include page="includes.html" flush="true"/>
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
        <A HREF="pkg.main.do?pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A>
      </TD>
    </TR>
 <% } %>
</TABLE>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<FORM METHOD="GET" ACTION="idx.package.do">
  <INPUT TYPE="HIDDEN" NAME="start" VALUE="<%=request.getAttribute("next")%>">
  <BUTTON TYPE="SUBMIT"
          STYLE="background-color: #dddddd; font-size: 8pt;"
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
