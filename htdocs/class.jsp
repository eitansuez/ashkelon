<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
  ClassType cls = (ClassType) request.getAttribute("cls");
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=cls.getName()%> at a glance</title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="configureExpandCollapse();loadCookies();initTabs('cls_member', '<%=cls.getName()%>');cleanTitles('span');cleanTitles();"
      onUnload="saveCookies();">
      
  <jsp:include page="main_header.jsp" flush="true"/>
  <jsp:include page="cls_header_clt.jsp" flush="true"/>
  
  <div class="pagebody">
    <jsp:include page="cls_info.jsp" flush="true"/>
  
    <div id="cls_main" style="display: none;">
      <jsp:include page="cls_main.jsp" flush="true"/>
    </div>
    
    <div id="cls_inner" style="display:none;">
      <jsp:include page="cls_inner.jsp" flush="true"/>
    </div>
    
    <div id="cls_member" style="display:none;">
      <jsp:include page="cls_member.jsp" flush="true"/>
    </div>

    <% for (int i=0; i<Member.MEMBERTYPES.length; i++)
       {
          String memberTypeName = Member.MEMBERTYPES[i];
    %>
      <div id="cls_<%=memberTypeName%>" style="display:none;">
        <jsp:include page="cls_member_type.jsp" flush="true">
          <jsp:param name="membertype" value="<%=memberTypeName%>"/>
        </jsp:include>
      </div>
    <% } %>
     
    <div id="cls_tree" style="display: none;">
      <jsp:include page="cls_tree.jsp" flush="true"/>
    </div>
  
    <div id="cls_xref" style="dislay: none;">
      <jsp:include page="cls_xref.jsp" flush="true"/>
    </div>
    
    <jsp:include page="doc_footer.jsp" flush="true" />
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>
