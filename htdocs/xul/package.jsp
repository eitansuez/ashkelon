<%@ page info="main package view"
         import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"
         contentType="application/vnd.mozilla.xul+xml" %><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><?xml version="1.0"?>
<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href="xul/global.css" type="text/css"?>

<window 
  xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul"
  xmlns:html="http://www.w3.org/1999/xhtml"
  title="Ashkelon: <c:out value="${pkg.name}" /> at a glance" orient="vertical">

 <script type="application/x-javascript" src="xul/global.js" />

  <c:import url="header.jsp" />

  <vbox id="body" flex="1">
  
 <tabbox flex="1">
  <c:import url="pkg_header.jsp" />
  
    <tabpanels flex="1">
    
     <tabpanel>
      <c:import url="pkg_main.jsp" />
     </tabpanel>

     <tabpanel>
      <c:import url="pkg_member.jsp" />
     </tabpanel>
    
    <c:forEach items="${ClassType.CLASSTYPES}" var="clsTypeName">
     <tabpanel>
        <c:import url="pkg_classes.jsp">
          <c:param name="classtype" value="${clsTypeName}" />
        </c:import>
     </tabpanel>
    </c:forEach>
    
     <tabpanel>
      <%-- <c:import url="pkg_tree.jsp" /> --%>
     </tabpanel>
     
    </tabpanels>
 </tabbox>
 
 </vbox>

    <%-- 
   <c:import url="doc_footer.jsp" />
    --%>

  <c:import url="footer.jsp" />
  
</window>


