<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- 
Author: Eitan Suez
Date: March 2001
--%>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
%>

<div style="float: right;">
  <button id="toggleBtn" onClick="toggleHeightMode('class-description', this, '300px');"><img src="images/expand.jpg" /></button>
</div>
    
<div id="class-description" class="scroll-description" style="clear: right;">
 <%= cls.getDescription() %>
</div>

