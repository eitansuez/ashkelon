<%@ page info="package tree" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  String name = pkg.getName();
  TreeNode tree = (TreeNode) request.getAttribute("tree");
%>  

<jsp:include page="pkg_info.jsp" flush="true" />

<div class="columnar scroll_table" style="margin: 0.25em 3em; padding: 0;">
   <%=org.ashkelon.pages.Page.printTree(tree, name)%>
</div>

