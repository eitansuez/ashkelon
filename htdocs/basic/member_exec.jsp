<%@ page info="exec member view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  List execs = (List) request.getAttribute("members");
  Object maxExMethod = Collections.max(execs,
      new Comparator()
      { 
        public int compare(Object f, Object s)
        { 
          return ((ExecMember)f).getThrownExceptions().size() - ((ExecMember)s).getThrownExceptions().size();
        }
      });
  int maxExceptions = ((ExecMember) maxExMethod).getThrownExceptions().size();
  
  Collections.sort(execs, (Comparator) execs.get(0)); // by parameter
  ExecMember firstExec = (ExecMember) execs.get(execs.size()-1);

  int maxParams = ((ExecMember) execs.get(execs.size()-1)).getParameters().size();
  
  String membertype = firstExec.getMemberTypeName();
  //String caption = firstExec.getModifiers() + " " + firstExec.getName();
  String caption = firstExec.getName() + "()";
  
  request.setAttribute("member", firstExec);
  ClassType cls = firstExec.getContainingClass();
  String pkgName = cls.getPackage().getName();
%>


<%-- SECTION: COMPONENT TEMPLATE --%> 

<% if (execs.size()>1)  { %>
  <P><%=caption%> has <%=execs.size()%> variants:</P>
<% } %>

<%
 ExecMember exec;
 List exs, params;
 for (int i=0; i<execs.size(); i++)
 {
  exec = (ExecMember) execs.get(i);
  exs = exec.getThrownExceptions();
  params = exec.getParameters();
  request.setAttribute("execmember", execs.get(i));
 %>

  <HR>
  <!-- <A NAME="<%=exec.getFullyQualifiedName()%>"> -->
  <A NAME="<%=exec.getId()%>">
  
  <H3><%=exec.getName()%></H3>
  <TT>
  
   <%=exec.getModifiers()%>

   <% if (membertype.equals("method"))
   { %>
   <% MethodMember method = (MethodMember) exec;
      String name = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkgName);
      String dim = JDocUtil.getDimension(method.getReturnTypeDimension());
   %>

   <% if (method.getReturnType() != null && method.getReturnType().getId() > 0)
      { %>
     <A HREF="index.html?cmd=cls.main&cls_id=<%=method.getReturnType().getId()%>"><%=name%></A><%=dim%>
   <% } else 
      { %>
     <%=name%><%=dim%>
   <% } %>

<% } %>

   <B><%=exec.getName()%></B>(<%
      ParameterInfo param;
      String dimStr = "";
      String typeName = "";
      for (int j=0; j<params.size(); j++)
      {
          param = (ParameterInfo) params.get(j);
          typeName = JDocUtil.conditionalQualify(param.getTypeName(), pkgName);
          dimStr = JDocUtil.getDimension(param.getTypeDimension());
   %>

   <% if (param.getType()!=null && param.getType().getId()>0)
      { %>
     <A HREF="index.html?cmd=cls.main&cls_id=<%=param.getType().getId()%>"><%=typeName%></A><%=dimStr%> <%=param.getName()%>
   <% } else 
      { %>
     <%=typeName%><%=dimStr%> <%=param.getName()%>
   <% } %>
   
   <% if (j < params.size() - 1) { %>
    , 
   <% } %>

   <% } // end for params
    %>)
   
 <% if (exs.size() > 0) { %>
   <BR>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;throws
 <% } %>
   
 <%    
      ThrownException ex;
      for (int j=0; j<exs.size(); j++)
      {
        ex = (ThrownException) exs.get(j);
 %>

         <%
          if (ex.getException()!=null && ex.getException().getId()>0)
          {
          %>
           <A HREF="index.html?cmd=cls.main&cls_id=<%=ex.getException().getId()%>"><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></A>
          <%
          } else
          {
          %>
           <%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%>
          <%
          }
          %>

<%
      } // end for 
 %>

  </TT>
 
     <DL><DD>
        <jsp:include page="member_child_descr.jsp" flush="true"/>  
     </DD></DL>

 <%
 }  // end iterating over method/constructor variants
 %>

