<%@ page info="page" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

 Add Number of Classes by package? java.* , javax.*, org.*, com.*
 --%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  Integer api_count = (Integer) application.getAttribute("api_count");
  Integer package_count = (Integer) application.getAttribute("package_count");
  Integer class_count = (Integer) application.getAttribute("class_count");
  Integer member_count = (Integer) application.getAttribute("member_count");
  
  NumberFormat nf = NumberFormat.getInstance();
  String api_count_f = nf.format(api_count);
  String package_count_f = nf.format(package_count);
  String class_count_f = nf.format(class_count);
  String member_count_f = nf.format(member_count);
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Stats</TITLE>
  <jsp:include page="../includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY BGCOLOR="white">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<BR><BR>

<DIV ALIGN="CENTER">
<B>Entity Counts</B>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0">
<TR>
  <TD>APIs</TD>
  <TD ALIGN="RIGHT"><%=api_count_f%></TD>
</TR>
<TR>
  <TD>Packages</TD>
  <TD ALIGN="RIGHT"><%=package_count_f%></TD>
</TR>
<TR>
  <TD>Classes & Interfaces</TD>
  <TD ALIGN="RIGHT"><%=class_count_f%></TD>
</TR>
<TR>
  <TD>Members</TD>
  <TD ALIGN="RIGHT"><%=member_count_f%></TD>
</TR>
</TABLE>
</DIV>


</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>