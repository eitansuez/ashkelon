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

<P><%= pkg.getSummaryDescription() %></P>

<DIV ALIGN="CENTER">
<TABLE CLASS="columnar">
<CAPTION CLASS="package"><%=pkg.getName()%></CAPTION>
<COLGROUP SPAN="4" ALIGN="LEFT"></COLGROUP>
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
<TBODY>
<TR>
    <%
     ClassType c;
     String cls_type = "";
     String title = "";
     List col;
     
     for (int i=0; i<cols.size(); i++)
     { %>
       <TD VALIGN="TOP">
<DIV CLASS="scroll_column">
         <TABLE>
    <%
       col = (List) cols.get(i);
       for (int j=0; j<col.size(); j++)
       {
          c = (ClassType) col.get(j);
          cls_type = c.getClassTypeName();
          title = HtmlUtils.cleanAttributeText(c.getDoc().getSummaryDescription());
          %>
        <TR>
        	<TD>
            <A HREF="cls.main.do?cls_id=<%=c.getId()%>"><SPAN CLASS="<%=cls_type%> <%=c.getModifiers()%> <%=c.isDeprecated() ? "deprecated" : ""%>" TITLE="<%=title%>"><%=c.getName()%></SPAN></A>
          </TD>
        </TR>
    <%
       } %>
        </TABLE>
</DIV>
      </TD>
  <%
     }
   %>
</TR>
</TBODY>
</TABLE>
</DIV>

