<%@ page info="top page: package list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  HashMap cols = (HashMap) application.getAttribute("javaPkgs");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <title>Ashkelon - Packages</title>
  <c:import url="/includes.html" />
</HEAD>

<body bgcolor="white">
<c:import url="main_header.jsp" />


<% if (cols.isEmpty()) { %>
 <P>No packages exist in the Ashkelon repository at this time.</P>
<% } else { %>

<div align="center">
Packages by Type
<TABLE BORDER="1" CELLSPACING="0" CELLPADDING="5">

  <TR>
  <%
  String columnName = null;
  Set keys = cols.keySet();
  Iterator iter = keys.iterator();
  while (iter.hasNext())
  {
    columnName = iter.next().toString();
  %>
  <TD><%=columnName%></TD>
  <%
  }
  %>
  </TR>
  <TR>
  <%
  JPackage pkg;
  String title;
  List col;
  iter = keys.iterator();
  while (iter.hasNext())
  {
  %>
    <TD VALIGN="TOP">
    <%
       col = (List) cols.get(iter.next());
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

<% } // end while %>

  </tr>
</table>
</div>

<% } // end if %>

<c:import url="footer.html" />

</body>
</html>

