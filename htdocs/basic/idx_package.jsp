<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  String cmd = (String) request.getAttribute("cmd");
  
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>dbdoc Package Index</TITLE>
  <jsp:include page="../includes.html" flush="true"/>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  List results = (List) request.getAttribute("results");
  JPackage pkg;
 %>

 <OL>
 <% for (int i=0; i<results.size(); i++) { %>
 <%  pkg = (JPackage) results.get(i); %>

    <LI>
    <A HREF="pkg.main.do?pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A>
    </LI>

 <% } %>
</OL>
 
<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<FORM METHOD="GET" ACTION="idx.package.do">
  <INPUT TYPE="HIDDEN" NAME="start" VALUE="<%=request.getAttribute("next")%>">
  <INPUT TYPE="SUBMIT"
          STYLE="background-color: #dddddd; font-size: 8 pt;"
          ACCESSKEY="N" VALUE="Next &gt;">
</FORM>
<% } // end if %>

<% } else { %>

<P><B>Package Index Page.</B></P>

<P>Use the A-Z buttons above to browse packages alphabetically by name.</P>

<% } %>


</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
