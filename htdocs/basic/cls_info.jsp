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
  
  String cmd = ServletUtils.getRequestParam(request, "cmd");
  String descr_type = request.getParameter("descr_type");

  ClassType impl_interface;
  List authors = cls.getAuthors();
  Author author;
  String version = cls.getVersion();
  
  //String accessibility = JDocUtil.ACCESSIBILITIES[cls.getAccessibility()-1];
  String modifier = cls.getModifiers();
  
  String classname = cls.getName();
  
  String nice_typename = "Class";
  if (cls.getClassType() == ClassType.INTERFACE)
    nice_typename = "Interface";
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<H2>
<FONT SIZE="-1"><A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A></FONT>
<BR>
<%=nice_typename%> <%=cls.getName()%>
</H2>


<%
      TreeNode node = (TreeNode) request.getAttribute("tree");
      ClassType clsnode;
      String tree = "";
      String indent = "";
      boolean firstpass = true;
      boolean linkable, isself;

      while (node != null)
      {
        isself = false;
        linkable = true;
        
        clsnode = (ClassType) node.getValue();
        
        if (clsnode.getId() == cls.getId())
        {
          isself = true;
          linkable = false;
        }
        if (clsnode.getId() == 0)
          linkable = false;  

        tree += indent;
        if (!firstpass)
          tree += "|\n" + indent + "+--";
        if (isself)
          tree += "<B>";
        if (linkable)
          tree += "<A HREF=\"index.html?cmd=cls.main&cls_id="+clsnode.getId()+"\">";
        tree += clsnode.getQualifiedName();
        if (linkable)
          tree += "</A>";
        if (isself)
          tree += "</B>";
        tree += "\n";
        if (firstpass)
          indent += "  ";
        else
          indent += "      ";
          
        node = node.getOnlyChild();
        firstpass = false;
      }
%>


<P>
  <A HREF="index.html?cmd=cls.source&cls_name=<%=cls.getQualifiedName()%>">View Source</A>
</P>


<PRE><%=tree%></PRE>


<% if (cls.getInterfaces()!=null && cls.getInterfaces().size() > 0) { %>
<DL>
<DT><B>All Implemented Interfaces:</B>

<DD>
  <% for (int i=0; i<cls.getInterfaces().size(); i++)
     {
       impl_interface = (ClassType) cls.getInterfaces().get(i); %>
    <% if (impl_interface.getId()>0) { %>
    <A HREF="index.html?cmd=cls.main&cls_id=<%=impl_interface.getId()%>"><%=impl_interface.getName()%></A>
    <% } else { %>
    <%=impl_interface.getName()%>
    <% } %>
    <%=(i<cls.getInterfaces().size()-1) ? "," : ""%>
  <% } %>
</DD>
</DL>
<DL>
<% } %>

<!--
<DT><B>Direct Known Subclasses:</B> <DD><A HREF="../../javax/swing/JApplet.html">JApplet</A></DD>
</DL>
-->

<% if (cls.isInnerClass()) { %>
<DT><B>Enclosing class:</B> <DD><A HREF="index.html?cmd=cls.main&cls_id=<%=cls.getContainingClass().getId()%>"><%=JDocUtil.unqualify(cls.getContainingClassName())%></A></DD>
</DL>
<% } %>

<HR>

<%
  String src_clstype = "class";
  if (cls.getClassType() == ClassType.INTERFACE)
    src_clstype = "interface";
 %>
 <%=modifier%> <%=src_clstype%> <B><%=classname%></B>
      
      <% if (cls.getClassType() != ClassType.INTERFACE) { %>
        <% if (cls.getSuperClass() != null && cls.getSuperClass().getId()>0) { %>
	       extends <A HREF="index.html?cmd=cls.main&cls_id=<%=cls.getSuperClass().getId()%>"><%=cls.getSuperClassName()%></A>
        <% } else { %>
	       extends <%=cls.getSuperClassName()%>
        <% } %>
      <% } %>
  
    <% if (cls.getInterfaces()!=null && cls.getInterfaces().size() > 0) { %>
     implements 
      <% for (int i=0; i<cls.getInterfaces().size(); i++)
         {
           impl_interface = (ClassType) cls.getInterfaces().get(i); %>
        <% if (impl_interface.getId()>0) { %>
        <A HREF="index.html?cmd=cls.main&cls_id=<%=impl_interface.getId()%>"><%=impl_interface.getName()%></A>
        <% } else { %>
        <%=impl_interface.getName()%>
        <% } %>
        <%=(i<cls.getInterfaces().size()-1) ? "," : ""%>
      <% } %>
    <% } %>



    <P><%=cls.getDescription()%></P>
  
