<%@ page info="top page: package list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  List cols = new ArrayList(4);
  List javaPkgs = (List) application.getAttribute("javaPkgs");
  List javaxPkgs = (List) application.getAttribute("javaxPkgs");
  List orgPkgs = (List) application.getAttribute("orgPkgs");
  List comPkgs = (List) application.getAttribute("comPkgs");
  if (!javaPkgs.isEmpty())
    cols.add(javaPkgs);
  if (!javaxPkgs.isEmpty())
    cols.add(javaxPkgs);
  if (!orgPkgs.isEmpty())
    cols.add(orgPkgs);
  if (!comPkgs.isEmpty())
    cols.add(comPkgs);
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc - Packages</TITLE>
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

<% if (cols.isEmpty()) { %>
 <P>No packages exist in the dbdoc repository at this time.</P>
<% } else { %>

<DIV ALIGN="CENTER">
<TABLE CLASS="columnar">
<CAPTION>Packages by Type</CAPTION>
<!--
<THEAD CLASS="table_header">
<TR>
  <TD>java.*</TD>
  <TD>javax.*</TD>
  <TD>org.*</TD>
  <TD>com.*</TD>
</TR>
</THEAD>
-->
<TBODY>
<TR>
  <%
  JPackage pkg;
  String title;
  List col;
  for (int i=0; i<cols.size(); i++) { %>
  <TD VALIGN="TOP">
 <DIV CLASS="scroll_column">
    <TABLE>
    <%
     col = (List) cols.get(i);
     for (int j=0; j<col.size(); j++)
     {
        pkg = (JPackage) col.get(j);
        title = pkg.getDoc().getSummaryDescription();
        if (StringUtils.isBlank(title))
          title = pkg.getName(); 
        %>
    <TR>
    	<TD>
        <A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><SPAN CLASS="package"  TITLE="<%=HtmlUtils.cleanAttributeText(title)%>"><%=pkg.getName()%></SPAN></A>
      </TD>
    </TR>
  <% }  // end inner for loop %>
    </TABLE>
 </DIV>
  </TD>
<% } // end for %>
</TR>
</TBODY>
</TABLE>
</DIV>

<% } // end if %>


</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
