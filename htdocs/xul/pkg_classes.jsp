<%@ page info="package classes view"
         import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
    
  String classtype = request.getParameter("classtype");
  String captions = "classes";
  String caption = "class";
  List classes = pkg.getOrdinaryClasses();

  boolean isinterface = false;
  
  if ("interface".equals(classtype))
  {
    captions = "interfaces";
    caption = "interface";
    classes = pkg.getInterfaces();
    isinterface = true;
  }
  else if ("exception".equals(classtype))
  {
    captions = "exceptions";
    caption = "exception";
    classes = pkg.getExceptionClasses();
  }
  else if ("errorClass".equals(classtype))
  {
    captions = "errors";
    caption = "error";
    classes = pkg.getErrorClasses();
  }
  
  request.setAttribute("classes", classes);
  String divid = "pkg_"+classtype;
  String emptyMsg = "No " + caption + " information available for " + pkg.getName();
  String captiondescr = "Brief " + caption + " descriptions";
 %>

<vbox flex="1">

<description><c:out value="${pkg.name}" /> <%=captions%></description>
<description><c:out value="${pkg.summaryDescription}" /></description>

<jsp:include page="class_list.jsp" flush="true">
  <jsp:param name="caption" value="<%=captiondescr%>" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="false" />
  <jsp:param name="classes_type" value="<%=classtype%>" />
  <jsp:param name="div_id" value="<%=divid%>" />
</jsp:include>

</vbox>
