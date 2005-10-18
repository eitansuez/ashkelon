<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
%>

<p class="api" style="font-size: 8pt; margin-bottom: 0;">
 (<u2d:link to="api" elem="<%=pkg.getAPI()%>"><%=pkg.getAPI().getName()%></u2d:link> API)
</p>

<p style="margin-top: 0.25em;"><%=pkg.getSummaryDescription()%></p>


