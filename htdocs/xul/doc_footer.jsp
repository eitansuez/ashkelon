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

<html:br />

<html:table width="80%" border="0" cellspacing="0" cellpadding="3">
<html:tr>
	<html:td align="left" class="docFooter">
  <% 
  boolean hrin = false;
  if (!StringUtils.isBlank(doc.getSince())) {
    hrin = true;
  %>
  <html:hr class="docFooter" />
  Since: <%=doc.getSince()%>
  <html:br />
  <% } %>

  <% if (!StringUtils.isBlank(doc.getDeprecated())) { %>
  <% if (!hrin) { %>
  <html:hr class="docFooter">
  <% } %>
  Deprecated: <%=doc.getDeprecated()%>
  <html:br />
  <% } %>

  <% List refs = doc.getReferences();
     if (refs != null && !refs.isEmpty()) { %>
  <% if (!hrin) { %>
  <html:hr class="docFooter">
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
      <html:a href="index.html?cmd=<%=refdoctypename%>.main&<%=refdoctypename%>_id=<%=refDoc.getId()%>"><%=label%></html:a>
    <% } else { %>
      <%=label%>
    <% } %>
  <% }  // end for refs %>

  <html:br/>
  <% }  // end if refs not is empty %>
  
  </html:td>
</html:tr>
</html:table>

<%-- }  // end if has anything to show --%>
