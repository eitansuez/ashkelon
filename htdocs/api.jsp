<%@ page info="main api view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*"%>

<%
  API api = (API) request.getAttribute("api");
  SimpleDateFormat df = new SimpleDateFormat("MMMM dd yyyy");
  String rdate_fmt= df.format(api.getReleaseDate());
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=api.getName()%></title>
  <jsp:include page="includes.html" flush="true" />
</head>

<body onLoad="loadCookies();cleanTitles();togglePage('api_member', 'Packages');"
   onUnload="saveCookies();">
  <jsp:include page="main_header.jsp" flush="true" />
  <jsp:include page="api_header.jsp" flush="true" />
  
  <div class="pagebody">

  <div id="api_main" style="display: none;">
  
<TABLE WIDTH="80%" STYLE="border-bottom: thin solid #808080;"><TR><TD ALIGN="LEFT">
Publisher:  <%=api.getPublisher()%>
</TD><TD ALIGN="RIGHT">
Version: <%=api.getVersion()%>
</TD></TR></TABLE>

<br/>

<table border="0" width="80%">
	<tr valign="bottom">
      <td><span class="api" style="font-size: large; font-weight: bold;"><%=api.getName()%></span></td>
      <td width="20"></td>
      <td align="center">Release Date: <%=rdate_fmt%></TD>
      <td width="20"></td>
      <td align="right"><a href="<%=api.getDownloadURL()%>" target="_new">API Home</A></td>
	</tr>
</table>

<br/>
<div style="height: 200; overflow: auto; border: thin solid #bbbbbb; padding: 5 px;">
    <p><%= api.getDescription() %></p>
</div>

</div>

<div id="api_member" style="display: none;">

<table cellpadding="3" cellspacing="0" align="center" style="border: 1px solid black;">
<caption class="api"><%=api.getName()%> API Packages</caption>
<tr>
<td>
<div style="height: 330; overflow: auto;  padding: 10px;">

<table>
<tbody>
    <%
     List pkgs = api.getPackages();
     JPackage pkg;
     
     for (int i=0; i<pkgs.size(); i++)
     {
          pkg = (JPackage) pkgs.get(i);
          %>
        <tr <% if (i%2==1) { %> BGCOLOR="beige" <% } %>>
          <td valign="top">
            <a href="pkg.main.do?pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></a>
          </td>
          <td valign="top">
            <%= pkg.getSummaryDescription() %>
          </td>
        </tr>
  <%
     }
   %>
</tbody>
</table>
</div>
</td>
</tr>
</table>

</div>
  
  
  </div> <!-- end page body -->

<br/><br/>

  <jsp:include page="footer.html" flush="true" />


</body>
</html>
