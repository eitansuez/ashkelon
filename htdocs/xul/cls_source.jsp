<%@ page info="main class view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.io.*,java.text.*,org.java2html.*"%>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001

Template page for Class or Interface types.
--%>

<%
  String cls_name = (String) request.getAttribute("cls_name");
  String source_file = (String) request.getAttribute("source_file");
 %>

<%-- SECTION: TEMPLATE --%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<HEAD>
	<TITLE>Source file: <%=cls_name%></TITLE>
  <jsp:include page="includes.html" flush="true"/>

  <!-- SECTION: PAGE STYLES -->
  <STYLE TYPE="text/css">
  </STYLE>

  <!-- SECTION: BEHAVIOR (JAVASCRIPT) -->
  <SCRIPT>
  </SCRIPT>
</HEAD>

<BODY>
  <jsp:include page="main_header.jsp" flush="true"/>
  
  <DIV CLASS="PAGEBODY">

  <% if (StringUtils.isBlank(source_file)) { %>
    <P>Unable to locate source file for class <%=cls_name%></P>
  <% } else { %>

  <TABLE CLASS="columnar" WIDTH="90%" ALIGN="CENTER">
   
   <TR><TD>
    <DIV CLASS="scroll_table" STYLE="height: 470;">

  <PRE><%
  
      try
      {
        FileReader fr = new FileReader(source_file);
        Java2Html j2h = new Java2Html(fr, out);
        j2h.processFile();
        
        /*
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        int i=1;
        DecimalFormat df = new DecimalFormat("0000");
        while ( (line = br.readLine()) != null )
        {
          line = StringUtils.substitute(line, "<", "&lt;");
          line = StringUtils.substitute(line, ">", "&gt;");
          boolean longline = false;
          while (line.length() > 80)
          {
            longline = true;
            out.println(df.format(i) + " " + line.substring(0, 80));
            line = line.substring(81);
          }
          if (longline)
            out.println(".... " + line);
          else
            out.println(df.format(i) + " " + line);
          i++;
        }
        br.close();
        */
        
        
        fr.close();
      }
      catch (IOException ex)
      {
      } 
   %>
  </PRE>

    </DIV>
   </TD></TR>
   </TABLE>

 <% } %>
  
  </DIV> <!-- end page body -->
  
  <jsp:include page="footer.html" flush="true"/>

</BODY>
</HTML>
