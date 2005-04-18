<%@ page info="top page: api list" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String msg = (String) request.getAttribute("msg");
  API api = (API) request.getAttribute("api");
  String pkgNames = StringUtils.join(api.getPackagenames().toArray(), "\n");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>

<head>
  <c:import url="includes.html" />
<style>
textarea
{
  font-family: sans-serif;
  font-size: 10pt;
}
.form-break
{
  border-bottom: 1px solid #888;
  margin: 1em 0 0.5em 0;
  text-align: left;
  padding: 0;
  font-size: 1.1em;
}
table tr td:first-child
{
  text-align: right;
}
tr.field-help td:first-child
{
  text-align: left;
  font-style: italic;
  font-size: 0.8em;
}
tr.field-help td *
{
  border-bottom: 1px solid black;
  padding: 0.5em;
  margin: 0 3em 1em 3em;
  color: green;
}
label
{
  cursor: pointer;
  font-size: 0.9em;
}
label:hover
{
  color: green;
}
table
{
  border: 1px solid #888;
  padding: 0.5em;
  background-color: #eee;
}
div.instructions ul
{
  font-style: italic;
  color: green;
  margin-bottom: 1em; 
  font-size: 0.9em;
  border-bottom: 1px solid #888;
  text-align: left;
}
div.instructions ul li
{
  padding-bottom: 0.5em;
}

#apiform input, form textarea, form select
{
  border: 1px solid black;
}
.required
{
  background-color: #aaaaff;
}
</style>
<script>
function attachLabelHandlers()
{
  var ra = document.getElementsByTagName("label");
  for (var i=0; i<ra.length; i++)
  {
    ra[i].onclick = toggleDescription;
  }
}
function toggleDescription(evt)
{
  var target = getEvtTarget(evt);
  // pseudocode:
  // 1. get next row
  // 2. toggle its display property
  var node = target.parentNode;
  while (node.tagName.toLowerCase() != "tr")
    node = node.parentNode;
  var nextNode = node.nextSibling;
  while (nextNode.nodeType != ELEMENT_TYPE)
    nextNode = nextNode.nextSibling;
 
  if (nextNode.className == "field-help")
  {
    nextNode.style.display = (nextNode.style.display == "none" ) ? 
                                "table-row" : "none";
  }
}
function hideFieldHelp()
{
  var ra = document.forms['apiform'].getElementsByClassName("field-help");
  for (var i=0; i<ra.length; i++)
  {
    ra[i].style.display = "none";
  }
}
function setup()
{
  attachLabelHandlers();
  hideFieldHelp();
  focusFirstField();
}
function focusFirstField()
{
  document.forms['apiform'].elements['api-name'].select();
}
</script>
</head>

<body onload="setup();">


<jsp:include page="main_header.jsp" flush="true" />

<div class="pagebody">

<h3>New API</h3>

<div class="message">
  <%=msg%>
</div>




<form id="apiform" name="apiform" method="post" action="api.edit.do">

<input type="hidden" name="api-id" value="<%=api.getId()%>" />
<input type="hidden" name="populated" value="<%=api.populatedVal()%>" />

<table align="center">

<tr><td colspan="2">
<div class="instructions">
  <ul>
    <li>
      For field-specific help click on the field's label.
    </li>
    <li>
      Required fields are depicted with light blue backgrounds.
    </li>
  </ul>
</div>
</td></tr>

<tr><td>
  <label for="api-name">API Name:</label>
</td><td>
  <input class="required" name="api-name" type="text" value="<%=api.getName()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
Enter the name of the api.  For example:  dom4j or jdom
</div>
</td></tr>

<tr><td>
  <label for="summary-description">Summary Description:</label>
</td><td>
  <textarea name="summary-description" rows="2" cols="80"><%=api.getSummaryDescription()%></textarea>
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
Enter a short or summary description for this api (1-2 sentences)
</div>
</td></tr>

<tr><td>
  <label for="full-description">Full Description:</label>
</td><td>
  <textarea name="full-description" rows="5" cols="80"><%=api.getDescription()%></textarea>
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
Enter a more complete description of this api.  This may span many paragraphs and may include html tags if you so desire.
</div>
</td></tr>


