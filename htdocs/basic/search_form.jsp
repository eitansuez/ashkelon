<%@ page info="component" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*,java.util.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%
 String searchtype = (String) request.getParameter("searchtype");
 String srch_type = (String) request.getParameter("srch_type");
 %>

<% if (StringUtils.isBlank(searchtype) || "direct".equals(searchtype)) { %>
  <B>Direct Search</B> | <A HREF="index.html?cmd=search&searchtype=advanced">Power Search</A>
<% } else { %>
  <A HREF="index.html?cmd=search&searchtype=direct">Direct Search</A> | <B>Power Search</B>
<% } %>

<BR>


<%-- SECTION: COMPONENT TEMPLATE --%> 


<% if (StringUtils.isBlank(searchtype) || "direct".equals(searchtype)) { %>

<FORM NAME="search_form" METHOD="POST" ACTION="index.html">
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="search">
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="true">
<TABLE>
<TR>
  <TD>
  Search:
  <INPUT TYPE="RADIO" ID="cls_srch" NAME="srch_type" VALUE="class" CHECKED>
    Classes
  <INPUT TYPE="RADIO" ID="mmb_srch" NAME="srch_type" VALUE="member">
    Members
  </TD>
</TR>

<TR>
  <TD>
  Name <SUP>1</SUP>:
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD ALIGN="RIGHT">
  <INPUT TYPE="SUBMIT" NAME="b" VALUE=" Go ">
  </TD>
</TR>
</TABLE>

<P STYLE="font-size: 8 pt;">[1]: Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>
</FORM>

<% } else { %>

<BR>

Search:
<% if (StringUtils.isBlank(srch_type) || "class".equals(srch_type)) { %>
  <B>Classes</B>
  or
  <A HREF="index.html?cmd=search&searchtype=advanced&srch_type=member">Members</A>
<% } else { %>
  <A HREF="index.html?cmd=search&searchtype=advanced&srch_type=class">Classes</A>
  or
  <B>Members</B>
<% } %>




<TABLE>
<TR>
  <TD>
  <BR>
  <I>Use any combination of the following selectors to define your advanced query.  Check or uncheck the box on the left to include or exclude a specific criterion in or from a query.</I>
  <BR><BR>
  </TD>
</TR>


<TR><TD>

<FORM NAME="advanced_class_search_form" METHOD="POST" ACTION="index.html">
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="search">
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="false" />

<% if (StringUtils.isBlank(srch_type) || "class".equals(srch_type)) { %>

<DIV ID="cls_type_display">
<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="class" />
  <TABLE border="1" cellspacing="0" cellpadding="3">
<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="class_type" CHECKED></TD>
  <TD>
  <B>Type:</B>
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="1" CHECKED>Ordinary Classes
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="2">Interfaces
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="3">Exceptions
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="4">Error classes
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED></TD>
  <TD>
  <B>Name</B> <SUP>1</SUP>:
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated"></TD>
  <TD>
  <B>Deprecated</B>:
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED></TD>
  <TD>
  <B>Abstract</B>:
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name"></TD>
  <TD>
  <B>In Package:</B>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="author"></TD>
  <TD>
  <B>Author:</B>
  <INPUT TYPE="TEXT" NAME="author" SIZE="25" MAXLENGTH="40">
  </TD>
</TR>
</TABLE>

</DIV>

<% } else { %>

<DIV ID="member_type_display">

<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="member" />
  <TABLE border="1" cellspacing="0" cellpadding="3">
<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="member_type" CHECKED></TD>
  <TD>
  <B>Type:</B>
  <INPUT TYPE="RADIO" NAME="member_type" VALUE="1" CHECKED>Field
  <INPUT TYPE="RADIO" NAME="member_type" VALUE="2">Constructor
  <INPUT TYPE="RADIO" NAME="member_type" VALUE="3">Method
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED></TD>
  <TD>
  <B>Name</B> <SUP>1</SUP>:
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated"></TD>
  <TD>
  <B>Deprecated</B>:
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="static" CHECKED></TD>
  <TD>
  <B>Static</B>:
  <INPUT TYPE="CHECKBOX" NAME="static" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="final"></TD>
  <TD>
  <B>Final</B>:
  <INPUT TYPE="CHECKBOX" NAME="final" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name"></TD>
  <TD>
  <B>In Package:</B>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="synchronized"></TD>
  <TD>
  <B>Synchronized</B>:
  <INPUT TYPE="CHECKBOX" NAME="synchronized" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="native"></TD>
  <TD>
  <B>Native</B>:
  <INPUT TYPE="CHECKBOX" NAME="native" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED></TD>
  <TD>
  <B>Abstract</B>:
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</TR>

</TABLE>

</DIV>

<% } %>

</TD></TR>


<TR>
  <TD ALIGN="RIGHT">
  <BR>
  <INPUT TYPE="SUBMIT" NAME="b" VALUE=" Go ">
  </TD>
</TR>

</FORM>

</TABLE>


<P STYLE="font-size: 8 pt;">[1]: Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>

<% } %>

