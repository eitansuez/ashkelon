<%@ page info="main package view" import="java.util.*,org.ashkelon.*"%>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

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

<jsp:include page="pkg_info.jsp" flush="true" />

<div style="float: right;">
  <button id="toggleBtn" onClick="toggleHeightMode('pkg-description', this, '300px');"><u2d:imgref ref="images/expand.jpg" /></button>
</div>

<div id="pkg-description" class="scroll-description" style="clear: right;">
  <%= pkg.getDescription() %>
</div>

