<%@ page info="top page: api list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: November 2001
--%>

<%
  List apiList = (List) application.getAttribute("apilist");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc - API Listing</TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="/<%=request.getContextPath()%>/basic/stylesheet.css" TITLE="Style">
</HEAD>

<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>

<H3>Welcome to dbdoc</H3>


<P>
dbdoc is an online reference to Java API documentation (see the help documentation for more information).  dbdoc is <B>open source</B> and available for download off sourceforge.net as project <A TARGET="_new" HREF="http://sourceforge.net/projects/ashkelon/">ashkelon</A>.
</P>
<BR>

<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 <TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
  <TD COLSPAN=2><FONT SIZE="+2">
   <B>API Listing</B></FONT></TD>
 </TR>
  <%
  API api;
  String title;
   for (int i=0; i<apiList.size(); i++)
   {
      api = (API) apiList.get(i);
      title = api.getSummaryDescription();
  %>
 <TR BGCOLOR="white" CLASS="TableRowColor">
  <TD WIDTH="20%"><B><A HREF="index.html?cmd=api.main&id=<%=api.getId()%>"><%=api.getName()%></A></B></TD>
  <TD><%=api.getSummaryDescription()%></TD>
 </TR>
<% } // end for %>
</TABLE>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>



