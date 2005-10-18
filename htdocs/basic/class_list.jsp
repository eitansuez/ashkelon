<%@ page info="package classes view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

A re-usable component for listing classes.  Quite flexible and useful.  Can be used for package class
listing and for class search results and for browsing through classes.  But, must pass a list of parameters
as follows:
  1. caption for table
  2. message to display if list is empty
  3. whether to display class names fully qualified or not
  4. whether class list is a uniform list of one class type (all interfaces, for example)
  5. id of div container that this component is located in (needed for filter feature to work properly)

  Here's a sample call spec:
    request.setAttribute("classes", classlist);
    <jsp:include page="class_list.jsp" flush="true">
      <jsp:param name="caption" value="Brief Class Descriptions" />
      <jsp:param name="empty_msg" value="No class information available for package" />
      <jsp:param name="display_qualified" value="false" />
      <jsp:param name="classes_type" value="interface" />
      <jsp:param name="div_id" value="pkg_classes" />
    </jsp:include>
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  String caption = ServletUtils.getRequestParam(request, "caption");
  if (StringUtils.isBlank(caption))
  {
    caption = "Class List";
  }
  
  String emptyMsg = ServletUtils.getRequestParam(request, "empty_msg");
  if (StringUtils.isBlank(emptyMsg))
  {
    emptyMsg = "No classes found.";
  }
  
  String displayQualified = ServletUtils.getRequestParam(request, "display_qualified");
  if (StringUtils.isBlank(displayQualified))
  {
    displayQualified = "true";
  }
  
  String classestype = ServletUtils.getRequestParam(request, "classes_type");
  if (StringUtils.isBlank(classestype))
  {
    classestype = "all";
  }
  
  List classes = (List) request.getAttribute("classes");
  if (classes == null)
  {
    classes = new ArrayList();
  }
  
  String containerDiv = ServletUtils.getRequestParam(request, "div_id");
  
%>

<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (classes.isEmpty()) { %>
  <P CLASS="message"><%=emptyMsg%></P>
<% } else { %>

  
<%
   ClassType cls = null;
   String clsDescr = "";
   boolean isinterface = "interface".equals(classestype);
   int colsadjust = (isinterface) ? (0) : (2);
   String divid = containerDiv;
   String colname_heading = (classestype.equals("all")) ? "Class Names" : classestype + " Names";
   boolean qualify = ("true".equals(displayQualified)) ? true : false;
   String classtype = "";
%>


<DIV ALIGN="CENTER"><B><%=caption%></B></DIV>

<TABLE BORDER="1" CLASS="columnar" WIDTH="100%" CELLPADDING="3" CELLSPACING="0">
<TR>
<TD STYLE="font-size: x-small; font-variant:small-caps;">R<BR>e<BR>s<BR>e<BR>t</TD>
<% if (!isinterface) { %>
<TD STYLE="cursor: hand; font-size: x-small; font-variant:small-caps;">a<BR>b<BR>s<BR>t<BR>r</TD>
<% } %>
<TD STYLE="cursor: hand; font-size: x-small; font-variant:small-caps;">p<BR>u<BR>b<BR>l<BR>i<BR>c</TD>
<TD STYLE="cursor: hand; font-size: x-small; font-variant:small-caps;">s<BR>t<BR>a<BR>t<BR>i<BR>c</TD>
<% if (!isinterface) { %>
<TD STYLE="cursor: hand; font-size: x-small; font-variant:small-caps;">f<BR>i<BR>n<BR>a<BR>l</TD>
<% } %>
<TD STYLE="font-size: x-small; font-variant:small-caps;"" TITLE="Version of API that this member was introduced">s<BR>i<BR>n<BR>c<BR>e</TD>
<TD STYLE="cursor: hand; font-size: x-small; font-variant:small-caps;">d<BR>e<BR>p<BR>r<BR>e<BR>c</TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;"><%=colname_heading%></TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;">Summary Description</TD>
</TR>



<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
     clsDescr = cls.getSummaryDescription();
     classtype = cls.getClassTypeName();
%>
<TR ID="memberrow<%=i%>" CLASS="<%=cls.getModifiers() + (cls.isDeprecated() ? " deprecated" : " ")%>" STYLE="display: ;">
  <TD>&nbsp;</TD>
<% if (!isinterface) { %>
  <TD>
    <% if (cls.isAbstract()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } else { out.println("&nbsp;"); } %>
  </TD>
<% } %>
  <TD>
    <% if (cls.getAccessibility() == JDocUtil.PUBLIC) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } else { out.println("&nbsp;"); } %>
  </TD>
  <TD>
    <% if (cls.isStatic()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } else { out.println("&nbsp;"); } %>
  </TD>
<% if (!isinterface) { %>
  <TD>
    <% if (cls.isFinal()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } else { out.println("&nbsp;"); } %>
  </TD>
<% } %>
  <TD><%=cls.getDoc().getCleanSince()%>&nbsp;</TD>
  <TD>
    <% if (cls.isDeprecated()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } else { out.println("&nbsp;"); } %>
  </TD>
  <TD><A HREF="cls.main.do?id=<%=cls.getId()%>" TITLE="<%=HtmlUtils.cleanAttributeText(clsDescr)%>"><SPAN CLASS="<%=classtype%>"><%=(qualify) ? cls.getQualifiedName() : cls.getName()%></SPAN></A></TD>
  <TD><%=clsDescr%></TD>
</TR>
<% } // end for %>


</TABLE>



<% } // end else statement (if packages is empty) %>

