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

<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<P STYLE="font-size: larger;"><B>Which cross references might you be interested in?</B></P>

<UL>
  <LI><A HREF="cls.xref.field.do?cls_id=<%=cls.getId()%>">fields</A>: Fields of type <%=name%></LI>
  <LI><A HREF="cls.xref.returnedby.do?cls_id=<%=cls.getId()%>">returned by</A>: Methods whose return type are <%=name%></LI>
  <LI><A HREF="cls.xref.passedto.do?cls_id=<%=cls.getId()%>">passed to</A>: Method or constructor parameters of type <%=name%></LI>
  <%if (cls.getClassType()==ClassType.EXCEPTION_CLASS) { %>
  <LI><A HREF="cls.xref.thrownby.do?cls_id=<%=cls.getId()%>">thrown by</A>: Methods or constructors that throw <%=name%>s</LI>
  <%} %>
  <%if (cls.getClassType()!=ClassType.INTERFACE) { %>
  <LI><A HREF="cls.xref.subclasses.do?cls_id=<%=cls.getId()%>">subclasses</A>: Classes that extend <%=name%></LI>
  <LI><A HREF="cls.xref.descendents.do?cls_id=<%=cls.getId()%>">descendents</A>: All subclasses that extend <%=name%> (i.e. both direct & indicrect subclasses)</LI>
  <%} else {%>
  <LI><A HREF="cls.xref.implementedby.do?cls_id=<%=cls.getId()%>">implemented by</A>: Classes that implement <%=name%></LI>
  <LI><A HREF="cls.xref.extendedby.do?cls_id=<%=cls.getId()%>">extended by</A>: Interfaces extended by <%=name%></LI>
  <%} %>
</UL>


<% if (xrefs != null) { %>

<%
String emptyMsg = name + " has no " + keys[i-1] + " cross references";
if (i<=4) // displaying member info
{
  request.setAttribute("members", xrefs);
  String mmb_type = "all";
  if (keys[i-1].equals("field"))
    mmb_type = "field";
  if (keys[i-1].equals("returnedby"))
    mmb_type = "method";
%>
  <DIV ID="xref_member">
      <jsp:include page="member_list.jsp" flush="true">
        <jsp:param name="caption" value="<%=keys[i-1]%>" />
        <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
        <jsp:param name="display_qualified" value="true" />
        <jsp:param name="members_type" value="<%=mmb_type%>" />
        <jsp:param name="div_id" value="xref_member" />
      </jsp:include>
  </DIV>
<%
}
else
{
  request.setAttribute("classes", xrefs);
%>
  <DIV ID="xref_class">
      <jsp:include page="class_list.jsp" flush="true">
        <jsp:param name="caption" value="<%=keys[i-1]%>" />
        <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
        <jsp:param name="display_qualified" value="true" />
        <jsp:param name="classes_type" value="all" />
        <jsp:param name="div_id" value="xref_class" />
      </jsp:include>
  </DIV>
<% } %>

<% } // end if xrefs ! null %>


<%-- descendents --%>

<% if (desc != null)
   {
%>

  <% if (desc.isEmpty()) { %>
   <P CLASS="message"><%=name%> has no descendents.</P>
  <% } else {   // has descendents%>
   <TABLE CLASS="columnar" WIDTH="80%">
   <CAPTION>Descendents</CAPTION>
   <TBODY>
   <TR><TD>
   <DIV CLASS="scroll_table">
     <%=org.ashkelon.pages.Page.printTree(desc, pkg.getName())%>
  <% } %>
   </DIV>
   </TD></TR>
   </TBODY>
   </TABLE>

<% } // end if desc ! null %>


