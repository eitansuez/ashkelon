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
<STYLE TYPE="text/css">
.docFooter
{
  font-size: 8pt;
}
HR.docFooter
{
  color: #808080;
}
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 

<BR>

<TABLE WIDTH="80%" BORDER="0" CELLSPACING="0" CELLPADDING="3">
<TR>
	<TD ALIGN="LEFT" CLASS="docFooter">
  <% 
  boolean hrin = false;
  if (!StringUtils.isBlank(doc.getSince())) {
    hrin = true;
  %>
  <HR CLASS="docFooter">
  Since: <%=doc.getSince()%>
  <BR>
  <% } %>

  <% if (!StringUtils.isBlank(doc.getDeprecated())) { %>
  <% if (!hrin) { %>
  <HR CLASS="docFooter">
  <% } %>
  Deprecated: <%=doc.getDeprecated()%>
  <BR>
  <% } %>

  <% List refs = doc.getReferences();
     if (refs != null && !refs.isEmpty()) { %>
  <% if (!hrin) { %>
  <HR CLASS="docFooter">
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
      <A HREF="index.html?cmd=<%=refdoctypename%>.main&<%=refdoctypename%>_id=<%=refDoc.getId()%>"><%=label%></A>
    <% } else { %>
      <%=label%>
    <% } %>
  <% }  // end for refs %>

  <BR>
  <% }  // end if refs not is empty %>
  
  </TD>
</TR>
</TABLE>

<%-- }  // end if has anything to show --%>
