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

<%-- SECTION: COMPONENT TEMPLATE --%> 

<p><%= cls.getDoc().getSummaryDescription() %></p>

<div style="margin: 20px;">

<% if (supers != null)
   {
   %>

   <% if (supers.isEmpty()) { %>
   
   <p class="message"><%=name%> has no superclasses.</p>
   
   <% } else {   // supers is not empty %>

   <h3>Inheritance Tree</h3>
   <table class="columnar" width="80%">
     <tr><td>
      <%=org.ashkelon.pages.Page.printTree(supers, pkg.getName())%>
     </td></tr>
   </table>

   <% } // end if supers.isEmpty() %>
   
<%
   }
%>

</div>

