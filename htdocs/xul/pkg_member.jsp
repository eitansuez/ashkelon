<%@ page info="main package view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*"%>

<%
  JPackage pkg = (JPackage) request.getAttribute("pkg");
  List colHeaders = new ArrayList(4);
  List cols = new ArrayList(4);
  if (!pkg.getOrdinaryClasses().isEmpty())
  {
    cols.add(pkg.getOrdinaryClasses());
    colHeaders.add("Classes");
  }
  if (!pkg.getInterfaces().isEmpty())
  {
    cols.add(pkg.getInterfaces());
    colHeaders.add("Interfaces");
  }
  if (!pkg.getExceptionClasses().isEmpty())
  {
    cols.add(pkg.getExceptionClasses());
    colHeaders.add("Exceptions");
  }
  if (!pkg.getErrorClasses().isEmpty())
  {
    cols.add(pkg.getErrorClasses());
    colHeaders.add("Errors");
  }
%>

<vbox flex="1">
 <description><%= pkg.getSummaryDescription() %></description>

 <caption label="<%=pkg.getName()%>" />

 <hbox>
    <%
     ClassType c;
     String cls_type = "";
     String title = "";
     List col;

     for (int i=0; i<cols.size(); i++)
     {
       col = (List) cols.get(i);
       %>
        <listbox>
         <listhead>
          <listheader label="<%=colHeaders.get(i)%>" />
         </listhead>
       <%
       for (int j=0; j<col.size(); j++)
       {
          c = (ClassType) col.get(j);
          cls_type = c.getClassTypeName();
          title = HtmlUtils.cleanAttributeText(c.getDoc().getSummaryDescription());
          %>
          <listitem>
           <listcell label="<%=c.getName()%>" onclick="location.href='index.html?cmd=cls.main&amp;cls_id=<%=c.getId()%>';"
           />
          </listitem>
    <% } %>
        </listbox>
  <% } %>
   </hbox>
</vbox>

