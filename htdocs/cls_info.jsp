<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
  
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  String descr_type = request.getParameter("descr_type");

  ClassType impl_interface;
  List authors = cls.getAuthors();
  Author author;
  String version = cls.getVersion();
  
  //String accessibility = JDocUtil.ACCESSIBILITIES[cls.getAccessibility()-1];
  String modifier = cls.getModifiers();
  
  String classname = cls.getName();
  String deprecated = cls.isDeprecated() ? "deprecated" : "";
%>

<div>

<a href="pkg.main.do?pkg_id=<%=pkg.getId()%>"><span class="package"><%=pkg.getName()%></span></a>

<span class="api" style="font-size: 8pt; padding-left: 1em;">
(<a href="api.main.do?id=<%=cls.getAPI().getId()%>"><%=cls.getAPI().getName()%></a> API)
</span>

<span style="padding-left: 5em;"><a href="cls.source.do?cls_name=<%=cls.getQualifiedName()%>" style="font-size: 8pt;">Source Code</a></span>

</div>


<div style="border-bottom: 1px solid #808080; padding: 0.5em 0.5em 0 0; margin: 0.5em 0">

<%=modifier%> <span class="<%=cls_type%> <%=modifier%> <%=deprecated%>" style="font-size: large; font-weight: bold;"><%=classname%></span>

      <% if (cls.getClassType() != ClassType.INTERFACE) { %>
        <% if (cls.getSuperClass() != null && cls.getSuperClass().getId()>0) { %>
	       <span style="padding-left: 5em;">Extends <a href="cls.main.do?cls_id=<%=cls.getSuperClass().getId()%>"><span class="superclass"><%=cls.getSuperClassName()%></span></a></span>
        <% } else { %>
	       <span style="padding-left: 5em;">Extends <span class="superclass"><%=cls.getSuperClassName()%></span></span>
        <% } %>
      <% } %>

      <%if (cls.getInterfaces()!=null && cls.getInterfaces().size() > 0) { %>
        <span style="padding-left: 5em;">Implements 
        <% for (int i=0; i<cls.getInterfaces().size(); i++)
           {
             impl_interface = (ClassType) cls.getInterfaces().get(i);
        %>
          <% if (impl_interface.getId()>0) { %>
	         <a href="cls.main.do?cls_id=<%=impl_interface.getId()%>"><span class="interface"><%=impl_interface.getName()%></span></a>
          <% } else { %>
	         <span class="interface"><%=impl_interface.getName()%></span>
          <% } %>
          <%=(i<cls.getInterfaces().size()-1) ? "," : ""%>
          
        <% } %>
        </span>
      <% } %>

</div>

<div style="margin-bottom: 3em; clear: both;">

<% if (authors != null && !authors.isEmpty()) { %>
  <span style="float: left;">
  <% for (int i=0; i<authors.size(); i++)
     {
       author = (Author) authors.get(i);
  %>
    <a href="author.do?id=<%=author.getId()%>" class="author"><%=author.getName()%></a> <%=(i<authors.size()-1) ? "," : ""%>
  <% } %>
  </span>
<% } %>

<% if (version != null) { %>
<span style="float: right;">Version: <%=version%></span>
<% } %>

</div>


