<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  List cols = new ArrayList(4);
  if (!pkg.getOrdinaryClasses().isEmpty())
    cols.add(pkg.getOrdinaryClasses());
  if (!pkg.getInterfaces().isEmpty())
    cols.add(pkg.getInterfaces());
  if (!pkg.getExceptionClasses().isEmpty())
    cols.add(pkg.getExceptionClasses());
  if (!pkg.getErrorClasses().isEmpty())
    cols.add(pkg.getErrorClasses());
%>

<jsp:include page="pkg_info.jsp" flush="true"/>

<div align="center">
<table class="columnar">
<caption class="package"><%=pkg.getName()%></caption>
<colgroup span="4" align="left"></colgroup>
<!--
<THEAD CLASS="table_header">
<TR>
  <TD>Classes</TD>
  <TD>Interfaces</TD>
  <TD>Exceptions</TD>
  <TD>Errors</TD>
</TR>
</THEAD>
-->
<tbody>
<tr>
    <%
     ClassType c;
     String cls_type = "";
     String title = "";
     List col;
     
     for (int i=0; i<cols.size(); i++)
     { %>
       <td valign="top">
<div class="scroll_column">
         <table>
    <%
       col = (List) cols.get(i);
       for (int j=0; j<col.size(); j++)
       {
          c = (ClassType) col.get(j);
          cls_type = c.getClassTypeName();
          title = HtmlUtils.cleanAttributeText(c.getDoc().getSummaryDescription());
          %>
        <tr>
        	<td>
            <a href="cls.main.do?cls_id=<%=c.getId()%>"><span class="<%=cls_type%> <%=c.getModifiers()%> <%=c.isDeprecated() ? "deprecated" : ""%>" title="<%=title%>"><%=c.getName()%></span></a>
          </td>
        </tr>
    <%
       } %>
        </table>
</div>
      </td>
  <%
     }
   %>
</tr>
</tbody>
</table>
</div>

