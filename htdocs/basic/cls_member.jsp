<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  ClassType cls = (ClassType) request.getAttribute("cls");
  JPackage pkg = cls.getPackage();
  String cls_type = cls.getClassTypeName();
  List members;
  String descr;
%>


<A NAME="field_summary"><!-- --></A>
  <%
     members = cls.getFields();
     if (!members.isEmpty())
     {
  %>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B>Field Summary</B></FONT></TD>
</TR>
    <%
     FieldMember field;
     for (int i=0; i<members.size(); i++)
     {
        field = (FieldMember) members.get(i);
        descr = field.getDoc().getSummaryDescription();
        %>
    <TR BGCOLOR="white" CLASS="TableRowColor">
      <TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
        <CODE>
          <%=field.getModifiers()%> <%=field.getTypeName()%>
        </CODE>
        </FONT>
        </TD>
        <TD>
          <CODE><B>
            <A HREF="member.main.do?member_id=<%=field.getId()%>"><%=field.getName()%></A>
          </B></CODE>
          <BR>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=descr%>
        </TD>

      </TD>
    </TR>
  <% } %>
</TABLE>
<BR>
   <% }  // end if emtpy  %>
  

   
<A NAME="constructor_summary"><!-- --></A>
   <%
     members = cls.getConstructors();
     if (!members.isEmpty())
     {
   %>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B>Constructor Summary</B></FONT></TD>
</TR>
    <%
     ConstructorMember constructor;
     for (int i=0; i<members.size(); i++)
     {
        constructor = (ConstructorMember) members.get(i);
        descr = constructor.getDoc().getSummaryDescription();
        %>
<TR BGCOLOR="white" CLASS="TableRowColor">
    	<TD>
        <CODE><B><A HREF="member.main.do?member_id=<%=constructor.getId()%>#<%=constructor.getId()%>"><%=constructor.getName()%><%=constructor.getSignature()%></A></B></CODE>
<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=descr%>
      </TD>
    </TR>
  <% } %>
    </TABLE>

   <% }  // end if emtpy  %>

  

<A NAME="method_summary"><!-- --></A>
   <%
     members = cls.getMethods();
     if (!members.isEmpty())
     {
   %>
<BR>
<TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TD COLSPAN=2><FONT SIZE="+2">
<B>Method Summary</B></FONT></TD>
</TR>
    <%
     MethodMember method;
     String returninfo;
     String returnClass;
     if (members.isEmpty())
       out.println("<TR><TD>none</TD></TR>");
     for (int i=0; i<members.size(); i++)
     {
        method = (MethodMember) members.get(i);
        descr = method.getDoc().getSummaryDescription();
        //returninfo = JDocUtil.conditionalQualify(method.getReturnTypeName(), pkg.getName()) +
        //                JDocUtil.getDimension(method.getReturnTypeDimension());
        returninfo = JDocUtil.unqualify(method.getReturnTypeName()) +
                        JDocUtil.getDimension(method.getReturnTypeDimension());
        returnClass = (method.getReturnType()!=null && method.getReturnType().getId()>0) ? "ordinaryClass" : "";
          // note, could also be an interface.  so need to handle this (later)
        %>
    <TR BGCOLOR="white" CLASS="TableRowColor">
      <TD ALIGN="right" VALIGN="top" WIDTH="1%">
        <FONT SIZE="-1">
          <CODE>
        <% if (!returnClass.equals("")) { // return type info %>
          <A HREF="cls.main.do?id=<%=method.getReturnType().getId()%>"><%=returninfo%></A>
        <% } else { %>
          <%=returninfo%>
        <% } // end if %>
          </CODE>
        </FONT>
      </TD>
      <TD>

<CODE><B>
<A HREF="member.main.do?member_id=<%=method.getId()%>#<%=method.getId()%>"><%=method.getName()%><%=method.getSignature()%></A>
</B></CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=descr%>
      </TD>
    </TR>
  <% } %>
    </TABLE>
   <% }  // end if emtpy  %>


