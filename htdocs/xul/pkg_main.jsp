<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<vbox flex="1">
<c:if test="${!empty pkg.API}">
<html:a href="index.html?cmd=api.main&amp;id=<c:out value="${pkg.API.id}" />"><description>
  (<c:out value="${pkg.API.name}" /> API)
</description></html:a>
</c:if>

<!-- problem is that javadoc design is bad ; they embed html tags in the java source
  code ; so now that html is dead and xul is in, we cannot guarantee valid xml documents
  unless we enclose the information as a cdata block -->
  <description>
    <c:out value="${pkg.description}" />
  </description>
  
</vbox>
