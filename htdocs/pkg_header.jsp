<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");

  Map tabs = new HashMap(10);
  List cmds = new ArrayList(10);
  
  tabs.put("pkg_main", pkg.getName());
  cmds.add("pkg_main");
  
  tabs.put("pkg_member", "Members");
  cmds.add("pkg_member");

  if (pkg.hasOrdinaryClasses())
  {
    tabs.put("pkg_ordinaryClass", "Classes");
    cmds.add("pkg_ordinaryClass");
  }
  if (pkg.hasInterfaces())
  {
    tabs.put("pkg_interface", "Interfaces");
    cmds.add("pkg_interface");
  }
  if (pkg.hasExceptionClasses())
  {
    tabs.put("pkg_exception", "Exceptions");
    cmds.add("pkg_exception");
  }
  if (pkg.hasErrorClasses())
  {
    tabs.put("pkg_errorClass", "Errors");
    cmds.add("pkg_errorClass");
  }
  
  tabs.put("pkg_tree", "Tree");
  cmds.add("pkg_tree");
  
  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
  
%>

<jsp:include page="l2_hdr.jsp" flush="true">
  <jsp:param name="title_prefix" value="<%=pkg.getName()%>"/>
</jsp:include>


