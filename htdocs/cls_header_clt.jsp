<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  
  Map tabs = new HashMap(10);
  List cmds = new ArrayList(10);
  
  String main_caption = cls.getName();
  if (StringUtils.isBlank(main_caption) || (main_caption.length() > 20))
    main_caption = "At a Glance";

  tabs.put("cls_main", main_caption);
  cmds.add("cls_main");

  if (cls.hasInnerClasses())
  {
    tabs.put("cls_inner", "Inner Classes");
    cmds.add("cls_inner");
  }

  tabs.put("cls_member", "Members");
  cmds.add("cls_member");

  if (cls.hasFields())
  {
    tabs.put("cls_field", "Fields");
    cmds.add("cls_field");
  }

  if (cls.hasConstructors())
  {
    tabs.put("cls_constructor", "Constructors");
    cmds.add("cls_constructor");
  }

  if (cls.hasMethods())
  {
    tabs.put("cls_method", "Methods");
    cmds.add("cls_method");
  }

  if (!cls.isInterface())
  {
    tabs.put("cls_tree", "Tree");
    cmds.add("cls_tree");
  }

  tabs.put("cls_xref", "Cross References");
  cmds.add("cls_xref");

  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<jsp:include page="l2_hdr.jsp" flush="true">
  <jsp:param name="title_prefix" value="<%=cls.getName()%>"/>
</jsp:include>



