<%@ page info="field view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

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

<!-- <A NAME="<%=field.getName()%>"> -->
<A NAME="<%=field.getId()%>">
<H3><%=field.getName()%></H3>

<TT>
<% if (field.getType()!=null && field.getType().getId()>0)
  {
  %>
   <%=field.getModifiers()%> <A HREF="cls.main.do?cls_id=<%=field.getType().getId()%>"><%=typeName%></A><%=typeDim%> <%=field.getName()%>
<%}
  else
  { %>
   <%=field.getModifiers()%> <%=typeName%><%=typeDim%> <%=field.getName()%>
<%
  }
%>
</TT>

<DL><DD>
  <%=field.getDescription()%>

  <jsp:include page="doc_footer.jsp" flush="true"/>
</DD></DL>


