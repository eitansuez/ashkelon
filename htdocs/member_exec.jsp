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


<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
  var selectedColor = "lightyellow";
  function showDescr(i)
  {
    try {
      getElementById("exec"+i).bgColor = selectedColor;
      getElementById("detailDescr"+i).style.display = "";  // show description
    } catch (ex) {}
  }
  
  function toggleDetailDescr(source, targetIdx)
  {
    try {
      // highlight row:
      var tableRows = source.parentNode.getElementsByTagName("TR");
      for (var i=0; i<tableRows.length; i++)
        tableRows[i].bgColor = "#FFFFFF";
      source.bgColor = selectedColor;
      
      // show descriptions:
      var numRows = getElementById("detailDescriptions").getElementsByTagName("DIV").length;
      for (var i=0; i<numRows; i++)
      {
        getElementById("detailDescr"+i).style.display = "none";
      }
      getElementById("detailDescr"+targetIdx).style.display = "";
    } catch (ex)
    {
      alert(ex);
    }
  }
  </SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<table width="100%">
<tr valign="top">
<td style="width: 300px;">

<table class="columnar" rules="rows" border="1" cellspacing="0" cellpadding="5">
<caption>
<% if (execs.size()>1)  { %>
  <span class="<%=membertype%>"><%=caption%></span> has <%=execs.size()%> variants:
<% } %>
</caption>
<TBODY>

<%
 ExecMember exec;
 List exs, params;
 String modifiers;
 for (int i=0; i<execs.size(); i++)
 {
  exec = (ExecMember) execs.get(i);
  exs = exec.getThrownExceptions();
  params = exec.getParameters();
  modifiers = exec.getModifiers();
  if (exec.getDoc().isDeprecated())
    modifiers += " deprecated";
 %>
 <TR ID="exec<%=i%>" onClick="toggleDetailDescr(this, <%=i%>);" STYLE="cursor: pointer;">
 <TD>
  <%= i+1 %>.
 </TD>
 <TD CLASS="<%=modifiers%>">
   <%=modifiers%>

   <% if (membertype.equals("method"))
   { %>
   <% MethodMember method = (MethodMember) exec;
      String name = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkgName);
      String dim = JDocUtil.getDimension(method.getReturnTypeDimension());
   %>
   <span title="<%=HtmlUtils.cleanAttributeText(method.getReturnDescription())%>">
   <% if (method.getReturnType() != null && method.getReturnType().getId() > 0)
      { %>
     <A HREF="cls.main.do?cls_id=<%=method.getReturnType().getId()%>"><%=name%></A><%=dim%>
   <% } else 
      { %>
     <%=name%><%=dim%>
   <% } %>
   </span>
<% } %>

   <span class="<%=membertype%>"><%=exec.getName()%></span>
     (
   
   <%
      ParameterInfo param;
      String dimStr = "";
      String typeName = "";
      for (int j=0; j<params.size(); j++)
      {
          param = (ParameterInfo) params.get(j);
          typeName = JDocUtil.conditionalQualify(param.getTypeName(), pkgName);
          dimStr = JDocUtil.getDimension(param.getTypeDimension());
   %>
   <span title="<%=HtmlUtils.cleanAttributeText(param.getDescription())%>">
   <% if (param.getType()!=null && param.getType().getId()>0)
      { %>
     <A HREF="cls.main.do?cls_id=<%=param.getType().getId()%>"><%=typeName%></A><%=dimStr%> <%=param.getName()%>
   <% } else 
      { %>
     <%=typeName%><%=dimStr%> <%=param.getName()%>
   <% } %>
   
   <% if (j < params.size() - 1) { %>
    , 
   <% } %>
   </span>
   <% } // end for params
    %>

  )
   
 <% if (exs.size() > 0) { %>
   throws
 <% } %>
   
 <%    
      ThrownException ex;
      for (int j=0; j<exs.size(); j++)
      {
        ex = (ThrownException) exs.get(j);
 %>
       <span title="<%=HtmlUtils.cleanAttributeText(ex.getDescription())%>">
         <%
          if (ex.getException()!=null && ex.getException().getId()>0)
          {
          %>
           <A HREF="cls.main.do?cls_id=<%=ex.getException().getId()%>"><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></A>
          <%
          } else
          {
          %>
           <%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%>
          <%
          }
          %>
       </span>
<%
      } // end for 
 %>
 
 </TD>
 </TR>
 <%
 }  // end iterating over method/constructor variants
 %>

</TBODY>
</TABLE>

</td> <td>

  <div align="center">Description</div>
  <DIV ID="detailDescriptions" STYLE="border: thin solid black; padding: 10px;">
<% for (int i=0; i<execs.size(); i++)
   {
     request.setAttribute("execmember", execs.get(i));
 %>
      <DIV ID="detailDescr<%=i%>" STYLE="display: none;">
        <jsp:include page="member_child_descr.jsp" flush="true"/>  
      </DIV>
<% } %>
  </DIV>

</td>
</tr>
</table>

  
<SCRIPT>
  showDescr(0);
</SCRIPT>

