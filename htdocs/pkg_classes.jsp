<%@ page info="package classes view" import="java.util.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
    
  String classtype = request.getParameter("classtype");
  String captions = "classes";
  String caption = "class";
  List classes = pkg.getOrdinaryClasses();

  if ("interface".equals(classtype))
  {
    captions = "interfaces";
    caption = "interface";
    classes = pkg.getInterfaces();
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

<%-- SECTION: COMPONENT TEMPLATE --%> 

<h3><span class="package"><%=pkg.getName()%></span> <span class="<%=classtype%>" style="text-transform: capitalize"><%=captions%></span></h3>

<jsp:include page="pkg_info.jsp" flush="true"/>

<jsp:include page="class_list.jsp" flush="true">
  <jsp:param name="caption" value="<%=captiondescr%>" />
  <jsp:param name="empty_msg" value="<%=emptyMsg%>" />
  <jsp:param name="display_qualified" value="false" />
  <jsp:param name="classes_type" value="<%=classtype%>" />
  <jsp:param name="div_id" value="<%=divid%>" />
</jsp:include>

