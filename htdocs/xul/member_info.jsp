<%@ page info="member information component" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  Member member = (Member) request.getAttribute("member");
  ClassType cls = member.getContainingClass();
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();

  String membertypename = member.getMemberTypeName();
%>


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<P>
<%= member.getModifiers() %> <A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><SPAN CLASS="package"><%=pkg.getName()%></SPAN></A>.<A HREF="index.html?cmd=cls.main&amp;cls_id=<%=cls.getId()%>"><SPAN CLASS="<%=cls_type%>"><%=cls.getName()%></SPAN></A>.<SPAN CLASS="<%=member.getMemberTypeName()%>" STYLE="font-size: large; font-weight: bold;"><%=member.getName()%>

<% if (!"field".equals(membertypename)) { %>
  ()
<% } %>
</SPAN>
</P>

<%-- 
<P CLASS="<%=member.getMemberTypeName()%>" STYLE="font-size: large; font-weight: bold;"><%=member.getName()%></P>
--%>

<%--
<P><%=member.getDoc().getDescription()%></P>
--%>
