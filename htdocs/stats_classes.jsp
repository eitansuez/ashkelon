<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  List numByPackage = (List) request.getAttribute("classCounts");
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Class Stats</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<TABLE CLASS="columnar" ALIGN="CENTER">
<CAPTION>Class count by package</CAPTION>
<TR>
<%  
  int n = numByPackage.size();
  int numCols = 3;
  int modulus = n%numCols;
  int numRows = n/numCols + ((modulus==0) ? 0 : 1);

  String numPkgClasses;
  JPackage pkg;
  
  int start, end, numtoskip;
  for (int col=0; col<numCols; col++)
  {
    start = numRows * col;
    end = start + numRows;
    numtoskip = numCols - n%numCols;
    if (col == numCols-1 && modulus>0)
      end = end - numtoskip;
  %>
  
  <!-- COLUMN <%=col%> -->
  <TD>
   <TABLE RULES="rows" CELLSPACING="0" CELLPADDING="3" BORDERCOLOR="white">
  
  <%  
  for (int i=start; i<end; i++)
   {
    pkg = (JPackage) ((List) numByPackage.get(i)).get(0);
    numPkgClasses = (String) ((List) numByPackage.get(i)).get(1);
 %>
    <% if (Integer.parseInt(numPkgClasses)>50)
    { %>
  <TR CLASS="highlight">
    <TD><B><a href="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></a></B></TD>
    <TD ALIGN="RIGHT"><B><%=numPkgClasses%></B></TD>
  </TR>
    <% } else
       { %>
  <TR>
    <TD><a href="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></a></TD>
    <TD ALIGN="RIGHT"><%=numPkgClasses%></TD>
  </TR>
    <% } %>
<% } %>
 
  </TABLE>
 </TD>
  
    <%
     if (col < numCols - 1)
     { %>  
      <!-- padding -->
      <TD WIDTH="20"></TD>
  <% } %>
 
<% } %>
  
</TR>
</TABLE>

</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>