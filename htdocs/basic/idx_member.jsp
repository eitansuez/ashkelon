<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
    needToDisplayResults = new Boolean(false);
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc Member Index</TITLE>
  <jsp:include page="../includes.html" flush="true"/>

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  List results = (List) request.getAttribute("results");
  request.setAttribute("members", results);
  String emptyMsg = "No members found";
 %>

 <BR>
 
<DIV ID="member_index">
<jsp:include page="member_list.jsp" flush="true">
  <jsp:param name="caption" value="Member Index" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="true" />
  <jsp:param name="members_type" value="all" />
  <jsp:param name="div_id" value="member_index" />
</jsp:include>
</DIV>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<FORM METHOD="GET" ACTION="index.html">
  <INPUT TYPE="HIDDEN" NAME="cmd" VALUE="idx.member">
  <INPUT TYPE="HIDDEN" NAME="start" VALUE="<%=request.getAttribute("next")%>">
  <INPUT TYPE="SUBMIT"
          STYLE="background-color: #dddddd; font-size: 8 pt;"
          ACCESSKEY="N" VALUE="Next &gt;">
</FORM>
<% } // end if %>


<% } else { %>

<P><B>Member Index Page.</B></P>

<P>Use the A-Z buttons above to browse packages alphabetically by name.</P>

<% } %>


</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>