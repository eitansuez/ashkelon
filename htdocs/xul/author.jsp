<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
--%>

<%-- SECTION: PAGE CODE --%>
<%
  Author author = (Author) request.getAttribute("author");
  List classes = author.getClasses();
  ClassType cls = null;
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Author: <%=author.getName()%></TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<H2><%=author.getName()%></H2>

<P>
<%=author.getName()%> has authored the following classes and/or interfaces:
</P>

<OL>
<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
%>
<LI><A HREF="index.html?cmd=cls.main&amp;cls_id=<%=cls.getId()%>" CLASS="<%=cls.getClassTypeName()%> %>"><%=cls.getQualifiedName()%></A></LI>
<% } %>
</OL>


</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
