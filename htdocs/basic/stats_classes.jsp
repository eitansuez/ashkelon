<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  String cmd = (String) request.getAttribute("cmd");
  List numByPackage = (List) request.getAttribute("classCounts");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>Class Stats</TITLE>
  <jsp:include page="../includes.html" flush="true"/>
</HEAD>

<BODY BGCOLOR="white">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<DIV ALIGN="CENTER">

<B>Class count by package</B>
<TABLE CELLSPACING="0" CELLPADDING="3" BORDER="1">
<TR>
<%  
  int n = numByPackage.size();
  int numCols = 2;
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
   <TABLE CELLSPACING="0" CELLPADDING="3">
  
  <%  
  for (int i=start; i<end; i++)
   {
    pkg = (JPackage) ((List) numByPackage.get(i)).get(0);
    numPkgClasses = (String) ((List) numByPackage.get(i)).get(1);
 %>
    <% if (Integer.parseInt(numPkgClasses)>50)
    { %>
  <TR CLASS="highlight">
    <TD><B><a href="pkg.main.do?pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></a></B></TD>
    <TD ALIGN="RIGHT"><B><%=numPkgClasses%></B></TD>
  </TR>
    <% } else
       { %>
  <TR>
    <TD><a href="pkg.main.do?pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></a></TD>
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
      <TD WIDTH="20">&nbsp;</TD>
  <% } %>
 
<% } %>
  
</TR>
</TABLE>

</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
