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


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 


<% if (members.isEmpty()) { %>
  <P CLASS="message"><%=emptyMsg%></P>
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


<TABLE BORDER="1" CLASS="columnar" WIDTH="90%">
<CAPTION><%=caption%></CAPTION>
  <COLGROUP SPAN="<%=colsadjust+6%>" WIDTH="15" ALIGN="CENTER">
  </COLGROUP>
  <COLGROUP SPAN="<%=colsadjust+2%>">
    <% if ("all".equals(memberstype) || ismethod || isfield ) { %>
    <COL WIDTH="100" ALIGN="LEFT">
    <% } %>
    <COL WIDTH="100">
    <COL WIDTH="300">
  </COLGROUP>
<THEAD CLASS="table_header">
<TR TITLE="Click on one of the modifiers to filter the table by modifier type">
<TD STYLE="font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onClick="filterRows('memberrow', 'reset', '<%=divid%>');" TITLE="Reset table to include all rows">R<BR>e<BR>s<BR>e<BR>t</TD>
<% if ( "all".equals(memberstype) || ismethod ) { %>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onClick="filterRows('memberrow', 'abstract', '<%=divid%>');" TITLE="Show only abstract members">a<BR>b<BR>s<BR>t<BR>r</TD>
<% } %>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('memberrow', 'public', '<%=divid%>');" TITLE="Show only public members">p<BR>u<BR>b<BR>l<BR>i<BR>c</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('memberrow', 'static', '<%=divid%>');" TITLE="Show only static members">s<BR>t<BR>a<BR>t<BR>i<BR>c</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('memberrow', 'final', '<%=divid%>');" TITLE="Show only final members">f<BR>i<BR>n<BR>a<BR>l</TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;" TITLE="Version of API that this member was introduced">s<BR>i<BR>n<BR>c<BR>e</TD>
<TD STYLE="cursor: pointer; font-size: x-small; font-variant:small-caps;" onmouseover="this.bgColor='#ffff00';" onmouseout="this.bgColor='beige';" onclick="filterRows('memberrow', 'deprecated', '<%=divid%>');" TITLE="Show only deprecated members">d<BR>e<BR>p<BR>r<BR>e<BR>c</TD>
<% if ("all".equals(memberstype) || ismethod ) { %>
<TD STYLE="font-size: x-small; font-variant:small-caps; text-align: right;">Returns</TD>
<% } else if (isfield) { %>
<TD STYLE="font-size: x-small; font-variant:small-caps; text-align: right;">Field Type</TD>
<% } %>
<TD STYLE="font-size: x-small; font-variant:small-caps;"><%=colname_heading%></TD>
<TD STYLE="font-size: x-small; font-variant:small-caps;">Summary Description</TD>
</TR>
</THEAD>

<TBODY>
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
<TR ID="memberrow<%=i%>" CLASS="<%=member.getModifiers() + ((member.isDeprecated()) ? " deprecated" : " ")%>" STYLE="display: ;">
  <TD>&nbsp;</TD>
<% if (member instanceof MethodMember) { %>
  <TD>
    <% if (method.isAbstract()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } %>
  </TD>
<% } else if ("all".equals(memberstype)) { %>
  <TD>
   n/a
  </TD>
<% } %>
  <TD>
    <% if (member.getAccessibility() == JDocUtil.PUBLIC) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } %>
  </TD>
  <TD>
    <% if (member.isStatic()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } %>
  </TD>
  <TD>
    <% if (member.isFinal()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } %>
  </TD>
  <TD><%=member.getDoc().getCleanSince()%></TD>
  <TD>
    <% if (member.isDeprecated()) { %>
    <IMG SRC="images/check_sm.gif" WIDTH="15" HEIGHT="14" ALT="" BORDER="0">
    <% } %>
  </TD>
  <% if (member instanceof MethodMember) { %>
      <TD ALIGN="RIGHT">
      <% if (method.getReturnType()!=null && method.getReturnType().getId()>0) { %>
        <A HREF="cls.main.do?cls_id=<%=method.getReturnType().getId()%>"><%=returnstuff%></A>
      <% } else { %>
        <%=returnstuff%>
      <% } // end if linkable %>
      </TD>
  <% } else if ("all".equals(memberstype)) { %>
      <TD ALIGN="RIGHT">
       n/a
      </TD>
  <% } // end if ismethod %>
  <% if (member instanceof FieldMember && !"all".equals(memberstype)) { %>
      <TD ALIGN="RIGHT">
      <% if (field.getType()!=null && field.getType().getId()>0) { %>
        <A HREF="cls.main.do?cls_id=<%=field.getType().getId()%>"><%=field.getTypeName()%></A> 
      <% } else { %>
        <%=field.getTypeName()%> 
      <% } // end if linkable %>
      </TD>
  <% } %>

  <TD><A HREF="member.main.do?member_id=<%=member.getId()%>"><SPAN CLASS="<%=membertype%>" TITLE="<%=HtmlUtils.cleanAttributeText(memberDescr)%>"><%=(qualify) ? member.getQualifiedName() : member.getName()%></SPAN></A></TD>
  <TD><%=memberDescr%></TD>
</TR>
<% } // end for %>


</TBODY>

</TABLE>

 
<% } // end if members is empty %>



