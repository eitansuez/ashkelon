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

<script>
function toggleHeightMode(tagid, btn)
{
  var tag = document.getElementById(tagid);
  if (tag.style.height == "" || tag.style.height == "330px")
  {
    tag.style.height = "auto";
    btn.innerHTML = "<img src='images/collapse.jpg' />";
  }
  else
  {
    tag.style.height = "330px";
    btn.innerHTML = "<img src='images/expand.jpg' />";
  }
}
</script>

<style>
#toggleBtn
{
  margin: 0;
  padding: 0;
  background-color: #ccc;
}

#api-pkglist
{
  height: 330px;
  overflow: auto;
  padding: 0;
  text-align: center;
  border: 1px solid gray;
  clear: right;
}
#api-pkglist-caption
{
  font-size: 120%;
  text-align: center;
}
table#api-pkgs
{
  width: 100%;
}
table#api-pkgs td
{
  border-bottom: 1px dotted gray;
  padding: 0.2em 0.5em;
} 
</style>
</head>

<body onLoad="loadCookies();cleanTitles();togglePage('api_member', 'Packages');"
   onUnload="saveCookies();">
  <jsp:include page="main_header.jsp" flush="true" />
  <jsp:include page="api_header.jsp" flush="true" />
  
  <div class="pagebody">

  <div id="api_main" style="display: none;">
  
<table width="80%" style="border-bottom: thin solid #808080;"><tr><td align="left">
Publisher:  <%=api.getPublisher()%>
</td><td align="right">
Version: <%=api.getVersion()%>
</td></tr></table>

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
<div style="height: 200px; overflow: auto; border: thin solid #bbb; padding: 5px;">
  <%= api.getDescription() %>
</div>

</div>

<div id="api_member" style="display: none;">

<div style="float: right;">
  <button id="toggleBtn" onClick="toggleHeightMode('api-pkglist', this);"><img src="images/expand.jpg" /></button>
</div>
<div class="api" id="api-pkglist-caption"><%=api.getName()%> API Packages</div>
<div id="api-pkglist">
<table id="api-pkgs" cellspacing="0">
<tbody>
    <%
     List pkgs = api.getPackages();
     JPackage pkg;
     
     for (int i=0; i<pkgs.size(); i++)
     {
          pkg = (JPackage) pkgs.get(i);
          %>
        <tr <% if (i%2==1) { %> bgcolor="beige" <% } %>>
          <td>
            <a href="pkg.main.do?pkg_id=<%=pkg.getId()%>">
              <span class="package"><%=pkg.getName()%></span>
             </a>
          </td>
          <td>
            <%= pkg.getSummaryDescription() %>
          </td>
        </tr>
  <%
     }
   %>
</tbody>
</table>
</div>

</div>
  
  
  </div> <!-- end page body -->

<br/><br/>

  <jsp:include page="footer.html" flush="true" />


</body>
</html>

