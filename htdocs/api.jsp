<%@ page info="main api view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  API api = (API) request.getAttribute("api");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=api.getName()%></title>
  <jsp:include page="includes.html" flush="true" />

<style>
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
  
    <jsp:include page="api_info.jsp" flush="true" />
    
    <div style="height: 200px; overflow: auto; border: thin solid #bbb; padding: 5px;">
      <%= api.getDescription() %>
    </div>

  </div>

  <div id="api_member" style="display: none;">

    <jsp:include page="api_info.jsp" flush="true" />

    <div style="float: right;">
      <button id="toggleBtn" onClick="toggleHeightMode('api-pkglist', this, '330px');"><img src="images/expand.jpg" /></button>
    </div>

    <div class="api" id="api-pkglist-caption"><%=api.getName()%> Packages</div>
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

</div>

<c:import url="footer.html" />

</body>
</html>

