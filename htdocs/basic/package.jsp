<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  
  String title = pkg.getName();
  if (pkg.getAPI() != null)
  {
    title = pkg.getAPI().getName() + ": Package " + pkg.getName();
  }
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=title%></title>
  <link rel="stylesheet" type="text/css" href="/<%=request.getContextPath()%>/basic/stylesheet.css" TITLE="Style" />
</head>


<body bgcolor="white">
<jsp:include page="main_header.jsp" flush="true"/>
  
    <jsp:include page="pkg_main.jsp" flush="true"/>

    <% for (int i=0; i<ClassType.CLASSTYPES.length; i++)
       {
          String clsTypeName = ClassType.CLASSTYPES[i];
    %>
        <jsp:include page="pkg_classes.jsp" flush="true">
          <jsp:param name="classtype" value="<%=clsTypeName%>"/>
        </jsp:include>
        
    <% } %>
   
    <a name="description">&nbsp;</a>
    <h2>Package <%=pkg.getName()%> Description</h2>
    <p><%=pkg.getDescription()%></p>
    
    <jsp:include page="doc_footer.jsp" flush="true"/>

<jsp:include page="footer.html" flush="true"/>

</body>
</html>

