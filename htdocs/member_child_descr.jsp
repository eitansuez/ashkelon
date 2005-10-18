<%@ page info="member child descriptions" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
ExecMember execmember = (ExecMember) request.getAttribute("execmember");
ClassType containingClass = execmember.getContainingClass();
String pkgName = containingClass.getPackage().getName();
%>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (execmember.getDoc().isDeprecated()) { %>
  <p><B>Deprecated:</B> <%=execmember.getDoc().getDeprecated()%></p>
<% } %>

<%=execmember.getDescription()%>

<%-- RETURN TYPE INFO --%>
<%
 if (execmember instanceof MethodMember)
 {
   MethodMember method = (MethodMember) execmember;
   String name = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkgName);
   String dim = JDocUtil.getDimension(method.getReturnTypeDimension());
   String descr = StringUtils.avoidNull(method.getReturnDescription());
   
   if (method.getReturnType() != null && method.getReturnType().getId() > 9)
   {
%>
  <p>
  <b>Returns</b> <a href="cls.main.do?id=<%=method.getReturnType().getId()%>"><%=name%></a><%=dim%>: <%=descr%>
  </p>
<%
   } else {
 %>   
  <p>
    <b>Returns</b> <%=name%><%=dim%>:  <%=descr%>
  </p>
<%
   }
   
 }  // end main if statement
 %>

 
 
<%-- PARAMETER INFO --%>
<%
   if (!execmember.getParameters().isEmpty())
   { %>
    <b>Parameters:</b>
    <ul>
    
    <%
       ParameterInfo param;
       String dimStr = "";
       String typeName = "";
       for (int i=0; i<execmember.getParameters().size(); i++)
       {
        param = (ParameterInfo) execmember.getParameters().get(i);
        dimStr = JDocUtil.getDimension(param.getTypeDimension());
        typeName = JDocUtil.conditionalQualify(param.getTypeName(), pkgName);
    
        %>
          
         <%
            if (param.getType()!=null && param.getType().getId()>9)
            {
         %>
                <li><a href="cls.main.do?id=<%=param.getType().getId()%>"><B><%=typeName%></B></A><%=dimStr%> <%=param.getName()%>: <%=param.getDescription()%></li>
         <%
            } else 
            {
         %>
                <li><b><%=typeName%><%=dimStr%> <%=param.getName()%></b>: <%=param.getDescription()%></li>
          <% } %>
          
    <% }  // end for loop %>
    
    </ul>
<%
   }  // end main if statement
 %>

 
<%-- THROWN EXCEPTION --%>
<%
 if (!execmember.getThrownExceptions().isEmpty())
 { %>
<b>Throws:</b>
<ul>
<%
 ThrownException ex;
 for (int i=0; i<execmember.getThrownExceptions().size(); i++)
 {
  ex = (ThrownException) execmember.getThrownExceptions().get(i);
  %>

  <%
    if (ex.getException()!=null && ex.getException().getId()>9)
    {
    %>
    <li><a href="cls.main.do?id=<%=ex.getException().getId()%>"><b><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></b></a>: <%=ex.getDescription()%></li>
    <%
    } else
    {
    %>
    <li><b><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></b>: <%=ex.getDescription()%></li>
    <%
    }
    %>

<% } %>
</ul>
<%
 } %>
 
<jsp:include page="doc_footer.jsp" flush="true"/>
 
