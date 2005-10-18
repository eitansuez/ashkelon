<%@ page info="class's inner classes view (component)" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: November 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  
  List classes = cls.getInnerClasses();
  ClassType inner;
%>


<% if (classes != null && !classes.isEmpty()) { %>

<A NAME="inner_summary"><!-- --></A>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B>Inner Class Summary</B></FONT></TD>
</TR>
<%
   String modifier;
   for (int i=0; i<classes.size(); i++)
   {
     inner = (ClassType) classes.get(i);
     modifier = inner.getModifiers();
     if (inner.getClassType() == ClassType.INTERFACE)
       modifier += " interface";
     else
       modifier += " class";
%>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE><%=modifier%></CODE></FONT></TD>
<TD><CODE><B><A HREF="cls.main.do?id=<%=inner.getId()%>"><%=inner.getName()%></A></B></CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=inner.getSummaryDescription()%></TD>
</TR>
<% } %>
</TABLE>

<% } // end if %>

