<%@ page info="package classes view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.*" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

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

<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (classes.isEmpty()) { %>
  <p class="message"><%=emptyMsg%></p>
<% } else { %>

  
<%
   ClassType cls = null;
   String clsDescr = "";
   boolean isinterface = "interface".equals(classestype);
   int colsadjust = (isinterface) ? (0) : (2);
   String divid = containerDiv;
   String colname_heading = (classestype.equals("all")) ? "Class Names" : classestype + " Names";
   boolean qualify = ("true".equals(displayQualified));
   String classtype = "";
%>

<style type="text/css">
#class-listing thead td
{     
  background-color: beige;
  cursor: default;
  font-size: small;
  font-variant: small-caps;
}
#class-listing thead td.dynamic, #class-listing thead td.thin
{
  width: 1.1em;
  text-align: center;
  font: 12pt monospace;
  font-weight: bold;
  font-variant: small-caps;
}
#class-listing thead td.dynamic:hover
{ 
  background-color: #ff8;
  cursor: pointer; 
} 
#class-listing tbody td
{ 
  padding: 0.1em;
}
#class-listing tbody tr:hover
{
  background-color: #ff8;
}

</style>

<table id="class-listing" border="1" class="columnar" width="100%" align="center">
<caption><%=caption%></caption>
<colgroup span="<%=colsadjust+5%>">
  <col width="15" align="center">
</colgroup>
<colgroup span="2">
  <col width="100">
  <col width="300">
</colgroup>
<thead class="table_header">
<tr title="Click on one of the modifiers to filter the table by modifier type">
<td class="dynamic" onClick="filterRows('memberrow', 'reset', '<%=divid%>');" title="Reset table to include all rows">c l e a r</td>
<% if (!isinterface) { %>
<td class="dynamic" onClick="filterRows('memberrow', 'abstract', '<%=divid%>');" title="Show only abstract members">a b s t r</td>
<% } %>
<td class="dynamic" onClick="filterRows('memberrow', 'public', '<%=divid%>');" title="Show only public members">p u b l i c</td>
<td class="dynamic" onClick="filterRows('memberrow', 'static', '<%=divid%>');" title="Show only static members">s t a t i c</td>
<% if (!isinterface) { %>
<td class="dynamic" onClick="filterRows('memberrow', 'final', '<%=divid%>');" title="Show only final members">f i n a l</td>
<% } %>
<td class="thin" title="Version of API that this member was introduced">s i n c e</td>
<td class="dynamic" onClick="filterRows('memberrow', 'deprecated', '<%=divid%>');" title="Show only deprecated members">d e p r e c</td>
<td><%=colname_heading%></td>
<td>Summary Description</td>
</tr>
</thead>
<tbody>
<% for (int i=0; i<classes.size(); i++)
   {
     cls = (ClassType) classes.get(i);
     clsDescr = cls.getSummaryDescription();
     classtype = cls.getClassTypeName();
%>
<tr id="memberrow<%=i%>" class="<%=cls.getModifiers() + (cls.isDeprecated() ? " deprecated" : " ")%>" style="display: ;">
  <td>&nbsp;</td>
  <% if (!isinterface) { %>
  <td>
    <% if (cls.isAbstract()) { %>
    <u2d:imgref ref="images/check_sm.gif" border="0" />
    <% } %>
  </td>
<% } %>
  <td>
    <% if (cls.getAccessibility() == JDocUtil.PUBLIC) { %>
    <u2d:imgref ref="images/check_sm.gif" border="0" />
    <% } %>
  </td>
  <td>
    <% if (cls.isStatic()) { %>
    <u2d:imgref ref="images/check_sm.gif" border="0" />
    <% } %>
  </td>
<% if (!isinterface) { %>
  <td>
    <% if (cls.isFinal()) { %>
    <u2d:imgref ref="images/check_sm.gif" border="0" />
    <% } %>
  </td>
<% } %>
  <td><%=cls.getDoc().getCleanSince()%></td>
  <td>
    <% if (cls.isDeprecated()) { %>
    <u2d:imgref ref="images/check_sm.gif" border="0" />
    <% } %>
  </td>
  <td>
     <u2d:link to="class" elem="<%=cls%>"><span class="<%=classtype%>" title="<%=HtmlUtils.cleanAttributeText(clsDescr)%>"><%=(qualify) ? cls.getQualifiedName() : cls.getName()%></span></u2d:link>
  </td>
  <td><%=clsDescr%></td>
</tr>
<% } // end for %>

</tbody>
</table>


<% } // end else statement (if packages is empty) %>

