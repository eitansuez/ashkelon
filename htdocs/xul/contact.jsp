<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: December 2001
--%>

<%
  String ui = (String) session.getAttribute("ui");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>dbdoc - Contact Information</TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  LI
  {
    margin: 0 0 10 0;
  }
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<P>

<UL>
<LI>If there's an open source API that you'd like to see in the dbdoc repository; or </LI>
<LI>If you'd like to license dbdoc for publishing your own docs on your company intranet; or </LI>
<LI>For any comments or feedback on this site or on dbdoc; or</LI>
<LI>For any questions not already answered in the <A HREF="index.html?cmd=issues">FAQ</A></LI>
</UL>

..please contact:
</P>

<UL>
Eitan Suez
<BR>
<A HREF="mailto:eitan@u2d.com">eitan@u2d.com</A>
<BR>
UptoData, Inc.
<BR>
Austin, Texas
</UL>

</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
