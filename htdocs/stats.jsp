<%@ page info="page" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*,java.util.*"%>

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

  List apipkgcounts = (List) request.getAttribute("apipkgcounts");
  List apiclscounts = (List) request.getAttribute("apiclscounts");
  List apimmbcounts = (List) request.getAttribute("apimmbcounts");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Stats</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<div class="pagebody">

<br/><br/>

<div ALIGN="CENTER">
<TABLE RULES="rows" CELLPADDING="3" CELLSPACING="0">
<CAPTION>Entity Counts</CAPTION>
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
</div>


<P ALIGN="CENTER">
<APPLET CODE="bar8.class" CODEBASE="classes" 
 WIDTH="500" HEIGHT="300" NAME="barchart_pkg"
 STYLE="border: thin solid 1;">
<PARAM NAME="title" VALUE="Package Counts by API">
<PARAM NAME="x_title" VALUE="API">
<PARAM NAME="y_title" VALUE="Package Count">
<PARAM NAME="num_series" VALUE="1">
<PARAM NAME="num_values" VALUE="<%=apipkgcounts.size()%>">
<PARAM NAME="series1name"   VALUE="Series 1">
<%
 Iterator itr = apipkgcounts.iterator();
 List pair;
 int count;
 String apiname;

 int i = 0;
 while (itr.hasNext())
 {
   i++;
   pair = (List) itr.next();
   count = ((Integer) pair.get(0)).intValue();
   apiname = ((String) pair.get(1));
 %>
<PARAM NAME="series1value<%=i%>" VALUE="<%=count%>">
<PARAM NAME="value_caption<%=i%>" VALUE="<%=apiname%>">
<%
 }
 %>
</APPLET>
</P>

<P ALIGN="CENTER">
<APPLET CODE="bar8.class" CODEBASE="classes" 
 WIDTH="500" HEIGHT="300" NAME="barchart_cls"
 STYLE="border: thin solid 1;">
<PARAM NAME="title" VALUE="Class Counts by API">
<PARAM NAME="x_title" VALUE="API">
<PARAM NAME="y_title" VALUE="Class Count">
<PARAM NAME="num_series" VALUE="1">
<PARAM NAME="num_values" VALUE="<%=apiclscounts.size()%>">
<%
 itr = apiclscounts.iterator();

 i = 0;
 while (itr.hasNext())
 {
   i++;
   pair = (List) itr.next();
   count = ((Integer) pair.get(0)).intValue();
   apiname = ((String) pair.get(1));
 %>
<PARAM NAME="series1value<%=i%>" VALUE="<%=count%>">
<PARAM NAME="value_caption<%=i%>" VALUE="<%=apiname%>">
<%
 }
 %>
</APPLET>
</P>

<P ALIGN="CENTER">
<APPLET CODE="bar8.class" CODEBASE="classes" 
 WIDTH="500" HEIGHT="300" NAME="barchart_mmb"
 STYLE="border: thin solid 1;">
<PARAM NAME="title" VALUE="Member Counts by API">
<PARAM NAME="x_title" VALUE="API">
<PARAM NAME="y_title" VALUE="Member Count">
<PARAM NAME="num_series" VALUE="1">
<PARAM NAME="num_values" VALUE="<%=apimmbcounts.size()%>">
<%
 itr = apimmbcounts.iterator();

 i = 0;
 while (itr.hasNext())
 {
   i++;
   pair = (List) itr.next();
   count = ((Integer) pair.get(0)).intValue();
   apiname = ((String) pair.get(1));
 %>
<PARAM NAME="series1value<%=i%>" VALUE="<%=count%>">
<PARAM NAME="value_caption<%=i%>" VALUE="<%=apiname%>">
<%
 }
 %>
</APPLET>
</P>


</div>

<jsp:include page="footer.html" flush="true"/>


</body>
</html>

