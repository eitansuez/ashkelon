<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  API api = (API) request.getAttribute("api");

  Map tabs = new HashMap(10);
  List cmds = new ArrayList(10);
  
  tabs.put("api_main", api.getName());
  cmds.add("api_main");
  
  tabs.put("api_member", "Packages");
  cmds.add("api_member");

  request.setAttribute("tabs", tabs);
  request.setAttribute("cmds", cmds);
 %>

<jsp:include page="l2_hdr.jsp" flush="true">
  <jsp:param name="title_prefix" value="<%=api.getName()%>" />
</jsp:include>


