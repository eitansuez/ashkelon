<%@ page info="field view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  FieldMember field = (FieldMember) request.getAttribute("member");
  ClassType containingClass = field.getContainingClass();
  String pkgName = containingClass.getPackage().getName();
  //String caption = field.getModifiers() + " " + field.getName();
  String caption = field.getName();
  
  String typeName = JDocUtil.conditionalQualify(field.getTypeName(), pkgName);
  String typeDim = JDocUtil.getDimension(field.getTypeDimension());
  String membertype = "field";
%>


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 
<P>
<% if (field.getType()!=null && field.getType().getId()>0)
  {
  %>

   <A HREF="index.html?cmd=cls.main&cls_id=<%=field.getType().getId()%>"><%=typeName%></A><%=typeDim%> <SPAN CLASS="<%=membertype%>"><%=field.getName()%></SPAN>
<%}
  else
  { %>
   <%=typeName%><%=typeDim%> <SPAN CLASS="<%=membertype%>"><%=field.getName()%></SPAN>
<%
  }
%>
</P>

<P>
<%=field.getDescription()%>
</P>
