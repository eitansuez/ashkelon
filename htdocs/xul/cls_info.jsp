<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
  
  String cmd = ServletUtils.getCommand(request);
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

<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<TABLE WIDTH="90%" STYLE="border-bottom: thin solid #808080;"><TR><TD ALIGN="LEFT">
<% if (authors != null && !authors.isEmpty()) { %>
Author<%=(authors.size()==1)?"":"s"%>: 
  <% for (int i=0; i<authors.size(); i++)
     {
       author = (Author) authors.get(i);
  %>
    <A HREF="index.html?cmd=author&id=<%=author.getId()%>"><%=author.getName()%></A> <%=(i<authors.size()-1) ? "," : ""%>
  <% } %>
<% } %>
</TD><TD ALIGN="RIGHT">
<% if (version != null) { %>
Version: <%=version%>
<% } %>
</TD></TR></TABLE>

<BR>

<TABLE BORDER="0">
	<TR VALIGN="BOTTOM">
     <TD COLSPAN="3"><A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><SPAN CLASS="package"><%=pkg.getName()%></SPAN></A>
<% if (pkg.getAPI() != null) { %>
&nbsp;&nbsp;&nbsp;
<SPAN CLASS="api" STYLE="font-size: 8 pt;">
(<a href="index.html?cmd=api.main&id=<%=cls.getAPI().getId()%>"><%=cls.getAPI().getName()%></a> API)
</SPAN>
<% } %>
     </TD>
      <TD WIDTH="20"></TD>
     <TD ALIGN="RIGHT">
       <A HREF="index.html?cmd=cls.source&amp;cls_name=<%=cls.getQualifiedName()%>" STYLE="font-size: 8 pt;">View Source</A>
     </TD>
  </TR>
	<TR VALIGN="BOTTOM">
      <TD><%=modifier%> <SPAN CLASS="<%=cls_type%> <%=modifier%> <%=deprecated%>" STYLE="font-size: large; font-weight: bold;"><%=classname%></SPAN></TD>
      <TD WIDTH="20"></TD>
      <% if (cls.getClassType() != ClassType.INTERFACE) { %>
        <% if (cls.getSuperClass() != null && cls.getSuperClass().getId()>0) { %>
	       <TD>Extends <A HREF="index.html?cmd=cls.main&amp;cls_id=<%=cls.getSuperClass().getId()%>"><SPAN CLASS="superclass"><%=cls.getSuperClassName()%></SPAN></A></TD>
        <% } else { %>
	       <TD>Extends <SPAN CLASS="superclass"><%=cls.getSuperClassName()%></SPAN></TD>
        <% } %>
      <% } else { %>
	    <TD></TD>
      <% } %>

      <%if (cls.getInterfaces()!=null && cls.getInterfaces().size() > 0) { %>
        <TD WIDTH="20"></TD>
        <TD>Implements 
        <% for (int i=0; i<cls.getInterfaces().size(); i++)
           {
             impl_interface = (ClassType) cls.getInterfaces().get(i);
        %>
          <% if (impl_interface.getId()>0) { %>
	         <A HREF="index.html?cmd=cls.main&amp;cls_id=<%=impl_interface.getId()%>"><SPAN CLASS="interface"><%=impl_interface.getName()%></SPAN></A>
          <% } else { %>
	         <SPAN CLASS="interface"><%=impl_interface.getName()%></SPAN>
          <% } %>
          <%=(i<cls.getInterfaces().size()-1) ? "," : ""%>
          
        <% } %>
        </TD>
      <% } else { %>
	    <TD></TD>
      <% } %>

	</TR>
</TABLE>

<BR>

