<%@ page info="class tree component (superclasses/inheritance)" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

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
  
  TreeNode supers = (TreeNode) request.getAttribute("tree");
%>


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<P><%= cls.getDoc().getSummaryDescription() %></P>

<DIV STYLE="margin: 20px;">

<% if (supers != null)
   {
   %>

   <% if (supers.isEmpty()) { %>
   
   <P CLASS="message"><%=name%> has no superclasses.</P>
   
   <% } else {   // supers is not empty %>

   <H3>Inheritance Tree</H3>
   <TABLE CLASS="columnar" WIDTH="80%">
     <TR><TD>
      <%=org.ashkelon.pages.Page.printTree(supers, pkg.getName())%>
     </TD></TR>
   </TABLE>

   <% } // end if supers.isEmpty() %>
   
<%
   }
%>

</DIV>

