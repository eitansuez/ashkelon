<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=pkg.getName()%> at a glance</title>
  <jsp:include page="includes.html" flush="true" />
</head>


<body onLoad="configureExpandCollapse();loadCookies();initTabs('pkg_member', '<%=pkg.getName()%>');cleanTitles('span');cleanTitles();"
      onUnload="saveCookies();">
      
  <jsp:include page="main_header.jsp" flush="true"/>
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
    
    <div ID="pkg_tree" STYLE="display: none;">
      <jsp:include page="pkg_tree.jsp" flush="true"/>
    </div>
  
    <jsp:include page="doc_footer.jsp" flush="true"/>
  </div> <!-- end page body -->
  
  <jsp:include page="footer.html" flush="true"/>

</body>
</html>
