<%@ page info="package classes view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
    
  String classtype = request.getParameter("classtype");
  String caption = "Class";
  List classes = pkg.getOrdinaryClasses();

  boolean isinterface = false;
  
  if ("interface".equals(classtype))
  {
    caption = "Interface";
    classes = pkg.getInterfaces();
    isinterface = true;
  }
  else if ("exception".equals(classtype))
  {
    caption = "Exception";
    classes = pkg.getExceptionClasses();
  }
  else if ("errorClass".equals(classtype))
  {
    caption = "Error";
    classes = pkg.getErrorClasses();
  }
  
  request.setAttribute("classes", classes);
 %>


<% if (!classes.isEmpty()) { %>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B><%=caption%> Summary</B></FONT></TD>
</TR>

    <%
     ClassType cls;
     
     for (int i=0; i<classes.size(); i++)
     {
          cls = (ClassType) classes.get(i);
          %>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD WIDTH="15%">
<% if (isinterface) { %>
<B><I><A HREF="cls.main.do?id=<%=cls.getId()%>"><%=cls.getName()%></A></I></B>
<% } else { %>
<B><A HREF="cls.main.do?id=<%=cls.getId()%>"><%=cls.getName()%></A></B>
<% } %>
</TD>
<TD><%=cls.getSummaryDescription() %></TD>
</TR>
  <%
     }
   %>
</TABLE>

  <BR>
<% } %>


