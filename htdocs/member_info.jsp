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


<%-- SECTION: COMPONENT TEMPLATE --%> 
<p style="margin-bottom: 10px;">
<%= member.getModifiers() %> <a href="pkg.main.do?pkg_id=<%=pkg.getId()%>"><span class="package"><%=pkg.getName()%></span></a>.<a href="cls.main.do?cls_id=<%=cls.getId()%>"><span class="<%=cls_type%>"><%=cls.getName()%></span></a>.<span class="<%=member.getMemberTypeName()%>" style="font-size: large; font-weight: bold;"><%=member.getName()%> <% if (!"field".equals(membertypename)) { %> () <% } %> </span>
</p>

<!--
<p class="<%=member.getMemberTypeName()%>" STYLE="font-size: large; font-weight: bold;"><%=member.getName()%></p>

<p><%=member.getDoc().getDescription()%></p>
-->
