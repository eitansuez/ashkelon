<%@ page info="package classes view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  String caption = ServletUtils.getRequestParam(request, "caption");
  if (StringUtils.isBlank(caption))
  {
    caption = "Class List";
  }
  
  String emptyMsg = ServletUtils.getRequestParam(request, "empty_msg");
  if (StringUtils.isBlank(emptyMsg))
  {
    emptyMsg = "No classes found.";
  }
  
  String displayQualified = ServletUtils.getRequestParam(request, "display_qualified");
  if (StringUtils.isBlank(displayQualified))
  {
    displayQualified = "true";
  }
  
  String classestype = ServletUtils.getRequestParam(request, "classes_type");
  if (StringUtils.isBlank(classestype))
  {
    classestype = "all";
  }
  
  List classes = (List) request.getAttribute("classes");
  if (classes == null)
  {
    classes = new ArrayList();
  }
  
  String containerDiv = ServletUtils.getRequestParam(request, "div_id");
%>

<c:choose>
 <c:when test="${classes.empty}">
  <description><%=emptyMsg%></description>
 </c:when>
 <c:otherwise>
  
<%
   boolean isinterface = "interface".equals(classestype);
   int colsadjust = (isinterface) ? (0) : (2);
   String divid = containerDiv;
   String colname_heading = (classestype.equals("all")) ? "Class Names" : classestype + " Names";
   boolean qualify = ("true".equals(displayQualified)) ? true : false;
   String classtype = "";
%>

<description label="<%=caption%>" />
<grid>
 <rows>
 
 <c:forEach items="${classes}" var="cls">
  <row>
<% if (!isinterface) { %>
   <c:if test="${cls.abstract}">
    <image src="images/check_sm.gif" />
   </c:if>
<% } %>
   <c:if test="${cls.accessibility == JDocUtil.PUBLIC}">
    <image src="images/check_sm.gif" />
   </c:if>
   <c:if test="${cls.static}">
    <image src="images/check_sm.gif" />
   </c:if>
<% if (!isinterface) { %>
   <c:if test="${cls.final}">
    <image src="images/check_sm.gif" />
   </c:if>
<% } %>
    <description><c:out value="${cls.doc.cleanSince}" /></description>
    <c:if test="${cls.deprecated}">
      <image src="images/check_sm.gif" />
    </c:if>
  <html:a href="index.html?cmd=cls.main&amp;id=<c:out value="${cls.id}" />">
    <description>
      <c:choose>
       <c:when test="${qualify}">
        <c:out value="${cls.qualifiedName}" />
       </c:when>
       <c:otherwise>
        <c:out value="${cls.name}" />
       </c:otherwise>
      </c:choose>
    </description>
  </html:a>
  <description><c:out value="${cls.summaryDescription}" /></description>
 </row>
</c:forEach>
</rows>
</grid>

</c:otherwise>
</c:choose>

