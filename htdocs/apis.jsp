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
	<TITLE>Ashkelon - API Listing</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<H3>Welcome to Ashkelon</H3>

<P>
Ashkelon is an online reference to Java API documentation (see the help documentation for more information).  Ashkelon is <B>open source</B> and available for download off sourceforge.net as project <A TARGET="_new" HREF="http://sourceforge.net/projects/ashkelon/">ashkelon</A>.
</P>

<TABLE BORDER="1" CELLPADDING="5" CELLSPACING="0" RULES="rows" ALIGN="CENTER" bordercolor="black">
<CAPTION>API Listing</CAPTION>
<TBODY>
  <%
  API api;
  String title;
   for (int i=0; i<apiList.size(); i++)
   {
      api = (API) apiList.get(i);
      title = api.getSummaryDescription();
  %>
    <TR <% if (i%2==1) { %>BGCOLOR="beige"<% } %>>
      <TD VALIGN="TOP">
        <A HREF="index.html?cmd=api.main&id=<%=api.getId()%>"><SPAN CLASS="api" TITLE="<%=title%>"><%=api.getName()%></SPAN></A>
      </TD>
      <TD VALIGN="TOP">
        <%=api.getSummaryDescription()%>
      </TD>
    </TR>
<% } // end for %>
</TR>
</TBODY>
</TABLE>

</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
