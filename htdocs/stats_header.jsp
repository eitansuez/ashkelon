<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#92A6F5";
  
  Map tabs = new HashMap();
  String main_caption = ServletUtils.getRequestParam(request, "main_caption");
  if (StringUtils.isBlank(main_caption)) main_caption = "At a Glance";
  tabs.put("stats.general", "General");
  tabs.put("stats.classes", "Classes");
  //tabs.put("stats.members", "Members");
  tabs.put("stats.authors", "Authors");
  
  List cmds = new ArrayList(10);
  cmds.add("stats.general");
  cmds.add("stats.classes");
  cmds.add("stats.authors");
  
  String cmd = ServletUtils.getCommand(request);

  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
%>

<jsp:include page="l2_hdr_svr.jsp" flush="true" />

<BR>
