<%@ page info="page" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


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
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body onLoad="loadCookies();" onUnload="saveCookies();">

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="stats_header.jsp" flush="true"/>

<div class="pagebody">

<br/><br/>

<div align="center">
<table rules="rows" cellpadding="3" cellspacing="0">
<caption>Entity Counts</caption>
<tr>
  <td>APIs</td>
  <td align="right"><%=api_count_f%></td>
</tr>
<tr>
  <td>Packages</td>
  <td align="right"><%=package_count_f%></td>
</tr>
<tr>
  <td>Classes & Interfaces</td>
  <td align="right"><%=class_count_f%></td>
</tr>
<tr>
  <td>Members</td>
  <td align="right"><%=member_count_f%></td>
</tr>
</table>
</div>


<p align="center">
<applet code="bar8.class" codebase="classes" 
 width="500" height="300" name="barchart_pkg"
 style="border: thin solid 1;">
<param name="title" value="Package Counts by API">
<param name="x_title" value="API">
<param name="y_title" value="Package Count">
<param name="num_series" value="1">
<param name="num_values" value="<%=apipkgcounts.size()%>">
<param name="series1name"   value="Series 1">
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
<param name="series1value<%=i%>" value="<%=count%>">
<param name="value_caption<%=i%>" value="<%=apiname%>">
<%
 }
 %>
</applet>
</p>

<p align="center">
<applet code="bar8.class" codebase="classes" 
 width="500" height="300" name="barchart_cls"
 style="border: thin solid 1;">
<param name="title" value="Class Counts by API">
<param name="x_title" value="API">
<param name="y_title" value="Class Count">
<param name="num_series" value="1">
<param name="num_values" value="<%=apiclscounts.size()%>">
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
<param name="series1value<%=i%>" value="<%=count%>">
<param name="value_caption<%=i%>" value="<%=apiname%>">
<%
 }
 %>
</applet>
</P>

<p align="center">
<applet code="bar8.class" codebase="classes" 
 width="500" height="300" name="barchart_mmb"
 style="border: thin solid 1;">
<param name="title" value="Member Counts by API">
<param name="x_title" value="API">
<param name="y_title" value="Member Count">
<param name="num_series" value="1">
<param name="num_values" value="<%=apimmbcounts.size()%>">
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
<param name="series1value<%=i%>" value="<%=count%>">
<param name="value_caption<%=i%>" value="<%=apiname%>">
<%
 }
 %>
</applet>
</p>


</div>

<c:import url="footer.html" />

</body>
</html>

