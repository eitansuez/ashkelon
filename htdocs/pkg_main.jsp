<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

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

<% if (pkg.getAPI() != null) { %>
<P CLASS="api" STYLE="font-size: 8 pt;">
(<a href="api.main.do?id=<%=pkg.getAPI().getId()%>"><%=pkg.getAPI().getName()%></a> API)
</P>
<% } %>
  
<DIV STYLE="height: 360; overflow: auto; border: thin solid #bbbbbb; padding: 5 px;">
    <P><%= pkg.getDescription() %></P>
</DIV>
