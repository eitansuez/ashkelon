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
	<TITLE>Ashkelon - Known Issues</TITLE>
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

<H3>
Known Issues
</H3>

<OL>
<LI>
Ashkelon is a beta quality product.  You may encounter bugs and inconsitencies in its behavior (in which case you are encouraged to <A HREF="mailto:eitan@uptodata.com">contact</A> the author so that the bug or inconsistency may be fixed)
</LI>
<LI>
  inheritance trees currently show only class inheritance.  interface
  inheritance structures are not shown
</LI>
<LI>
  Ashkelon does not currently provide class serialization information (javadoc does this)
</LI>
</OL>

<HR>

<STYLE>
.question
{
  font-style: italic;
}

</STYLE>

<P>
I will post new entries to this FAQ for any questions that are submitted.  You may <A HREF="mailto:eitan@uptodata.com">submit questions</A> directly to the author.
</P>

<H3>
FAQ
</H3>

<OL>
  <LI>
   <SPAN CLASS="question">Why are docs for Sun's J2SE (or other Sun APIs) not available in Ashkelon's repository?</SPAN>
   <P><B>Answer</B></P>
   <P>

While publishing of Sun javadocs on intranets is permitted, publishing javadocs on the internet requires explicit permission and is a more complicated thing to do from a legal perspective. UptoData has thus far not gone through the trouble to obtain permission to publish Sun's Java APIs (but nothing is stopping you from running Ashkelon on your intranet fully populated with Java 2 APIs). 

<!--
   While publishing of Sun javadocs on intranets is permitted, publishing javadocs on the internet requires explicit permission.  UptoData has thus far been unable to obtain permission from Sun to publish their APIs.  To quote a message from Sun's JavaDoc team:<BR><BR>

<UL STYLE="margin: 0 50 0 50; font-style: italic;">

We wouldn't likely provide support (such as republishing J2SE API) for a program unless it was markedly better than what we already have. DocFather already provides search capability at:<BR><BR>

<UL STYLE="margin: 0 50 0 50;">
<A HREF="http://www.siteforum.com/servlets/sfs?s=ZG7jwK2J5buRQyay&i=983199639937&b=983199639937&t=gateway&u=guest&p=guest&include=gateway&includeT=onSunSearch" TARGET="_new">
http://www.siteforum.com/servlets/sfs?s=ZG7jwK2J5buRQyay&
i=983199639937&b=983199639937&t=gateway&u=guest&p=guest&
include=gateway&includeT=onSunSearch
</A>
</UL>

</UL>

-->

   </P>
  </LI>
</OL>

</DIV>

<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
