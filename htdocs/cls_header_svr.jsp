<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String unselectedColor = "#CDCDCD";
  String selectedColor = "#92A6F5";
  
  ClassType cls = (ClassType) request.getAttribute("cls");
  
  Map tabs = new HashMap();
  List cmds = new ArrayList(10);
  
  String main_caption = cls.getName();
  if (StringUtils.isBlank(main_caption) || (main_caption.length() > 20))
    main_caption = "At a Glance";

    tabs.put("cls.main", main_caption);
    cmds.add("cls.main");

  if (cls.hasInnerClasses())
  {
    tabs.put("cls.inner", "Inner Classes");
    cmds.add("cls.inner");
  }
  
    tabs.put("cls.member", "Members");
    cmds.add("cls.member");

  if (!cls.getFields().isEmpty())
  {
    tabs.put("cls.field", "Fields");
    cmds.add("cls.field");
  }
  
  if (!cls.getConstructors().isEmpty())
  {
    tabs.put("cls.constructor", "Constructors");
    cmds.add("cls.constructor");
  }
  
  if (!cls.getMethods().isEmpty())
  {
    tabs.put("cls.method", "Methods");
    cmds.add("cls.method");
  }
  
  if (cls.getClassType() != ClassType.INTERFACE)
  {
    tabs.put("cls.tree", "Tree");
    cmds.add("cls.tree");
  }
  
  tabs.put("cls.xref", "Cross References");
  cmds.add("cls.xref");
  
  String cmd = ServletUtils.getCommand(request);
  String cls_id = ServletUtils.getRequestParam(request, "cls_id");
  
  String[] cmd_hierarchy = StringUtils.split(cmd,".");
  String area = cmd_hierarchy[0] + "." + cmd_hierarchy[1];
  
  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 
<jsp:include page="l2_hdr_svr.jsp" flush="true">
  <jsp:param name="args" value="<%=\"cls_id=\"+cls_id%>"/>
</jsp:include>
<br/>

