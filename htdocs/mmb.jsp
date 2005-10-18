<%@ page info="page" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  boolean simple = (Boolean.valueOf((String) ServletUtils.getRequestParam(request, "simple"))).booleanValue();
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Ashkelon - Member Search Results</title>
  <jsp:include page="includes.jsp" flush="true"/>
</head>

<body onLoad="cleanTitles();">

<jsp:include page="main_header.jsp" flush="true"/>

<div class="pagebody">

  <p><%= request.getAttribute("total-results") %> matching entries</p>
  <jsp:include page="member_results.jsp" flush="true"/>

  <!-- paging -->
  <table width="100%"><tr>
  <% if (request.getAttribute("prev-cursor-position") != null) { %>
  <td align="left"> 
    <form method="get" action="member.do">
     <input type="hidden" name="simple" value="<%=request.getParameter("simple")%>" />
     <% if (simple) { %>
     <input type="hidden" name="searchField" value="<%=request.getParameter("searchField")%>" />
     <% }
        else
        {
          Map map = request.getParameterMap();
          Iterator itr = map.keySet().iterator();
          String paramName = null;
          while (itr.hasNext())
          {
            paramName = (String) itr.next();
            if ("cursor-position".equals(paramName)) continue;
            if ("selector".equals(paramName))
            {
               String[] selectors = request.getParameterValues(paramName);
               for (int i=0; i<selectors.length; i++)
               { %>
     <input type="hidden" name="<%=paramName%>" value="<%=selectors[i]%>" />
            <% }
            }
            else
            { %>
     <input type="hidden" name="<%=paramName%>" value="<%=request.getParameter(paramName)%>" />
         <% }
          }
        } %>
     <input type="hidden" name="cursor-position" value="<%= request.getAttribute("prev-cursor-position") %>" />
     <button type="submit">Previous</button>  </form>
  </td>
  <% } %>
  <% if (request.getAttribute("next-cursor-position") != null) { %><td align="right">
    <form method="get" action="member.do">
     <input type="hidden" name="simple" value="<%=request.getParameter("simple")%>" />
     <% if (simple) { %>
     <input type="hidden" name="searchField" value="<%=request.getParameter("searchField")%>" />
     <% }
        else
        {
          Map map = request.getParameterMap();
          Iterator itr = map.keySet().iterator();
          String paramName = null;
          while (itr.hasNext())
          {
            paramName = (String) itr.next();
            if ("cursor-position".equals(paramName)) continue;
            if ("selector".equals(paramName))
            {
               String[] selectors = request.getParameterValues(paramName);
               for (int i=0; i<selectors.length; i++)
               { %>
     <input type="hidden" name="<%=paramName%>" value="<%=selectors[i]%>" />
            <% }
            }
            else
            { %>
     <input type="hidden" name="<%=paramName%>" value="<%=request.getParameter(paramName)%>" />
         <% }
          }
        } %>
     <input type="hidden" name="cursor-position" value="<%= request.getAttribute("next-cursor-position") %>" />
     <button type="submit">Next</button>
    </form></td> 
  <% } %>
  </tr></table>

</div>

<c:import url="footer.html" />

</body>
</html>

