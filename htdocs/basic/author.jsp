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
</HEAD>

<BODY BGCOLOR="white">

<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<H2><%=author.getName()%></H2>

<P>
<%=author.getName()%> has authored the following classes:
</P>

<OL>
<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
%>
<LI><A HREF="cls.main.do?cls_id=<%=cls.getId()%>"><%=cls.getQualifiedName()%></A></LI>
<% } %>
</OL>


</DIV>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