<tr><td colspan="2">
<div class="form-break" />
</td></tr>

<tr><td>
  <label for="publisher">Publisher:</label>
</td><td>
  <input name="publisher" type="text" value="<%=api.getPublisher()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
The api's publisher.  This may be an individual or an organization.
</div>
</td></tr>

<tr><td>
  <label for="url">URL:</label>
</td><td>
  <input name="url" type="text" size="80" value="<%=api.getDownloadURL()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
The url for a web page where the api may be downloaded or documented.  e.g. http://www.hibernate.org would be a suitable entry for the hibernate api.
</div>
</td></tr>

<tr><td>
  <label for="release-date">Release Date:</label>
</td><td>
  <input name="release-date" type="text" value="<%=api.getReleaseDate()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
API release date.  Used only as a caption.  So formatting is free-form.
</div>
</td></tr>


<tr><td>
  <label for="version">Version:</label>
</td><td>
  <input class="required" name="version" type="text" size="10" value="<%=api.getVersion()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
API version number.  e.g. 1.2 or beta3
</div>
</td></tr>


<tr><td colspan="2">
<div class="form-break" />
</td></tr>

<tr><td>
  <label for="package-list">Packages to Document:</label>
</td><td>
  <textarea class="required" name="package-list" cols="40" rows="10"><%=pkgNames%></textarea>
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
List of packages that are part of this api, that you want to publish as part of the javadocs for this api.  If you've ever generated javadocs for your api before, then the javadoc engine has already created this information for you in a file named "package-list"
<br/>
You may simply copy and paste the contents of package-list here.  Otherwise, you can specify the list of package names directly in this text area.  Make sure to separate each package name by white space (that is, a space or carriage return)
</div>
</td></tr>

<tr><td colspan="2">
<div class="form-break">
Source Repository Information:
</div>
</td></tr>

<tr><td>
  <label for="repository-type">Repository Type:</label>
</td><td>
  <select name="repository-type">
    <option name="CVS" value="cvs">CVS</option>
  </select>
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
The type of source code repository where this API's source code resides.  openjavadocs.org requires that your source code be accessible via a source code repository.
</div>
</td></tr>


<tr><td>
  <label for="repository-url">Repository URL:</label>
</td><td>
  <input class="required" name="repository-url" type="text" size="80" value="<%=api.getRepository().getUrl()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
e.g.  :pserver:anonymous@cvs.sourceforge.net:/cvsroot/hibernate
</div>
</td></tr>

<tr><td>
  <label for="repository-module">Module Name:</label>
</td><td>
  <input class="required" name="repository-module" type="text" value="<%=api.getRepository().getModulename()%>"/>
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
The name of the CVS module where the API's source code resides.  e.g. hibernate
</div>
</td></tr>



<tr><td>
  <label for="repository-revision">Revision or Tag Name:</label>
</td><td>
  <input name="repository-revision" type="text" value="<%=api.getRepository().getTagname()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
The name of the tag or revision for this specific version of your API.  e.g. Release_v1-2
<br/>
This value is optional.  If not specified, then the source code will be fetched from the HEAD (trunk) revision.
</div>
</td></tr>


<tr><td>
  <label for="repository-sourcepath">Source Path:</label>
</td><td>
  <input class="required" name="repository-sourcepath" type="text" value="<%=api.getRepository().getSourcepath()%>" />
</td></tr>

<tr class="field-help"><td colspan="2">
<div>
This value is the relative path from your cvs module to your Java source code.  For example many jakarta projects place their source code in the "src/java" path.  Others simply use "src" or "src/main"
</div>
</td></tr>

<tr><td colspan="2">
<input type="submit" name="action" value="Save" />
</td></tr>
</table>

</form>


<div class="actions">
  <c:forEach items="${actions}" var="action" varStatus="status">
    <a href="<c:out value="${action.href}" />"><c:out value="${action.caption}" /></a>
    <c:if test="${!status.last}"> | </c:if>
  </c:forEach>
</div>


</div>

<c:import url="footer.html" />

</body>
</html>
