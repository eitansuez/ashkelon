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


<%-- SECTION: COMPONENT TEMPLATE --%> 
<p>
<% if (field.getType()!=null && field.getType().getId()>0)
  {
  %>

   <a href="cls.main.do?id=<%=field.getType().getId()%>"><%=typeName%></a><%=typeDim%> <span class="<%=membertype%>"><%=field.getName()%></span>
<%}
  else
  { %>
   <%=typeName%><%=typeDim%> <span class="<%=membertype%>"><%=field.getName()%></span>
<%
  }
%>
</p>

<p>
<%=field.getDescription()%>
</p>

