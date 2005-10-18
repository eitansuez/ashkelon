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

<a name="cls_xref">
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B>Cross-References</B></FONT></TD>
</TR>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.field.do?id=<%=cls.getId()%>#cls_xref">fields</A></B>
      </TD>
      <TD>
        Fields of type <%=name%>
      </TD>
    </TR>
  
    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.returnedby.do?id=<%=cls.getId()%>#cls_xref">returned by</A></B>
      </TD>
      <TD>
        Methods whose return type are <%=name%>
      </TD>
    </TR>
    
    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.passedto.do?id=<%=cls.getId()%>#cls_xref">passed to</A></B>
      </TD>
      <TD>
        Method or constructor parameters of type <%=name%>
      </TD>
    </TR>

  <%if (cls.getClassType()==ClassType.EXCEPTION_CLASS) { %>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.thrownby.do?id=<%=cls.getId()%>#cls_xref">thrown by</A></B>
      </TD>
      <TD>
        Methods or constructors that throw <%=name%>s
      </TD>
    </TR>

  <%} %>

  <%if (cls.getClassType()!=ClassType.INTERFACE) { %>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.subclasses.do?id=<%=cls.getId()%>#cls_xref">subclasses</A></B>
      </TD>
      <TD>
        Classes that extend <%=name%>
      </TD>
    </TR>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.descendents.do?id=<%=cls.getId()%>#cls_xref">descendents</A></B>
      </TD>
      <TD>
        All subclasses that extend <%=name%> (i.e. both direct & indicrect subclasses)
      </TD>
    </TR>

  <%} else {%>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.implementedby.do?id=<%=cls.getId()%>#cls_xref">implemented by</A></B>
      </TD>
      <TD>
        Classes that implement <%=name%>        
      </TD>
    </TR>

    <TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD ALIGN="RIGHT">
        <B><A HREF="cls.xref.extendedby.do?id=<%=cls.getId()%>#cls_xref">extended by</A></B>
      </TD>
      <TD>
        Interfaces extended by <%=name%>        
      </TD>
    </TR>

  <%} %>
</TABLE>

<BR><BR>

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


