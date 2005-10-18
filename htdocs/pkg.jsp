<%@ page info="top page: package list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  HashMap cols = (HashMap) application.getAttribute("javaPkgs");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Ashkelon - Packages</title>
  <c:import url="includes.html" />
  <style>
  div.pkg-family
  {
    width: 25%;
    float: left;
    text-align: left;
  }
  div.pkg-family p
  {
    margin: 0px;
    text-align: center;
    background-color: beige;
    border: 1px solid gray;
    border-width: 1px 1px 0px 1px;
  }
  div.pkg-family ol
  {
    margin-top: 0px;
    height: 440px;
    border: 1px solid gray;
    overflow: auto;
  }
  div.pkg-family ol li
  {
    border-bottom: 1px dotted gray;
  }
  </style>
</head>

<body onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true" />

<div class="pagebody">

<% if (cols.isEmpty()) { %>
 <p>No packages exist in the Ashkelon repository at this time.</p>
<% } else { %>

<div style="text-align: center;">
<p style="font-size: 120%; margin-bottom: 0px;">Packages by Type</p>

  <%
    JPackage pkg;
    String title;
    List col;
    String columnName;
    Iterator iter = cols.keySet().iterator();
    while (iter.hasNext())
    {
       columnName = iter.next().toString();
       col = (List) cols.get(columnName);
  %>
  <div class="pkg-family">
  <p style="font-weight: bold;"><%=columnName%>.*</p>
  <ol>
   <%
       for (int j=0; j<col.size(); j++)
       {
          pkg = (JPackage) col.get(j);
          title = pkg.getDoc().getSummaryDescription();
          if (StringUtils.isBlank(title))
            title = pkg.getName(); 
   %>
   <li>
        <a href="pkg.main.do?id=<%=pkg.getId()%>"><span class="package" title="<%=HtmlUtils.cleanAttributeText(title)%>"><%=pkg.getName()%></span></a>
   </li>
   <%   }  // end inner for loop %>
  </ol>
  </div>
<%  } // end while %>
</div>

<% } // end if %>

</div>

<div style="clear: left; margin-bottom: 1em;">&nbsp;</div>

<c:import url="footer.html" />

</body>
</html>

