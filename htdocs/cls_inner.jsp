<%@ page info="class's inner classes view (component)" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: November 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  
  request.setAttribute("classes", cls.getInnerClasses());
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<p><%= cls.getSummaryDescription() %></p>


<jsp:include page="class_list.jsp" flush="true">
  <jsp:param name="caption" value="Inner Classes" />
  <jsp:param name="empty_msg" value="No Inner Classes" />
  <jsp:param name="display_qualified" value="false" />
  <jsp:param name="classes_type" value="all" />
  <jsp:param name="div_id" value="cls_inner" />
</jsp:include>
