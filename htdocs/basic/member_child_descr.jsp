<%@ page info="member child descriptions" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
ExecMember execmember = (ExecMember) request.getAttribute("execmember");
ClassType containingClass = execmember.getContainingClass();
String pkgName = containingClass.getPackage().getName();
%>


<% if (execmember.getDoc().isDeprecated()) { %>
  <P><B>Deprecated:</B> <%=execmember.getDoc().getDeprecated()%></P>
<% } %>

<P>
<%=execmember.getDescription()%>
</P>

<%-- RETURN TYPE INFO --%>
<%
 if (execmember instanceof MethodMember)
 {
   MethodMember method = (MethodMember) execmember;
   String name = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkgName);
   String dim = JDocUtil.getDimension(method.getReturnTypeDimension());
   String descr = StringUtils.avoidNull(method.getReturnDescription());

   if (!("void".equals(method.getReturnTypeName())))
   {
     if (method.getReturnType() != null && method.getReturnType().getId() > 9)
     {
%>
  <P>
  <B>Returns</B> <A HREF="cls.main.do?cls_id=<%=method.getReturnType().getId()%>"><%=name%></A><%=dim%>: <%=descr%>
  </P>
<%
     } else {
 %>   
  <P>
    <B>Returns</B> <%=name%><%=dim%>:  <%=descr%>
  </P>
<%
     }
   }
   
 }  // end main if statement
 %>

 
 
<%-- PARAMETER INFO --%>
<%
   if (!execmember.getParameters().isEmpty())
   { %>
    <DL>
    <DT>
    <B>Parameters:</B>
    </DT>
    
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
          
        <DD>
         <%
            if (param.getType()!=null && param.getType().getId()>9)
            {
         %>
                <CODE><%=param.getName()%></CODE> - <%=param.getDescription()%>
         <%
            } else 
            {
         %>
                <CODE><%=param.getName()%></CODE> - <%=param.getDescription()%>
          <% } %>
         </DD>
          
    <% }  // end for loop %>
    
    </DL>
<%
   }  // end main if statement
 %>

 
<%-- THROWN EXCEPTION --%>
<%
 if (!execmember.getThrownExceptions().isEmpty())
 { %>
<DL>
<DT>
<B>Throws:</B>
</DT>
<DD>
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
    <CODE><A HREF="cls.main.do?cls_id=<%=ex.getException().getId()%>"><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></A></CODE> - <%=ex.getDescription()%>
    <%
    } else
    {
    %>
    <CODE><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></CODE> - <%=ex.getDescription()%>
    <%
    }
    %>

<% } %>
</DD>
<%
 } %>
</DL>
 
<jsp:include page="doc_footer.jsp" flush="true"/>
 
