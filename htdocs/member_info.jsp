<%@ page info="member information component" import="org.ashkelon.*" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%--
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  Member member = (Member) request.getAttribute("member");
  ClassType cls = member.getContainingClass();
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();

  String membertypename = member.getMemberTypeName();
%>


<p style="margin-bottom: 10px;">
<%= member.getModifiers() %> <u2d:link to="package" elem="<%=pkg%>"><span class="package"><%=pkg.getName()%></span></u2d:link>.<u2d:link to="class" elem="<%=cls%>"><span class="<%=cls_type%>"><%=cls.getName()%></span></u2d:link>.<span class="<%=member.getMemberTypeName()%>" style="font-size: large; font-weight: bold;"><%=member.getName()%> <% if (!"field".equals(membertypename)) { %> () <% } %> </span>
</p>

<!--
<p class="<%=member.getMemberTypeName()%>" STYLE="font-size: large; font-weight: bold;"><%=member.getName()%></p>

<p><%=member.getDoc().getDescription()%></p>
-->
