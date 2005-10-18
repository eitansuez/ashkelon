<%@ page info="exec member view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.*" %>

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
<script>
  function selectItem(targetIdx)
  {
      var olnode = document.getElementById("variants");
      var listItem = olnode.getElementsByClassName("selected")[0];
      deselect(listItem);

      var listItems = olnode.getElementsByTagName("li");
      select(listItems[targetIdx]);
  }
  function select(node)
  {
    addClass(node, "selected");
    document.getElementById(getCorrespondingId(node)).style.display = "block";
  }
  function deselect(node)
  {
    if (!node) return;
    removeClass(node, "selected");
    document.getElementById(getCorrespondingId(node)).style.display = "none";
  }
  function getCorrespondingId(node)
  {
    return node.getAttribute("corresponding-descr");
  }
  </script>

<style>
#variants
{
  border: 1px solid gray;
  list-style-position: inside;
  padding: 0px;
  margin-top: 0px;
}
#variants li
{
  border-bottom: 1px dotted gray;
  padding: 0.2em 0.5em;
  cursor: pointer;
}
#variants li:hover
{
  background-color: #ff8;
}
#variants li.selected
{
  background-color: lightyellow;
}
#detailDescriptions
{
  border: 1px solid gray;
  padding: 0.5em;
  position: relative;
  height: 250px;
  overflow: auto;
}
#detailDescriptions div.descr-item
{
  position: absolute;
  display: none;
}
</style>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<div style="float: left; width: 45%;">

<% if (execs.size()>1)  { %>
  <span class="<%=membertype%>"><%=caption%></span> has <%=execs.size()%> variants:
<% } %>

<ol id="variants">

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
 <li id="exec<%=i%>" class="<%=modifiers%>" onClick="selectItem(<%=i%>);" 
     corresponding-descr="detailDescr<%=i%>">
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
     <a href="cls.main.do?id=<%=method.getReturnType().getId()%>"><%=name%></a><%=dim%>
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
     <a href="cls.main.do?id=<%=param.getType().getId()%>"><%=typeName%></a><%=dimStr%> <%=param.getName()%>
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
           <a href="cls.main.do?id=<%=ex.getException().getId()%>"><%=JDocUtil.conditionalQualify(ex.getName(), pkgName)%></a>
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
 
 </li>
 <%
 }  // end iterating over method/constructor variants
 %>

</ol>

</div>

<div style="float: right; width: 50%;">

  <div align="center">Description</div>
  <div id="detailDescriptions">
<% for (int i=0; i<execs.size(); i++)
   {
     request.setAttribute("execmember", execs.get(i));
 %>
      <div class="descr-item" id="detailDescr<%=i%>">
        <jsp:include page="member_child_descr.jsp" flush="true"/>
      </div>
<% } %>
  </div>

</div>

<div style="clear: both;"></div>
<script>selectItem(0);</script>

