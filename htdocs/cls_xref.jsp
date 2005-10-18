<%@ page info="class cross references component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  int clstype = cls.getClassType();
  String clstypename = cls.getClassTypeName();
  String name = cls.getName();
  
  List xrefs = null;
  int i=0;
  String keys[] = {"field", "returnedby", "passedto", "thrownby", "implementedby", "subclasses", "extendedby"};
  while (xrefs==null && i<keys.length)
  {
      xrefs = (List) request.getAttribute(keys[i++]);
  }
  
  TreeNode desc = (TreeNode) request.getAttribute("descendents");
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<p style="font-size: larger; font-weight: bold;">Which cross references might you be interested in?</P>

<ul>
  <li><a href="cls.xref.field.do?id=<%=cls.getId()%>">fields</A>: Fields of type <%=name%></LI>
  <LI><a href="cls.xref.returnedby.do?id=<%=cls.getId()%>">returned by</A>: Methods whose return type are <%=name%></LI>
  <LI><a href="cls.xref.passedto.do?id=<%=cls.getId()%>">passed to</A>: Method or constructor parameters of type <%=name%></LI>
  <%if (cls.getClassType()==ClassType.EXCEPTION_CLASS) { %>
  <LI><a href="cls.xref.thrownby.do?id=<%=cls.getId()%>">thrown by</A>: Methods or constructors that throw <%=name%>s</LI>
  <%} %>
  <%if (cls.getClassType()!=ClassType.INTERFACE) { %>
  <LI><a href="cls.xref.subclasses.do?id=<%=cls.getId()%>">subclasses</A>: Classes that extend <%=name%></LI>
  <LI><a href="cls.xref.descendents.do?id=<%=cls.getId()%>">descendents</A>: All subclasses that extend <%=name%> (i.e. both direct & indicrect subclasses)</LI>
  <%} else {%>
  <LI><a href="cls.xref.implementedby.do?id=<%=cls.getId()%>">implemented by</A>: Classes that implement <%=name%></LI>
  <LI><a href="cls.xref.extendedby.do?id=<%=cls.getId()%>">extended by</A>: Interfaces extended by <%=name%></LI>
  <%} %>
</ul>


<% if (xrefs != null) { %>

<p>Total Results: <%= request.getAttribute("total-results") %></p>

<%
String emptyMsg = name + " has no " + keys[i-1] + " cross references";
if (i<=4) // displaying member info
{
  request.setAttribute("members", xrefs);
  String mmb_type = "all";
  if (keys[i-1].equals("field"))
    mmb_type = "field";
  else if (keys[i-1].equals("returnedby"))
    mmb_type = "method";
%>
  <div id="xref_member">
      <jsp:include page="member_list.jsp" flush="true">
        <jsp:param name="caption" value="<%=keys[i-1]%>" />
        <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
        <jsp:param name="display_qualified" value="true" />
        <jsp:param name="members_type" value="<%=mmb_type%>" />
        <jsp:param name="div_id" value="xref_member" />
      </jsp:include>
  </div>
<%
}
else
{
  request.setAttribute("classes", xrefs);
%>
  <div id="xref_class">
      <jsp:include page="class_list.jsp" flush="true">
        <jsp:param name="caption" value="<%=keys[i-1]%>" />
        <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
        <jsp:param name="display_qualified" value="true" />
        <jsp:param name="classes_type" value="all" />
        <jsp:param name="div_id" value="xref_class" />
      </jsp:include>
  </div>
<% } %>


<!-- paging -->
<table width="100%"><tr>
<% if (request.getAttribute("prev-cursor-position") != null) { %>
<td align="left">
  <form method="get" action="cls.xref.<%=keys[i-1]%>.do">
   <input type="hidden" name="id" value="<%=cls.getId()%>" />
   <input type="hidden" name="cursor-position" value="<%= request.getAttribute("prev-cursor-position") %>" />
   <button type="submit">Previous</button>
  </form>
</td>
<% } %>
<% if (request.getAttribute("next-cursor-position") != null) { %>
<td align="right">
  <form method="get" action="cls.xref.<%=keys[i-1]%>.do">
   <input type="hidden" name="id" value="<%=cls.getId()%>" />
   <input type="hidden" name="cursor-position" value="<%= request.getAttribute("next-cursor-position") %>" />
   <button type="submit">Next</button>
  </form>
</td>
<% } %>
</tr></table>


<% } // end if xrefs ! null %>


<%-- descendents --%>

<% if (desc != null)
   {
%>

  <% if (desc.isEmpty()) { %>
   <p class="message"><%=name%> has no descendents.</p>
  <% } else {   // has descendents%>
   <table class="columnar" width="80%">
   <caption>Descendents</caption>
   <tbody>
   <tr><td>
   <div class="scroll_table">
     <%=org.ashkelon.pages.Page.printTree(desc, pkg.getName())%>
  <% } %>
   </div>
   </td></tr>
   </tbody>
   </table>

<% } // end if desc ! null %>


