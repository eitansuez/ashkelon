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

<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<P><%=pkg.getSummaryDescription()%></P>

<DIV STYLE="margin: 0px;">
   
   <TABLE CLASS="columnar" WIDTH="80%" ALIGN="CENTER">
   
   <TR><TD>

    <DIV CLASS="scroll_table">
      <%=org.ashkelon.pages.Page.printTree(tree, name)%>
    </DIV>
    
   </TD></TR>
   
   </TABLE>
</DIV>
