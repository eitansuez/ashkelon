<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
%>

<style>
#cls-members-table th
{
  font-weight: normal;
  font-style: italic;
  padding: 0.3em;
  border: 1px solid #937353;
}
#cls-members-table td
{
  border: 1px solid #937353;
  padding: 0;
  vertical-align: top;
}
#cls-members-table ol
{
  margin: 0;
  padding: 0;
  list-style-type: none;
}
#cls-members-table li
{
  padding: 0.2em;
  margin: 0;
  border-bottom: 1px dotted #808080;
  margin-right: 8px;
}
</style>

<p>
  <%= cls.getDoc().getSummaryDescription() %>
</p>

<%! List members; %>

<div align="center">
<table id="cls-members-table" cellspacing="0" class="columnar">
<caption><%=cls.getName()%> Members</caption>
<thead class="table_header">
<tr>
  <% if (!cls.getFields().isEmpty()) { %>
  <th width="150">Fields</th>
  <% } %>
  <% if (!cls.getConstructors().isEmpty()) { %>
  <th width="200">Constructors</th>
  <% } %>
  <% if (!cls.getMethods().isEmpty()) { %>
  <th width="250">Methods</th>
  <% } %>
</tr>
</thead>
<tbody>
<tr>
  <%
     members = cls.getFields();
     String descr, mtype;
     if (!members.isEmpty())
     {
  %>
  <td>
    <ol class="scroll_column">
    <%
     FieldMember field;
     for (int i=0; i<members.size(); i++)
     {
        field = (FieldMember) members.get(i);
        descr = field.getDoc().getSummaryDescription();
        descr = HtmlUtils.cleanAttributeText(descr);
        mtype = field.getMemberTypeName();
        %>
    	<li>
        <a href="member.main.do?member_id=<%=field.getId()%>" title="<%=descr%>"><span class="<%=mtype%> <%=field.getModifiers()%> <%=field.isDeprecated() ? "deprecated" : ""%>"><%=field.getName()%></span></a>
      </li>
  <% } %>
    </ol>
  </td>
   <% }  // end if emtpy  %>
   
   <%
     members = cls.getConstructors();
     if (!members.isEmpty())
     {
   %>
  <td>
    <ol class="scroll_column">
    <%
     ConstructorMember constructor;
     for (int i=0; i<members.size(); i++)
     {
        constructor = (ConstructorMember) members.get(i);
        mtype = constructor.getMemberTypeName();
        descr = constructor.getDoc().getSummaryDescription();
        descr = HtmlUtils.cleanAttributeText(descr);
        %>
    	<li>
        <a href="member.main.do?member_id=<%=constructor.getId()%>" title="<%=descr%>"><span class="<%=mtype%>"><%=constructor.getName()%><%=constructor.getSignature()%></span></a>
      </li>
  <% } %>
    </ol>
   
  </td>

   <% }  // end if emtpy  %>

   <%
     members = cls.getMethods();
     if (!members.isEmpty())
     {
   %>
  <td>
    <ol class="scroll_column">
    <%
     MethodMember method;
     String returninfo;
     String returnClass;
     if (members.isEmpty())
       out.println("<tr><td>none</td></tr>");
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
    	<li>
        <% if (!returnClass.equals("")) { // return type info %>
          <a href="cls.main.do?cls_id=<%=method.getReturnType().getId()%>"><span class="<%=returnClass%>"><%=returninfo%></span></a>
        <% } else { %>
          <span class="<%=returnClass%>"><%=returninfo%></span>
        <% } // end if %>
        <a href="member.main.do?member_id=<%=method.getId()%>" title="<%=descr%>"><span class="<%=mtype%> <%=method.getModifiers()%> <%=(method.isDeprecated()) ? "deprecated" : ""%>"><%=method.getName()%><%=method.getSignature()%></span></a>
      </li>
  <% } %>
    </ol>
  </td>
   <% }  // end if emtpy  %>

</tr>
</tbody>
</table>
</div>


