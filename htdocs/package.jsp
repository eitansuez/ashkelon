<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=pkg.getName()%> at a glance</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
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
    
  </SCRIPT>
</HEAD>


<BODY onLoad="init();loadCookies();initTabs();cleanTitles('SPAN');cleanTitles();"
      onUnload="saveCookies();">
      
  <jsp:include page="main_header.jsp" flush="true"/>
  <jsp:include page="pkg_header.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">

    <DIV ID="pkg_main" STYLE="display: none;">
      <jsp:include page="pkg_main.jsp" flush="true"/>
    </DIV>    

    <DIV ID="pkg_member" STYLE="display: none;">
      <jsp:include page="pkg_member.jsp" flush="true"/>
    </DIV>    

    
    <% for (int i=0; i<ClassType.CLASSTYPES.length; i++)
       {
          String clsTypeName = ClassType.CLASSTYPES[i];
    %>
      <DIV ID="pkg_<%=clsTypeName%>" STYLE="display: none;">
        <jsp:include page="pkg_classes.jsp" flush="true">
          <jsp:param name="classtype" value="<%=clsTypeName%>"/>
        </jsp:include>
      </DIV>
    <% } %>
    
    <DIV ID="pkg_tree" STYLE="display: none;">
      <jsp:include page="pkg_tree.jsp" flush="true"/>
    </DIV>
  
    <jsp:include page="doc_footer.jsp" flush="true"/>
  </DIV> <!-- end page body -->
  
  <jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
