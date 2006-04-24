<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%-- 
Author: Eitan Suez
Date: March 2001
--%>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
%>

<div style="float: right;">
  <button id="toggleBtn" onClick="toggleHeightMode('class-description', this, '300px');"><u2d:imgref ref="images/expand.jpg" /></button>
</div>
    
<div id="class-description" class="scroll-description" style="clear: right;">
 <%= cls.getDescription() %>
</div>

