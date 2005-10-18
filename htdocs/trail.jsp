<%@ page info="tabbed heading" import="java.util.*,org.ashkelon.util.*" %>

<% String style = request.getParameter("style"); %>
<% if (style==null || !style.equals("simple")) { %>

<%
   LinkedList trail = (LinkedList) session.getAttribute("trail");
 %>

 <div style="background-color: beige; padding: 3 3 3 3; border-bottom: 1px solid black;" title="Navigation Trail">
  <%
   String[] uriLabelPair = null;
   String uri, caption, itemtype;
   for (int i=0; i<trail.size()-1; i++)
   {
     if (i==0) out.println("Trail: ");
     uriLabelPair = (String[]) trail.get(i);
     uri = uriLabelPair[0];
     caption = uriLabelPair[1];
     itemtype = uriLabelPair[2];
  %>
  <% if (StringUtils.isBlank(itemtype)) { %>
     <a href="<%=uri%>"><%=caption%></a>
  <% } else { %>
     <a href="<%=uri%>"><span class="<%=itemtype%>"><%=caption%></span></a>
  <% } // end if %>

    <% if (i < trail.size() - 2) out.println("<img src=\""+request.getContextPath()+"/images/arrow_rt.gif\">"); %>
  <% } %>

  <%--
    <form method="GET" action="<%=request.getContextPath()%>/trail.reset.do">
  <td align="right">
    <% if (trail.size() > 3) { %>
      <button type="submit" class="footer" title="Reset navigation trail" STYLE="background-color: #dddddd;">Reset Trail</button>
    <% } %>
  </td>
    </form>
   --%>
 </div>

<% } %>

