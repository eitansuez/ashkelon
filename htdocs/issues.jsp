<%@ page info="configuration/settings page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

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

<html>
<head>
  <title>Ashkelon - Known Issues</title>
  <jsp:include page="includes.jsp" flush="true"/>

  <style type="text/css">
  li
  {
    margin: 0 0 10px 0;
  }
  </style>

</head>

<body onLoad="cleanTitles();">
<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

<h3>
Known Issues
</h3>

<ol>
<li>
Ashkelon is a beta quality product.  You may encounter bugs and inconsitencies in its behavior (in which case you are encouraged to <a href="mailto:eitan@u2d.com">contact</a> the author so that the bug or inconsistency may be fixed)
</li>
<li>
  inheritance trees currently show only class inheritance.  interface
  inheritance structures are not shown
</li>
<li>
  Ashkelon does not currently provide class serialization information (javadoc does this)
</li>
</ol>

<hr/>

<style>
.question
{
  font-style: italic;
}

</style>

<p>
I will post new entries to this FAQ for any questions that are submitted.  You may <a href="mailto:eitan@u2d.com">submit questions</a> directly to the author.
</p>

<h3>
FAQ
</h3>

<ol>
  <li>
   <span class="question">Why are docs for Sun's J2SE (or other Sun APIs) not available in Ashkelon's repository?</span>
   <p><b>Answer</b></p>
   <p>

While publishing of Sun javadocs on intranets is permitted, publishing javadocs on the internet requires explicit permission and is a more complicated thing to do from a legal perspective. UptoData has thus far not gone through the trouble to obtain permission to publish Sun's Java APIs (but nothing is stopping you from running Ashkelon on your intranet fully populated with Java 2 APIs). 

<!--
   While publishing of Sun javadocs on intranets is permitted, publishing javadocs on the internet requires explicit permission.  UptoData has thus far been unable to obtain permission from Sun to publish their APIs.  To quote a message from Sun's JavaDoc team:<BR><BR>

<ul style="margin: 0 50 0 50; font-style: italic;">

We wouldn't likely provide support (such as republishing J2SE API) for a program unless it was markedly better than what we already have. DocFather already provides search capability at:<BR><BR>

<ul style="margin: 0 50 0 50;">
<a href="http://www.siteforum.com/servlets/sfs?s=ZG7jwK2J5buRQyay&i=983199639937&b=983199639937&t=gateway&u=guest&p=guest&include=gateway&includeT=onSunSearch" target="_new">
http://www.siteforum.com/servlets/sfs?s=ZG7jwK2J5buRQyay&
i=983199639937&b=983199639937&t=gateway&u=guest&p=guest&
include=gateway&includeT=onSunSearch
</a>
</ul>

</ul>

-->

   </p>
  </li>
</ol>

</div>

<c:import url="footer.html" />


</body>
</html>
