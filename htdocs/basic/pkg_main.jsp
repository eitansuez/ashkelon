<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
%>

<H2><%=pkg.getName()%></H2>

<P>
<%=pkg.getSummaryDescription()%>
</P>

<DL>
<DT><B>See:</B></DT>
<DD><A HREF="#description">Description</A></DD>
</DL>


