<%@ page info="main member view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
  Member member = (Member) request.getAttribute("member");
  String membertypename = member.getMemberTypeName();
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><%=member.getName()%></title>
  <jsp:include page="includes.html" flush="true"/>
</head>

<body onLoad="cleanTitles('A');cleanTitles('span');">
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <div class="pagebody">
    <jsp:include page="member_info.jsp" flush="true"/>
  
     <% if ("field".equals(membertypename)) { %>
      <jsp:include page="member_field.jsp" flush="true"/>
     <% } else { %>
      <jsp:include page="member_exec.jsp" flush="true"/>
     <% } %>

    <jsp:include page="doc_footer.jsp" flush="true"/>
  </div> <!-- end page body -->
  
<c:import url="footer.html" />

</body>
</html>
