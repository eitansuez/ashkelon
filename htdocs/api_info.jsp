<%@ page info="component" import="org.ashkelon.*" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%
  API api = (API) request.getAttribute("api");
%>

<div style="float: left;">
  <span class="api" style="font-size: large; font-weight: bold;"><%=api.getName()%></span> v<%=api.getVersion()%>
</div>

<div style="float: right;">
  <fmt:formatDate value="${api.releaseDate}" pattern="MMMM dd yyyy" />
</div>


<div class="publisher" style="border-top: 1px solid #808080; margin-bottom: 1.5em; clear: both;">
  <a href="<%=api.getDownloadURL()%>" target="_new"><%=api.getPublisher()%></a>
</div>


