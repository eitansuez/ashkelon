<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

Template page for Class or Interface types.
--%>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  String cmd = ServletUtils.getCommand(request);
  String[] parts = StringUtils.split(cmd, ".");
  String inittab = "cls_"+parts[1];
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=cls.getName()%> at a glance</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
    function initTabs()
    {
      var page_signature = readCookie("pagecontext");
      if (location.search == page_signature)
      {  // must be a reload or a back button press
        togglePage(readCookie("tabid"), "<%=cls.getName()%>");
      } else
      {
        togglePage("<%=inittab%>", "<%=cls.getName()%>");
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
  <jsp:include page="cls_header_clt.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">
    <jsp:include page="cls_info.jsp" flush="true"/>
  
    <DIV ID="cls_main" STYLE="display: none;">
      <jsp:include page="cls_main.jsp" flush="true"/>
    </DIV>    
    
    <DIV ID="cls_inner" STYLE="display:none;">
      <jsp:include page="cls_inner.jsp" flush="true"/>
    </DIV>
    
    <DIV ID="cls_member" STYLE="display:none;">
      <jsp:include page="cls_member.jsp" flush="true"/>
    </DIV>    

    <% for (int i=0; i<Member.MEMBERTYPES.length; i++)
       {
          String memberTypeName = Member.MEMBERTYPES[i];
    %>
      <DIV ID="cls_<%=memberTypeName%>" STYLE="display:none;">
        <jsp:include page="cls_member_type.jsp" flush="true">
          <jsp:param name="membertype" value="<%=memberTypeName%>"/>
        </jsp:include>
      </DIV>
    <% } %>
     
    <DIV ID="cls_tree" STYLE="display: none;">
      <jsp:include page="cls_tree.jsp" flush="true"/>
    </DIV>
  
    <DIV ID="cls_xref" STYLE="dislay: none;">
      <jsp:include page="cls_xref.jsp" flush="true"/>
    </DIV>
    
    <jsp:include page="doc_footer.jsp" flush="true">
    </jsp:include>
  </DIV> <!-- end page body -->
  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
