<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String cmd = ServletUtils.getRequestParam(request, "cmd");

  List numByAuthor = (List) request.getAttribute("classCounts");
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Class Stats</TITLE>
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

<DIV CLASS="PAGEBODY" ALIGN="CENTER">

<B>Class count by Author</B>
<TABLE CELLSPACING="0" CELLPADDING="3" BORDER="1">
<TR>
<%  
  int n = numByAuthor.size();
  int numCols = 3;
  int modulus = n%numCols;
  int numRows = n/numCols + ((modulus==0) ? 0 : 1);

  String numAuthorClasses;
  Author author;
  
  int start, end, numtoskip;
  for (int col=0; col<numCols; col++)
  {
    start = numRows * col;
    end = start + numRows;
    numtoskip = numCols - modulus;
    if (col == numCols-1 && modulus > 0)
      end = end - numtoskip;
  %>
  
  <!-- COLUMN <%=col%> -->
  <TD VALIGN="TOP">
   <TABLE CELLSPACING="0" CELLPADDING="3">
  
    <%  
    for (int i=start; i<end; i++)
     {
      author = (Author) ((List) numByAuthor.get(i)).get(0);
      numAuthorClasses = (String) ((List) numByAuthor.get(i)).get(1);
   %>

    <% if (Integer.parseInt(numAuthorClasses)>30)
    { %>
  <TR CLASS="highlight">
    <TD><B><a href="index.html?cmd=author&id=<%=author.getId()%>"><%=author.getName()%></a></B></TD>
    <TD ALIGN="RIGHT"><B><%=numAuthorClasses%></B></TD>
  </TR>
    <% } else
       { %>
  <TR>
    <TD><a href="index.html?cmd=author&id=<%=author.getId()%>"><%=author.getName()%></a></TD>
    <TD ALIGN="RIGHT"><%=numAuthorClasses%></TD>
  </TR>
    <% } %>

  <% } %>
 
    </TABLE>
   </TD>
  
    <%
     if (col < numCols - 1)
     { %>  
      <!-- padding -->
      <TD WIDTH="20">&nbsp;</TD>
  <% } %>
 
<% } %>
  
</TR>
</TABLE>

</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>

