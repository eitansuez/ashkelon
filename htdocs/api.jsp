<%@ page info="main api view" import="java.util.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>


<%
  API api = (API) request.getAttribute("api");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=api.getName()%></title>
  <jsp:include page="includes.jsp" flush="true" />

<style type="text/css">
#api-pkglist
{
  padding: 0;
  text-align: center;
  border: 1px solid gray;
  clear: right;
  height: 330px;
  overflow: auto;
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
   
<u2d:include page="main_header.jsp" dynamic="true" />
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
      <button id="toggleBtn" onClick="toggleHeightMode('api-pkglist', this, '330px');"><u2d:imgref ref="images/expand.jpg" /></button>
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
                <u2d:link to="package" elem="<%=pkg%>">
                   <span class="package"><%=pkg.getName()%></span>
                </u2d:link>
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


<!--
<div class="actions">
  <a href="api.edit.do?id=<%=api.getId()%>">Edit API Info</a> | 
  <a href="api.rebuild.do">Rebuild API Docs</a> | 
  <a href="api.remove.do">Remove API Docs from System</a>
</div>
-->


<c:import url="footer.html" />

</body>
</html>

