<%@ page info="admin page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: PAGE COMMENTS & DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: PAGE CODE --%>
<%
  DBMgr dbmgr = DBMgr.getInstance();
  String message = ServletUtils.getRequestParam(request, "message");
 %>

<%-- SECTION: PAGE TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Admin Page</TITLE>
  <jsp:include page="includes.html" flush="true" />

  <%-- SECTION: PAGE STYLES --%>
  <STYLE TYPE="text/css">
  </STYLE>

  <%-- SECTION: PAGE BEHAVIOR (JAVASCRIPT) --%>
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>

<DIV CLASS="PAGEBODY">

<jsp:include page="main_header.jsp" flush="true"/>
<%-- <jsp:include page="sub_header.jsp" flush="true"/> --%>

<P>Welcome to Ashkelon...eitan.</P>

<FIELDSET>
<LEGEND><B>Database Connection Pooling</B></LEGEND>
<P>Pool Status: <%=dbmgr.getPoolStatus()%></P>
<P CLASS="message"><%=message%></P>

<FORM METHOD="POST" ACTION="admin.db.do">
  <INPUT TYPE="HIDDEN" NAME="command" VALUE="resetconns">
  <BUTTON TYPE="SUBMIT">Reset Connections</BUTTON>
</FORM>
</FIELDSET>

<FIELDSET>
<LEGEND>Cache</LEGEND>

<UL>
<%
  Enumeration attributes = application.getAttributeNames();
  String att;
  Object val;
  int size;
  while (attributes.hasMoreElements())
  {
    att = (String) attributes.nextElement();
    val = application.getAttribute(att);
    if (val instanceof List)
      size = ((List) val).size();
    else if (val instanceof Map)
      size = ((Map) val).size();
    else
      continue;
 %>
   <LI><B><%=att%></B>: <%=size%></LI>
 <%
  }
 %>
</UL>

<FORM METHOD="POST" ACTION="admin.db.do">
  <INPUT TYPE="HIDDEN" NAME="command" VALUE="resetcache">
  <BUTTON TYPE="SUBMIT">Reset Cache</BUTTON>
</FORM>
</FIELDSET>

</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
