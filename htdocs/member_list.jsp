<%@ page info="class members view (component)" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

A re-usable component for listing members.  Quite flexible and useful.  Can be used for class member
listing and for member search results and for browsing through members.  But, must pass a list of parameters
as follows:
  1. caption for table
  2. message to display if list is empty
  3. whether to display member names fully qualified or not
  4. whether member list is a uniform list of one member type (all methods, for example)
  5. id of div container that this component is located in (needed for filter feature to work properly)

  Here's a sample call spec:
    request.setAttribute("members", memberlist);
    <jsp:include page="member_list.jsp" flush="true">
      <jsp:param name="caption" value="Brief Member Descriptions" />
      <jsp:param name="empty_msg" value="No member information available for class" />
      <jsp:param name="display_qualified" value="false" />
      <jsp:param name="members_type" value="method" />
      <jsp:param name="div_id" value="cls_method" />
    </jsp:include>
--%>


<%-- SECTION: COMPONENT CODE --%>
<%
  String caption = ServletUtils.getRequestParam(request, "caption");
  if (StringUtils.isBlank(caption))
  {
    caption = "Member List";
  }
  
  String emptyMsg = ServletUtils.getRequestParam(request, "empty_msg");
  if (StringUtils.isBlank(emptyMsg))
  {
    emptyMsg = "No members found.";
  }
  
  String displayQualified = ServletUtils.getRequestParam(request, "display_qualified");
  if (StringUtils.isBlank(displayQualified))
  {
    displayQualified = "true";
  }
  
  String memberstype = ServletUtils.getRequestParam(request, "members_type");
  if (StringUtils.isBlank(memberstype))
  {
    memberstype = "all";
  }
  
  List members = (List) request.getAttribute("members");
  if (members == null)
  {
    members = new ArrayList();
  }
  
  String containerDiv = ServletUtils.getRequestParam(request, "div_id");
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (members.isEmpty()) { %>
  <p class="message"><%=emptyMsg%></P>
<% } else { %>

<%
   Member member = null;
   MethodMember method = null;
   FieldMember field = null;
   String returnstuff = "";
   String memberDescr = "";
   boolean ismethod = "method".equals(memberstype);
   boolean isfield = "field".equals(memberstype);
   int colsadjust = (ismethod || "all".equals(memberstype)) ? (1) : (0);
   String divid = containerDiv;
   String colname_heading = ("all".equals(memberstype)) ? "Member Name" : memberstype + " Name";
   boolean qualify = ("true".equals(displayQualified)) ? true : false;
   String membertype = "";
%>

<%-- SECTION: COMPONENT STYLES --%>
<style type="text/css">
#member-listing thead td
{
  background-color: beige;
  cursor: default;
  font-size: small;
  font-variant: small-caps;
}
#member-listing thead td.dynamic, #member-listing thead td.thin
{
  width: 1.1em;
  text-align: center;
  font: 12pt monospace;
  font-weight: bold;
  font-variant: small-caps;
}
#member-listing thead td.dynamic:hover
{
  background-color: #ff8;
  cursor: pointer;
}
#member-listing tbody td
{
  padding: 0.1em;
}
#member-listing tbody tr:hover
{
  background-color: #ff8;
}
</style>


<table id="member-listing" border="1" class="columnar" width="100%" align="center">
<caption><%=caption%></caption>
<colgroup span="<%=colsadjust+6%>" align="center">
  <col width="15" align="center">
</colgroup>
<colgroup span="<%=colsadjust+2%>">
  <% if ("all".equals(memberstype) || ismethod || isfield ) { %>
  <col width="100" align="left">
  <% } %>
  <col width="100">
  <col width="300">
</colgroup>
<thead class="table_header">
<tr title="Click on one of the modifiers to filter the table by modifier type">
<td class="dynamic" onClick="filterRows('memberrow', 'reset', '<%=divid%>');" title="Reset table to include all rows">c l e a r</td>
<% if ( "all".equals(memberstype) || ismethod ) { %>
<td class="dynamic" onClick="filterRows('memberrow', 'abstract', '<%=divid%>');" title="Show only abstract members">a b s t r</td>
<% } %>
<td class="dynamic" onClick="filterRows('memberrow', 'public', '<%=divid%>');" title="Show only public members">p u b l i c</td>
<td class="dynamic" onClick="filterRows('memberrow', 'static', '<%=divid%>');" title="Show only static members">s t a t i c</tx>
<td class="dynamic" onClick="filterRows('memberrow', 'final', '<%=divid%>');" title="Show only final members">f i n a l</td>
<td class="thin" title="Version of API that this member was introduced">s i n c e</td>
<td class="dynamic" onclick="filterRows('memberrow', 'deprecated', '<%=divid%>');" title="Show only deprecated members">d e p r e c</td>
<% if ("all".equals(memberstype) || ismethod ) { %>
<td style="text-align: right;">Returns</td>
<% } else if (isfield) { %>
<td style="text-align: right;">Field Type</td>
<% } %>
<td><%=colname_heading%></td>
<td>Summary Description</td>
</tr>
</thead>

<tbody>
<% for (int i=0; i<members.size(); i++)
   {
     member = (Member) members.get(i);
     memberDescr = member.getSummaryDescription();
     if (member instanceof MethodMember)
     {
       method = (MethodMember) member;
       /*
       if (qualify)
       {
         returnstuff = method.getReturnTypeName();
       }
       else
       {
         if (member.getPackage() != null && member.getPackage().getName() != null)
           returnstuff = JDocUtil.conditionalQualify(method.getReturnTypeName(), member.getPackage().getName());
         else
           returnstuff = member.getName();
       }
       */
       returnstuff = JDocUtil.unqualify(method.getReturnTypeName());
       returnstuff += JDocUtil.getDimension(method.getReturnTypeDimension());
     }
     else if (member instanceof FieldMember)
     {
       field = (FieldMember) member;
     }
     membertype = member.getMemberTypeName();
%>
<tr id="memberrow<%=i%>" class="<%=member.getModifiers() + ((member.isDeprecated()) ? " deprecated" : " ")%>" style="display: ;">
  <td></td>
<% if (member instanceof MethodMember) { %>
  <td>
    <% if (method.isAbstract()) { %>
    <img src="images/check_sm.gif" border="0">
    <% } %>
  </td>
<% } else if ("all".equals(memberstype)) { %>
  <td>
   n/a
  </td>
<% } %>
  <td>
    <% if (member.getAccessibility() == JDocUtil.PUBLIC) { %>
    <img src="images/check_sm.gif" border="0">
    <% } %>
  </td>
  <td>
    <% if (member.isStatic()) { %>
    <img src="images/check_sm.gif" border="0">
    <% } %>
  </td>
  <td>
    <% if (member.isFinal()) { %>
    <img src="images/check_sm.gif" border="0">
    <% } %>
  </td>
  <td><%=member.getDoc().getCleanSince()%></td>
  <td>
    <% if (member.isDeprecated()) { %>
    <img src="images/check_sm.gif" border="0">
    <% } %>
  </td>
  <% if (member instanceof MethodMember) { %>
      <TD ALIGN="RIGHT">
      <% if (method.getReturnType()!=null && method.getReturnType().getId()>0) { %>
        <A HREF="cls.main.do?cls_id=<%=method.getReturnType().getId()%>"><%=returnstuff%></A>
      <% } else { %>
        <%=returnstuff%>
      <% } // end if linkable %>
      </td>
  <% } else if ("all".equals(memberstype)) { %>
      <TD ALIGN="RIGHT">
       n/a
      </td>
  <% } // end if ismethod %>
  <% if (member instanceof FieldMember && !"all".equals(memberstype)) { %>
      <TD ALIGN="RIGHT">
      <% if (field.getType()!=null && field.getType().getId()>0) { %>
        <A HREF="cls.main.do?cls_id=<%=field.getType().getId()%>"><%=field.getTypeName()%></A> 
      <% } else { %>
        <%=field.getTypeName()%> 
      <% } // end if linkable %>
      </td>
  <% } %>

  <td><A HREF="member.main.do?member_id=<%=member.getId()%>"><SPAN CLASS="<%=membertype%>" title="<%=HtmlUtils.cleanAttributeText(memberDescr)%>"><%=(qualify) ? member.getQualifiedName() : member.getName()%></SPAN></A></td>
  <td><%=memberDescr%></td>
</tr>
<% } // end for %>


</tbody>

</table>

 
<% } // end if members is empty %>



