<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  
  String title = pkg.getName();
  if (pkg.getAPI() != null)
  {
    title = pkg.getAPI().getName() + ": Package " + pkg.getName();
  }
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=title%></TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="/<%=request.getContextPath()%>/basic/stylesheet.css" TITLE="Style">
</HEAD>


<BODY BGCOLOR="white">
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
   
    <A NAME="description">&nbsp;</A> 
    <H2>Package <%=pkg.getName()%> Description</H2>
    <P><%=pkg.getDescription()%></P>
    
    <jsp:include page="doc_footer.jsp" flush="true"/>

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
