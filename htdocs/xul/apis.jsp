<%@ page info="top page: api list"
         import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"
         contentType="application/vnd.mozilla.xul+xml" %><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><?xml version="1.0"?>
<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href="xul/global.css" type="text/css"?>

<window
  title="Ashkelon: API Listing"
  xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul"
  xmlns:html="http://www.w3.org/1999/xhtml" orient="vertical">
  
 <script type="application/x-javascript" src="xul/global.js" />

  <c:import url="header.jsp" />

  <vbox id="body" flex="1">
     
     <hbox>
       <description class="title" flex="1">Welcome to Ashkelon</description>
     </hbox>
     
     <hbox>
       <description class="para" flex="1">
         Ashkelon is an online reference for Java API documentation (see help documentation for more information).  Ashkelon is open source and available for download off<spacer style="width: 3px;"/> <html:a href="http://sourceforge.net/projects/ashkelon/" target="_new"> sourceforge.net</html:a>.
       </description>
     </hbox>
     
     <spacer class="blank_line" />
   
     <hbox pack="center">
       <description class="subtitle">API Listing</description>
     </hbox>
 
     <hbox>
       <grid flex="1">
        <columns>
         <column />
         <column flex="1" />
        </columns>
        <rows>
        
        <c:forEach items="${apilist}" var="api">
         <row onclick="navto('api.main','id=<c:out value="${api.id}" />')">
           <description>
              <c:out value="${api.name}" />
           </description>
           <description><c:out value="${api.summaryDescription}" /></description>
           </row>
         </c:forEach>
        </rows>
       </grid>
     </hbox>
 
  </vbox>

  <jsp:include page="footer.jsp" flush="true"/>

</window>
