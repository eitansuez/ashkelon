<%@ page info="package tree" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  String name = pkg.getName();
  
  TreeNode tree = (TreeNode) request.getAttribute("tree");
%>  

<%-- SECTION: COMPONENT TEMPLATE --%> 
<p><%=pkg.getSummaryDescription()%></p>

<div style="margin: 0px;">
   
   <table class="columnar" width="80%" align="CENTER">
   
   <tr><td>

    <div class="scroll_table">
      <%=org.ashkelon.pages.Page.printTree(tree, name)%>
    </div>
    
   </td></tr>
   
   </table>
</div>
