<%@ page info="class members view (component)" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  
  String membertype = request.getParameter("membertype");
  List members = new ArrayList();
  
  if ("field".equals(membertype))
    members = cls.getFields();
  else if ("constructor".equals(membertype))
    members = cls.getConstructors();
  else if ("method".equals(membertype))
    members = cls.getMethods();
  
  String cls_type = cls.getClassTypeName();
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<p><%= cls.getSummaryDescription() %></p>


<%
  request.setAttribute("members", members);
  String divid = "cls_"+membertype;
  String emptyMsg = "No " + membertype + " information available for " + cls.getName();
  String captiondescr = "Brief " + membertype + " descriptions";
 %>
 
<jsp:include page="member_list.jsp" flush="true">
  <jsp:param name="caption" value="<%=captiondescr%>" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="false" />
  <jsp:param name="members_type" value="<%=membertype%>" />
  <jsp:param name="div_id" value="<%=divid%>" />
</jsp:include>


