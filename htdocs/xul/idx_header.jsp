<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

outstanding tasks:
  parametrize: colors, their links (associated commands)
--%>

<%-- SECTION: PAGE CODE --%>
<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#92A6F5";
  
  Map tabs = new HashMap();
  String main_caption = ServletUtils.getRequestParam(request, "main_caption");
  if (StringUtils.isBlank(main_caption)) main_caption = "At a Glance";
  tabs.put("idx.package", "Packages");
  tabs.put("idx.class", "Classes");
  tabs.put("idx.member", "Members");
  tabs.put("idx.author", "Authors");
  
  //String[] cmds = {"idx.package", "idx.class", "idx.member", "idx.author"};
  List cmds = new ArrayList(10);
  cmds.add("idx.package");
  cmds.add("idx.class");
  cmds.add("idx.member");
  cmds.add("idx.author");
  String cmd = ServletUtils.getCommand(request);

  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<jsp:include page="l2_hdr_svr.jsp" flush="true">
  <jsp:param name="args" value="start=A" />
</jsp:include>

<BR>

