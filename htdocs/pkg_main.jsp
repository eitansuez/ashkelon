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

<p class="api" style="font-size: 8pt;">
(<a href="api.main.do?id=<%=pkg.getAPI().getId()%>"><%=pkg.getAPI().getName()%></a> API)
</p>

<div style="float: right;">
  <button id="toggleBtn" onClick="toggleHeightMode('pkg-description', this, '300px');"><img src="images/expand.jpg" /></button>
</div>

<div id="pkg-description" class="scroll-description" style="clear: right;">
  <%= pkg.getDescription() %>
</div>

