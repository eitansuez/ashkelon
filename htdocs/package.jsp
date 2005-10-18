<%@ page info="main package view" import="org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=pkg.getName()%> at a glance</title>
  <jsp:include page="includes.jsp" flush="true" />
</head>


<body onLoad="configureExpandCollapse();loadCookies();initTabs('pkg_member', '<%=pkg.getName()%>');cleanTitles('span');cleanTitles();"
      onUnload="saveCookies();">

  <u2d:include page="main_header.jsp" dynamic="true" />
  <jsp:include page="pkg_header.jsp" flush="true"/>
  
  <div class="pagebody">

    <div id="pkg_main" style="display: none;">
      <jsp:include page="pkg_main.jsp" flush="true"/>
    </div>

    <div id="pkg_member" style="display: none;">
      <jsp:include page="pkg_member.jsp" flush="true"/>
    </div>

    
    <% for (int i=0; i<ClassType.CLASSTYPES.length; i++)
       {
          String clsTypeName = ClassType.CLASSTYPES[i];
    %>
      <div id="pkg_<%=clsTypeName%>" STYLE="display: none;">
        <jsp:include page="pkg_classes.jsp" flush="true">
          <jsp:param name="classtype" value="<%=clsTypeName%>"/>
        </jsp:include>
      </div>
    <% } %>
    
    <div id="pkg_tree" style="display: none;">
      <u2d:include page="pkg_tree.jsp" dynamic="true"/>
    </div>
  
    <jsp:include page="doc_footer.jsp" flush="true"/>
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>
