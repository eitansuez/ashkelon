<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=pkg.getName()%> at a glance</title>
  <jsp:include page="includes.html" flush="true" />

  <script>
    function initTabs()
    {
      var context = readCookie("pagecontext");
      if (location.search == context)
      {  // must be a reload or a back button press
        togglePage(readCookie("tabid"), "<%=pkg.getName()%>");
      } else
      {
        togglePage("pkg_member", "<%=pkg.getName()%>");
      }
      // enable tabs:
      for (var i=0; i<cmds.length; i++)
      {
        var tag = getElementById(cmds[i]+"_tabchild");
        tag.disabled = false;
      }
    }
    
    function init()
    {
      if (IE)
        document.onclick = expandCollapse;
      else
        document.addEventListener("click", expandCollapse, false);
    }
    
  </script>
</head>


<body onLoad="init();loadCookies();initTabs();cleanTitles('SPAN');cleanTitles();"
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
