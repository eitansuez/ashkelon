<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<body>

<%
 String test = "this is a test";
 response.test = test;
 %>

<%=test%>

<c:out value="${test}" />

</body>
</html>
