<%@ page info="component" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%-- SECTION: COMMENTS/DOCUMENTATION
Copyright UptoData Inc 2001
Author: Eitan Suez
Date: March 2001
--%>

<%-- SECTION: COMPONENT CODE --%>
<%
%>

<%-- SECTION: COMPONENT STYLES --%>
<LINK REL="stylesheet" TYPE="text/css" HREF="l2_hdr.css"></LINK>
<STYLE TYPE="text/css">
</STYLE>

<%-- SECTION: COMPONENT BEHAVIOR (JAVASCRIPT) --%>
<SCRIPT>
  function init()
  {
    document.search_form.searchField.focus();
    doTab(getElementsById("tab")[0]);
    init_disabled();    
  }
  
  function init_disabled()
  {
    var whichform = document.advanced_member_search_form;
    if (getElementById('cls_type_display').style.display == "")
      whichform = document.advanced_class_search_form;
    var selector = whichform.selector;
    for (var i=0; i<selector.length; i++)
      update(selector[i]);
  }
  
  function submitform()
  {
    var which = document.advanced_member_search_form;
    if (getElementById('cls_type_display').style.display == "")
      which = document.advanced_class_search_form;
    which.submit();
  }
  
  function toggleForm(which)
  {
    if (which == "cls_type_display")
    {
      getElementById('cls_type_display').style.display = "";
      getElementById('member_type_display').style.display = "none";
    }
    else
    {
      getElementById('cls_type_display').style.display = "none";
      getElementById('member_type_display').style.display = "";
    }
    init_disabled();
  }

  function doTab(ref)
  {
    var tabs = getElementsById("tab");
    var forval;
    for (var i=0; i<tabs.length; i++)
    {
      if (tabs[i].className == "tab_selected_tab")
      {
        tabs[i].className = "tab_tab";
        forval = tabs[i].getAttribute("FOR");
        getElementById(forval).style.display = "none";
      }
    }
    ref.className = "tab_selected_tab";
    forval = ref.getAttribute("FOR");
    getElementById(forval).style.display = "";
  }
  
  function update(ref)
  {
    ref.parentNode.nextSibling.disabled=((ref.checked) ? false : true)
  }
</SCRIPT>

<%-- SECTION: COMPONENT TEMPLATE --%> 


<table class="tab" border="1" cellpadding="0" cellspacing="0" width="600">

<thead>
<tr>
<td id="tab" class="tab_selected_tab" onClick="doTab(this);" FOR="directSearch">
Direct Search
</td>
<td class="tab_buffer"></td>
<td id="tab" class="tab_tab" onClick="doTab(this);" FOR="powerSearch">
Power Search
</td>
<td class="tab_pad"></td>
</tr>
</thead>
<tbody>
<tr>
<td colspan="4" style="border-right: 1 solid black; border-left: 1 solid black; padding: 10 px;">

<DIV ID="directSearch">

<FORM NAME="search_form" METHOD="POST" ACTION="index.html">
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="search" />
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="true" />
<TABLE>
<TR>
  <TD>
  <LABEL FOR="srch_type">Search: </LABEL>
  <INPUT TYPE="RADIO" ID="cls_srch" NAME="srch_type" VALUE="class" ACCESSKEY="C" CHECKED>
    <LABEL FOR="cls_srch"><U>C</U>lasses</LABEL>
  <INPUT TYPE="RADIO" ID="mmb_srch" NAME="srch_type" VALUE="member" ACCESSKEY="M">
    <LABEL FOR="mmb_srch"><U>M</U>embers</LABEL>
  </TD>
</TR>

<TR>
  <TD>
  <LABEL FOR="searchField" ACCESSKEY="N"><U>N</U>ame <SUP>1</SUP>: </LABEL>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD ALIGN="RIGHT">
  <BUTTON NAME="b" ACCESSKEY="G" TYPE="SUBMIT">&nbsp;<U>G</U>o&nbsp;</BUTTON>
  </TD>
