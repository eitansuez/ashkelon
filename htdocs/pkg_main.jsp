<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  List cols = new ArrayList(4);
  if (!pkg.getOrdinaryClasses().isEmpty())
    cols.add(pkg.getOrdinaryClasses());
  if (!pkg.getInterfaces().isEmpty())
    cols.add(pkg.getInterfaces());
  if (!pkg.getExceptionClasses().isEmpty())
    cols.add(pkg.getExceptionClasses());
  if (!pkg.getErrorClasses().isEmpty())
    cols.add(pkg.getErrorClasses());
%>


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<% if (pkg.getAPI() != null) { %>
<P CLASS="api" STYLE="font-size: 8 pt;">
(<a href="index.html?cmd=api.main&id=<%=pkg.getAPI().getId()%>"><%=pkg.getAPI().getName()%></a> API)
</P>
<% } %>
  
<DIV STYLE="height: 360; overflow: auto; border: thin solid #bbbbbb; padding: 5 px;">
    <P><%= pkg.getDescription() %></P>
</DIV>
