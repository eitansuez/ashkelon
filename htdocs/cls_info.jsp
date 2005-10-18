<%@ page info="component" import="java.util.*,org.ashkelon.*" %>
<%@ taglib prefix="u2d" uri="http://u2d.com/taglib" %>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
  
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

<u2d:link to="package" elem="<%=pkg%>"><span class="package"><%=pkg.getName()%></span></u2d:link>

<span class="api" style="font-size: 8pt; padding-left: 1em;">
(<u2d:link to="api" elem="<%=cls.getAPI()%>"><%=cls.getAPI().getName()%></u2d:link> API)
</span>

<span style="padding-left: 5em; font-size: 8pt;">
   <u2d:link to="source" elem="<%=cls%>">Source Code</u2d:link>
</span>

</div>


<div style="border-bottom: 1px solid #808080; padding: 0.5em 0.5em 0 0; margin: 0.5em 0">

<%=modifier%> <span class="<%=cls_type%> <%=modifier%> <%=deprecated%>" style="font-size: large; font-weight: bold;"><%=classname%></span>

      <% if (cls.getClassType() != ClassType.INTERFACE) { %>
        <% if (cls.getSuperClass() != null && cls.getSuperClass().getId()>0) { %>
	       <span style="padding-left: 5em;">Extends <a href="cls.main.do?id=<%=cls.getSuperClass().getId()%>"><span class="superclass"><%=cls.getSuperClassName()%></span></a></span>
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
	         <a href="cls.main.do?id=<%=impl_interface.getId()%>"><span class="interface"><%=impl_interface.getName()%></span></a>
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
