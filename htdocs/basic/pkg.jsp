<%@ page info="top page: package list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>dbdoc - Packages</TITLE>
  <jsp:include page="../includes.html" flush="true"/>
</HEAD>

<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>

<% if (cols.isEmpty()) { %>
 <P>No packages exist in the dbdoc repository at this time.</P>
<% } else { %>

<DIV ALIGN="CENTER">
Packages by Type
<TABLE BORDER="1" CELLSPACING="0" CELLPADDING="5">

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
  <TR>

  <%
  JPackage pkg;
  String title;
  List col;
  for (int i=0; i<cols.size(); i++) { %>
  	<TD VALIGN="TOP">
    <%
     col = (List) cols.get(i);
     for (int j=0; j<col.size(); j++)
     {
        pkg = (JPackage) col.get(j);
        title = pkg.getDoc().getSummaryDescription();
        if (StringUtils.isBlank(title))
          title = pkg.getName(); 
        %>
        <A HREF="pkg.main.do?pkg_id=<%=pkg.getId()%>" TITLE="<%=HtmlUtils.cleanAttributeText(title)%>"><%=pkg.getName()%></A>
        <BR>
  <% }  // end inner for loop %>
    </TD>

<% } // end for %>

  </TR>
</TABLE>
</DIV>

<% } // end if %>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
