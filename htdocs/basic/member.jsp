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
  <SCRIPT>
  function dohref(url)
  {
    window.opener.location.href = url;
  }
  </SCRIPT>
</HEAD>

<BODY BGCOLOR="white">
<jsp:include page="main_header.jsp" flush="true"/>

Package: <A HREF="index.html?cmd=pkg.main&pkg_id=<%=member.getPackage().getId()%>"><%=member.getPackage().getName()%></A>
|
Class: <A HREF="index.html?cmd=cls.main&cls_id=<%=member.getContainingClass().getId()%>"><%=member.getContainingClass().getName()%></A>
  
     <% if ("field".equals(membertypename)) { %>
      <jsp:include page="member_field.jsp" flush="true"/>
     <% } else { %>
      <jsp:include page="member_exec.jsp" flush="true"/>
     <% } %>


<!-- 
    <DIV ALIGN="RIGHT">
     <FORM>
       <INPUT TYPE="BUTTON" VALUE="Close" onClick="window.close();">
     </FORM>
     </DIV>
     
 -->     

<jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
