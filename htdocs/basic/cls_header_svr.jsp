<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

outstanding tasks:
  parametrize: colors, their links (associated commands)
--%>

<%-- SECTION: COMPONENT CODE --%>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<FONT SIZE="-2">
SUMMARY:
<% if (cls.getInnerClasses()!=null && !cls.getInnerClasses().isEmpty()) { %>
&nbsp; <A HREF="#inner_summary">INNER</A> &nbsp;
<% } %>
<% if (!cls.getFields().isEmpty()) { %>
&nbsp; <A HREF="#field_summary">FIELD</A> &nbsp;
<% } %>
<% if (!cls.getConstructors().isEmpty()) { %>
&nbsp; <A HREF="#constructor_summary">CONSTR</A> &nbsp;
<% } %>
<% if (!cls.getMethods().isEmpty()) { %>
&nbsp; <A HREF="#method_summary">METHOD</A> &nbsp;
<% } %>
&nbsp; <A HREF="#cls_xref">CROSS-REF</A> &nbsp;
</FONT>
<HR>

<%--
<jsp:include page="l2_hdr_svr.jsp" flush="true">
  <jsp:param name="args" value="<%=(\"cls_id=\"+cls_id)%>"/>
</jsp:include>
--%>
<BR>
