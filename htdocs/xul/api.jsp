<%@ page info="main api view" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.text.*"  contentType="application/vnd.mozilla.xul+xml" %><?xml version="1.0"?>
<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href="xul/global.css" type="text/css"?>

<%
  API api = (API) request.getAttribute("api");
  SimpleDateFormat df = new SimpleDateFormat("MMMM dd yyyy");
  String rdate_fmt= df.format(api.getReleaseDate());
 %>

<window
  xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul"
  xmlns:html="http://www.w3.org/1999/xhtml"
  title="Ashkelon: <%=api.getName()%>" orient="vertical">

  <jsp:include page="header.jsp" flush="true"/>
  
  <vbox id="body" flex="1">

   <hbox flex="1">
    <vbox flex="1">
  <description style="font-size: 14pt; font-weight: bold;"><%=api.getName()%></description>
  <description>Publisher:  <%=api.getPublisher()%></description>
  <description>Version: <%=api.getVersion()%></description>
  <description>Release Date: <%=rdate_fmt%></description>
  <description>
    <html:a href="<%=api.getDownloadURL()%>" target="_new">API Home</html:a>
  </description>
    </vbox>
    <description><%= api.getDescription() %></description>
   </hbox>

<%--
<BR>

<TABLE CELLPADDING="3" CELLSPACING="0" ALIGN="CENTER" STYLE="border: 1px solid black;">
<CAPTION CLASS="api"><%=api.getName()%> API Packages</CAPTION>
<TBODY>
<TR>
    <%
     List pkgs = api.getPackages();
     JPackage pkg;
     
     for (int i=0; i<pkgs.size(); i++)
     {
          pkg = (JPackage) pkgs.get(i);
          %>
        <TR <% if (i%2==1) { %> BGCOLOR="beige" <% } %>>
          <TD VALIGN="TOP">
            <A HREF="index.html?cmd=pkg.main&pkg_id=<%=pkg.getId()%>"><%=pkg.getName()%></A>
          </TD>
          <TD VALIGN="TOP">
            <%= pkg.getSummaryDescription() %>
          </TD>
        </TR>
  <%
     }
   %>
</TR>
</TBODY>
</TABLE>
--%>

</vbox>


  <jsp:include page="footer.html" flush="true"/>

  
</window>
