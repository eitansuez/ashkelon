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


<%-- SECTION: COMPONENT STYLES --%>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

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
   
   if (method.getReturnType() != null && method.getReturnType().getId() > 9)
   {
%>
  <P>
  <B>Returns</B> <A HREF="index.html?cmd=cls.main&amp;cls_id=<%=method.getReturnType().getId()%>"><%=name%></A><%=dim%>: <%=descr%>
  </P>
<%
   } else {
 %>   
  <P>
    <B>Returns</B> <%=name%><%=dim%>:  <%=descr%>
  </P>
<%
   }
   
 }  // end main if statement
 %>

 
 
<%-- PARAMETER INFO --%>
<%
   if (!execmember.getParameters().isEmpty())
   { %>
    <B>Parameters:</B>
    <UL>
    
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
                <LI><A HREF="index.html?cmd=cls.main&amp;cls_id=<%=param.getType().getId()%>"><B><%=typeName%></B></A><%=dimStr%> <%=param.getName()%>: <%=param.getDescription()%></LI>
         <%
            } else 
            {
         %>
                <LI><B><%=typeName%><%=dimStr%> <%=param.getName()%></B>: <%=param.getDescription()%></LI>
          <% } %>
          
    <% }  // end for loop %>
    
    </UL>
<%
   }  // end main if statement
 %>

 
<%-- THROWN EXCEPTION --%>
<%
 if (!execmember.getThrownExceptions().isEmpty())
 { %>
<B>Throws:</B>
<UL>
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
    <LI><A HREF="index.html?cmd=cls.main&cls_id=<%=ex.getException().getId()%>"><B><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></B></A>: <%=ex.getDescription()%></LI>
    <%
    } else
    {
    %>
    <LI><B><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></B>: <%=ex.getDescription()%></LI>
    <%
    }
    %>

<% } %>
</UL>
<%
 } %>
 
<jsp:include page="doc_footer.jsp" flush="true"/>
 
