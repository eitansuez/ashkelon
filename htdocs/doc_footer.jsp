<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

footer containing since, see also, and deprecated information for various
 doc types.  present at bottom of class, member, and package pages.
--%>

<%-- SECTION: COMPONENT CODE --%>
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

<%-- if (doc.hasAnythingToShow()) { --%>

<%-- SECTION: COMPONENT STYLES --%>
<style type="text/css">
.doc_footer
{
  font-size: 8pt;
  margin-top: 3em;
}
hr.docFooter
{
  color: #808080;
}
</style>

<%-- SECTION: COMPONENT TEMPLATE --%> 

  <div class="doc_footer">

  <% 
  boolean hrin = false;
  if (!StringUtils.isBlank(doc.getSince())) {
    hrin = true;
  %>
  <hr class="docFooter" />
  Since: <%=doc.getSince()%>
  <br/>
  <% } %>

  <% if (!StringUtils.isBlank(doc.getDeprecated())) { %>
  <% if (!hrin) { %>
  <hr class="docFooter" />
  <% } %>
  Deprecated: <%=doc.getDeprecated()%>
  <br/>
  <% } %>

  <% List refs = doc.getReferences();
     if (refs != null && !refs.isEmpty()) { %>
  <% if (!hrin) { %>
  <hr class="docFooter" />
  <% } %>
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

  <% }  // end if refs not is empty %>

  </div>
  
<%-- }  // end if has anything to show --%>

