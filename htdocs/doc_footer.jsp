<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%--
Author: Eitan Suez
Date: March 2001
Description:
 footer containing since, see also, and deprecated information for various
 doc types.  present at bottom of class, member, and package pages.
--%>

<%
String name = null;
String pkgname = null;
DocInfo doc = null;

if (request.getAttribute("cls") != null)
{
  ClassType cls = (ClassType) request.getAttribute("cls");
  name = cls.getQualifiedName();
  pkgname = cls.getPackage().getName();
  doc = cls.getDoc();
}
if (request.getAttribute("pkg") != null)
{
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  name = pkg.getName();
  pkgname = name;
  doc = pkg.getDoc();
}
if (request.getAttribute("member") != null)
{
  Member member = (Member) request.getAttribute("member");
  name = member.getQualifiedName();
  pkgname = member.getPackage().getName();
  doc = member.getDoc();
}
if (request.getAttribute("execmember") != null)
{
  Member member = (Member) request.getAttribute("member");
  name = member.getQualifiedName();
  pkgname = member.getPackage().getName();
  doc = member.getDoc();
}
%>

<style>
.doc-footer
{
  font-size: 8pt;
  margin-top: 3em;
  border-top: 1px solid #aaa;
  padding-top: 0.3em;
}
</style>

<div class="doc-footer">

  <% if (!StringUtils.isBlank(doc.getSince())) { %>
  <div>
  Since: <%=doc.getSince()%>
  </div>
  <% } %>

  <% if (!StringUtils.isBlank(doc.getDeprecated())) { %>
  <div>
  Deprecated: <%=doc.getDeprecated()%>
  </div>
  <% } %>

  <% List refs = doc.getReferences();
     if (refs != null && !refs.isEmpty()) { %>
  <div>
  See Also:
  <% Reference ref;
     String label;
     int refdoctype;
     DocInfo refDoc;
     String refdoctypename;
     for (int i=0; i<refs.size(); i++)
     {
       ref = (Reference) refs.get(i);
       label = (StringUtils.isBlank(ref.getLabel())) ? ref.getRefDocName() : ref.getLabel();
       if (StringUtils.isBlank(label))
         continue;
       else
         label = JDocUtil.conditionalQualify(label, pkgname);
       refdoctype = ref.getRefDocType();
       refDoc = ref.getRefDoc();
       refdoctypename = "";
  %>
    <%=((i>0) ? "," : "")%>
    <% if (refDoc.getId() > 0)
       {
         refdoctypename = DocInfo.DOCTYPES[refdoctype-1];
     %>
      <a href="<%=refdoctypename%>.main.do?<%=refdoctypename%>_id=<%=refDoc.getId()%>"><%=label%></a>
    <% } else { %>
      <%=label%>
    <% } %>
  <% }  // end for refs %>

  </div>
  <% }  // end if refs not is empty %>

</div>
  

