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
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>


<BODY onLoad="cleanTitles();">
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">

<TABLE WIDTH="80%" STYLE="border-bottom: thin solid #808080;"><TR><TD ALIGN="LEFT">
Publisher:  <%=api.getPublisher()%>
</TD><TD ALIGN="RIGHT">
Version: <%=api.getVersion()%>
</TD></TR></TABLE>

<BR>

<TABLE BORDER="0" WIDTH="80%">
	<TR VALIGN="BOTTOM">
      <TD><SPAN CLASS="api" STYLE="font-size: large; font-weight: bold;"><%=api.getName()%></SPAN></TD>
      <TD WIDTH="20"></TD>
      <TD ALIGN="CENTER">Release Date: <%=rdate_fmt%></TD>
      <TD WIDTH="20"></TD>
      <TD ALIGN="RIGHT"><A HREF="<%=api.getDownloadURL()%>" TARGET="_new">API Home</A></TD>
	</TR>
</TABLE>

<BR>
<DIV STYLE="height: 200; overflow: auto; border: thin solid #bbbbbb; padding: 5 px;">
    <P><%= api.getDescription() %></P>
</DIV>

<BR>

<TABLE CELLPADDING="3" CELLSPACING="0" ALIGN="CENTER" STYLE="border: 1px solid black;">
<CAPTION CLASS="api"><%=api.getName()%> API Packages</CAPTION>
<TBODY>
<TR>
    <%
     List pkgs = api.getPackages();
     JPackage pkg;
     
     for (int i=0; i<pkgs.size(); i++)
     {
          pkg = (JPackage) pkgs.get(i);
          %>
        <TR <% if (i%2==1) { %> BGCOLOR="beige" <% } %>>
          <TD VALIGN="TOP">
            <A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A>
          </TD>
          <TD VALIGN="TOP">
            <%= pkg.getSummaryDescription() %>
          </TD>
        </TR>
  <%
     }
   %>
</TR>
</TBODY>
</TABLE>


  
  
  
  </DIV> <!-- end page body -->

<BR><BR>
  
  <jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
