<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

Template page for Class or Interface types.
--%>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <!-- Todo: title should include api name -->
	<TITLE><%=cls.getName()%></TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="/<%=request.getContextPath()%>/basic/stylesheet.css" TITLE="Style">
  <SCRIPT>
  function dialog(url)
  {
    var dlg = window.open(url, "member", "scrollbars=1,width=500,height=350,resizable=1");
    dlg.focus();
  }
  </SCRIPT>

</HEAD>

<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>

<jsp:include page="cls_header_svr.jsp" flush="true"/>

    <jsp:include page="cls_info.jsp" flush="true"/>

    <jsp:include page="doc_footer.jsp" flush="true"/>
    <HR>

    <jsp:include page="cls_inner.jsp" flush="true"/>
    <BR>
    <jsp:include page="cls_member.jsp" flush="true"/>

<%-- 
    <% for (int i=0; i<Member.MEMBERTYPES.length; i++)
       {
          String memberTypeName = Member.MEMBERTYPES[i];
    %>
      <A NAME="cls_<%=memberTypeName%>">&nbsp;</A> 
        <jsp:include page="cls_member_type.jsp" flush="true">
          <jsp:param name="membertype" value="<%=memberTypeName%>"/>
        </jsp:include>
    <% } %>
--%>

<BR>
      <jsp:include page="cls_xref.jsp" flush="true"/>

      
<jsp:include page="footer.html" flush="true"/>
  
</BODY>
</HTML>
