<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
%>

<p class="api" style="font-size: 8pt; margin-bottom: 0;">
 (<a href="api.main.do?id=<%=pkg.getAPI().getId()%>"><%=pkg.getAPI().getName()%></a> API)
</p>

<p style="margin-top: 0.25em;"><%=pkg.getSummaryDescription()%></p>


