<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
%>

<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<P><%= cls.getDoc().getSummaryDescription() %></P>

<%! List members; %>

<DIV ALIGN="CENTER">
<TABLE CLASS="columnar" CELLPADDING="5" BORDER="1">
<CAPTION><%=cls.getName()%> Members</CAPTION>
<!-- 
<THEAD CLASS="table_header">
<TR>
  <TH WIDTH="150">Fields</TH>
  <TH WIDTH="200">Constructors</TH>
  <TH WIDTH="250">Methods</TH>
</TR>
</THEAD>
 -->
<TBODY>
<TR>
  <%
     members = cls.getFields();
     String descr, mtype;
     if (!members.isEmpty())
     {
  %>
  <TD VALIGN="TOP">
   <DIV CLASS="scroll_column">
    <TABLE>
    <%
     FieldMember field;
     for (int i=0; i<members.size(); i++)
     {
        field = (FieldMember) members.get(i);
        descr = field.getDoc().getSummaryDescription();
        descr = HtmlUtils.cleanAttributeText(descr);
        mtype = field.getMemberTypeName();
        %>
    <TR>
    	<TD>
        <A HREF="index.html?cmd=member.main&member_id=<%=field.getId()%>" TITLE="<%=descr%>"><SPAN CLASS="<%=mtype%> <%=field.getModifiers()%> <%=field.isDeprecated() ? "deprecated" : ""%>"><%=field.getName()%></SPAN></A>
      </TD>
    </TR>
  <% } %>
    </TABLE>
   </DIV>
  </TD>
   <% }  // end if emtpy  %>
   
   <%
     members = cls.getConstructors();
     if (!members.isEmpty())
     {
   %>
  <TD VALIGN="TOP">
   <DIV CLASS="scroll_column">
    <TABLE>
    <%
     ConstructorMember constructor;
     for (int i=0; i<members.size(); i++)
     {
        constructor = (ConstructorMember) members.get(i);
        mtype = constructor.getMemberTypeName();
        descr = constructor.getDoc().getSummaryDescription();
        descr = HtmlUtils.cleanAttributeText(descr);
        %>
    <TR>
    	<TD>
        <A HREF="index.html?cmd=member.main&member_id=<%=constructor.getId()%>" TITLE="<%=descr%>"><SPAN CLASS="<%=mtype%>"><%=constructor.getName()%><%=constructor.getSignature()%></SPAN></A>
      </TD>
    </TR>
  <% } %>
    </TABLE>
   </DIV>
   
  </TD>

   <% }  // end if emtpy  %>

   <%
     members = cls.getMethods();
     if (!members.isEmpty())
     {
   %>
  <TD VALIGN="TOP">
   <DIV CLASS="scroll_column">
    <TABLE>
    <%
     MethodMember method;
     String returninfo;
     String returnClass;
     if (members.isEmpty())
       out.println("<TR><TD>none</TD></TR>");
     for (int i=0; i<members.size(); i++)
     {
        method = (MethodMember) members.get(i);
        mtype = method.getMemberTypeName();
        descr = method.getDoc().getSummaryDescription();
        descr = HtmlUtils.cleanAttributeText(descr);
        returninfo = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkg.getName()) +
                        JDocUtil.getDimension(method.getReturnTypeDimension());
        returnClass = (method.getReturnType()!=null && method.getReturnType().getId()>0) ? "ordinaryClass" : "";
          // note, could also be an interface.  so need to handle this (later)
        %>
    <TR>
    	<TD>
        <% if (!returnClass.equals("")) { // return type info %>
          <A HREF="index.html?cmd=cls.main&amp;id=<%=method.getReturnType().getId()%>"><SPAN CLASS="<%=returnClass%>"><%=returninfo%></SPAN></A>
        <% } else { %>
          <SPAN CLASS="<%=returnClass%>"><%=returninfo%></SPAN>
        <% } // end if %>
        <A HREF="index.html?cmd=member.main&member_id=<%=method.getId()%>" TITLE="<%=descr%>"><SPAN CLASS="<%=mtype%> <%=method.getModifiers()%> <%=(method.isDeprecated()) ? "deprecated" : ""%>"><%=method.getName()%><%=method.getSignature()%></SPAN></A>
      </TD>
    </TR>
  <% } %>
    </TABLE>
   </DIV>
  </TD>
   <% }  // end if emtpy  %>

</TR>
</TBODY>
</TABLE>
</DIV>

