<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title><c:out value="${cls.name}" /> Cross References</TITLE>
  <c:import url="includes.html" />

  <script>
  function init()
  {
    if (IE)
      document.onclick = expandCollapse;
    else
      document.addEventListener("click", expandCollapse, false);
  }
  </script>
</head>

<body onLoad="init();loadCookies();cleanTitles();" onUnload="saveCookies();">

 <c:import url="main_header.jsp" />
 <c:import url="cls_header_svr.jsp" />
  
  <div class="pagebody">
    <c:import url="cls_info.jsp" />
    <c:import url="cls_xref.jsp" />
  </div>
  
  <c:import url="footer.html" />

</body>
</html>

