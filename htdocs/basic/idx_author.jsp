<%@ page info="page" import="java.util.*, org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  Boolean needToDisplayResults = (Boolean) request.getAttribute("display_results");
  if (needToDisplayResults == null)
  {
    needToDisplayResults = new Boolean(false);
  }
 %>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
  <TITLE>dbdoc Author Index</TITLE>
  <jsp:include page="../includes.html" flush="true"/>
</HEAD>

<BODY>

<jsp:include page="main_header.jsp" flush="true"/>
<jsp:include page="idx_header.jsp" flush="true"/>

<jsp:include page="idx_az_header.jsp" flush="true"/>

<DIV CLASS="PAGEBODY">

<% if (needToDisplayResults.booleanValue()) { %>

<%
  String startFrom = request.getParameter("startFrom");
  String emptyMsg = "";
  if (StringUtils.isBlank(startFrom))
  {
    emptyMsg = "No authors found";
  } else
  {
    emptyMsg =   "Found no authors passed "+startFrom;
  }
  List authors = (List) request.getAttribute("results");
%>
 
<DIV ID="author_index">
<OL>
<% Author author;
   for (int i=0; i<authors.size(); i++)
   {
     author = (Author) authors.get(i);
   %>
  <LI><A HREF="author.do?id=<%=author.getId()%>"><%=author.getName()%></A></LI>
<% } %>
</OL>

</DIV>

<% if (!StringUtils.isBlank((String) request.getAttribute("next"))) { %>
<FORM METHOD="GET" ACTION="idx.author.do">
  <INPUT TYPE="HIDDEN" NAME="start" VALUE="<%=request.getAttribute("next")%>">
  <INPUT TYPE="SUBMIT"
          STYLE="background-color: #dddddd; font-size: 8 pt;"
          ACCESSKEY="N" VALUE="Next &gt;">
</FORM>
<% } // end if %>

<% } else { %>

<P><B>Author Index Page.</B></P>

<P>Use the A-Z buttons above to browse authors alphabetically by name.</P>

<% } %>


</DIV>


<jsp:include page="footer.html" flush="true"/>


</BODY>
</HTML>
