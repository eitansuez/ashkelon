<%@ page info="main member view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  Member member = (Member) request.getAttribute("member");
  String membertypename = member.getMemberTypeName();
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE><%=member.getName()%></TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="cleanTitles('A');cleanTitles('SPAN');">
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">
    <jsp:include page="member_info.jsp" flush="true"/>
  
     <% if ("field".equals(membertypename)) { %>
      <jsp:include page="member_field.jsp" flush="true"/>
     <% } else { %>
      <jsp:include page="member_exec.jsp" flush="true"/>
     <% } %>

    <jsp:include page="doc_footer.jsp" flush="true"/>
  </DIV> <!-- end page body -->
  
  <jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