</TR>
</TABLE>

<P STYLE="font-size: 8 pt;">[1]: Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>
</FORM>

</DIV>


<DIV ID="powerSearch" STYLE="display: none;">

<TABLE>
<THEAD>
<TR>
  <TD>
  <LABEL FOR="toggle_srch_type">Search: </LABEL>
  <INPUT TYPE="RADIO" ID="cls_srch" NAME="toggle_srch_type" VALUE="class"  ACCESSKEY="C" CHECKED onClick="toggleForm('cls_type_display')">
    <LABEL FOR="cls_srch"><U>C</U>lasses</LABEL>
  <INPUT TYPE="RADIO" ID="mmb_srch" NAME="toggle_srch_type" VALUE="member" ACCESSKEY="M" onClick="toggleForm('member_type_display');">
    <LABEL FOR="mmb_srch"><U>M</U>embers</LABEL>
  </TD>
</TR>

<TR>
  <TD>
  <BR>
  <I>Use any combination of the following selectors to define your advanced query.  Check or uncheck the box on the left to include or exclude a specific criterion in or from a query.</I>
  <BR><BR>
  </TD>
</TR>
</THEAD>

<TBODY><TR><TD>

<DIV ID="cls_type_display">
<FORM NAME="advanced_class_search_form" METHOD="POST" ACTION="index.html">
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="search" />
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="false" />
<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="class" />
  <TABLE border="1" cellspacing="0" cellpadding="3">
<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="class_type" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="class_type"><B>Type:</B> </LABEL>
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="O" VALUE="1" CHECKED><U>O</U>rdinary Classes
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="I" VALUE="2"><U>I</U>nterfaces
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="X" VALUE="3">E<U>x</U>ceptions
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="4">Error classes
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED onClick="update(this);"></TD>
  <TD>
  <LABEL FOR="searchField" ACCESSKEY="N"><B><U>N</U>ame</B> <SUP>1</SUP>: </LABEL>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="deprecated"><B>Deprecated</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="abstract"><B>Abstract</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>In Package:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="author" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>Author:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="author" SIZE="25" MAXLENGTH="40">
  </TD>
</TR>
</TABLE>

 </FORM>

</DIV>
<DIV ID="member_type_display" STYLE="display: none;">

<FORM NAME="advanced_member_search_form" METHOD="POST" ACTION="index.html">
<INPUT TYPE="HIDDEN" NAME="cmd" VALUE="search" />
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="false" />
<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="member" />
  <TABLE border="1" cellspacing="0" cellpadding="3">
<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="member_type" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="class_type"><B>Type:</B> </LABEL>
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="I" VALUE="1" CHECKED>F<U>i</U>eld
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="S" VALUE="2">Con<U>s</U>tructor
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="O" VALUE="3">Meth<U>o</U>d
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED onClick="update(this);"></TD>
  <TD>
  <LABEL FOR="searchField" ACCESSKEY="N"><B><U>N</U>ame</B> <SUP>1</SUP>: </LABEL>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="deprecated"><B>Deprecated</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="static" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="static"><B>Static</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="static" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="final" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="final"><B>Final</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="final" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>In Package:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="synchronized" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="synchronized"><B>Synchronized</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="synchronized" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="native" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="native"><B>Native</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="native" VALUE="1">
  </TD>
</TR>

<TR>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="abstract"><B>Abstract</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</TR>

</TABLE>

 </FORM>


</DIV>

</TD></TR></TBODY>

<TFOOT>
<TR>
  <TD ALIGN="RIGHT">
  <BR>
  <BUTTON NAME="b" ACCESSKEY="G" onClick="submitform();">&nbsp;<U>G</U>o&nbsp;</BUTTON>
  </TD>
</TR>
</TFOOT>

</TABLE>

<P STYLE="font-size: 8 pt;">[1]: Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>

</DIV>


</td>
</tr>
</tbody>
</table>


<SCRIPT>
  init();
</SCRIPT>

