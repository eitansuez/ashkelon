<%@ page info="top page: package list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" 
  contentType="application/vnd.mozilla.xul+xml" %><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><?xml version="1.0"?>
<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href="xul/global.css" type="text/css"?>

<%
  HashMap cols = (HashMap) application.getAttribute("javaPkgs");
 %>

<window
  xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul"
  xmlns:html="http://www.w3.org/1999/xhtml"
  title="Ashkelon: Packages" orient="vertical">

 <script type="application/x-javascript" src="xul/global.js" />

 <c:import url="header.jsp" />

   <vbox id="body" flex="1">

<% if (cols.isEmpty()) { %>
 <description>No packages exist in the dbdoc repository at this time.</description>
<% } else { %>

    <hbox pack="center">
     <description class="subtitle">Packages by Type</description>
    </hbox>

    <vbox flex="1">
     <hbox flex="1">

     <%
     String columnName = null;
     Set keys = cols.keySet();
     Iterator iter = keys.iterator();
     Object item;
     while (iter.hasNext())
     {
       item = iter.next();
       columnName = item.toString();
     %>
        <listbox flex="1" style="margin: 0px;">
         <listhead>
           <listheader label="<%=columnName%>"/>
         </listhead>
       <%

       JPackage pkg;
       String title;
       List col = (List) cols.get(item);
       for (int j=0; j<col.size(); j++)
       {
          pkg = (JPackage) col.get(j);
          title = pkg.getDoc().getSummaryDescription();
          if (StringUtils.isBlank(title))
            title = pkg.getName(); 
%>
	<listitem onclick="navto('pkg.main','pkg_id=<%=pkg.getId()%>');" label="<%=pkg.getName()%>" />
  <% }  // end inner for loop %>
    </listbox>
<% } // end while %>
     </hbox>
    </vbox>
     
<% } // end if %>
  </vbox>

  <c:import url="footer.jsp" />


</window>
