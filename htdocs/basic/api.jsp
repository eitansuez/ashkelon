<%@ page info="main api view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: November 2001
--%>

<%
  API api = (API) request.getAttribute("api");
  SimpleDateFormat df = new SimpleDateFormat("MMMM dd yyyy");
  String rdate_fmt= df.format(api.getReleaseDate());
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=api.getName()%></TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="/<%=request.getContextPath()%>/basic/stylesheet.css" TITLE="Style">
</HEAD>


<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>
  
<H2><%=api.getName()%></H2>

<P><%= api.getDescription() %></P>

<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD WIDTH="100">Publisher:</TD>
<TD><A HREF="<%=api.getDownloadURL()%>" TARGET="_new"><%=api.getPublisher()%></A></TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD>Version:</TD>
<TD><%=api.getVersion()%></TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD>Release Date:</TD>
<TD><%=rdate_fmt%></TD>
</TR>
</TABLE>

<BR><BR>

<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B><%=api.getName()%> API Packages</B></FONT></TD>
</TR>

    <%
     List pkgs = api.getPackages();
     JPackage pkg;
     
     for (int i=0; i<pkgs.size(); i++)
     {
          pkg = (JPackage) pkgs.get(i);
          %>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD WIDTH="15%"><B><A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A></B></TD>
<TD><%= pkg.getSummaryDescription() %></TD>
        </TR>
  <%
     }
   %>
</TABLE>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
