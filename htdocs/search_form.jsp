<%@ page info="component" import="org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<link rel="stylesheet" type="text/css" href="l2_hdr.css" />

<script>
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
</script>

<style>
table table
{
  border: thin solid gray;
  margin-left: 20px;
}
tr
{
 background-color: white;
}
tr.even
{
 background-color: beige;
}
</style>

<table cellpadding="1" cellspacing="0" width="600" style="empty-cells: show;">

<thead>
<tr>
<td class="tab_pad" width="20px;">&nbsp;</td>
<td id="tab" class="tab_selected_tab" onClick="doTab(this);" FOR="directSearch">
Direct Search
</td>
<td class="tab_buffer" width="5px;">&nbsp;</td>
<td id="tab" class="tab_tab" onClick="doTab(this);" FOR="powerSearch">
Power Search
</td>
<td class="tab_pad" width="20px;">&nbsp;</td>
</tr>
</thead>
<tbody>
<tr>
<td colspan="5" style="background-color: #92A6F5; border: 1px solid black; border-top: 0px none black; padding: 10 px;">

<div id="directSearch" style="padding: 10px; padding-right: 30px;">

<form NAME="search_form" METHOD="POST" ACTION="search.do">
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="true" />

<div style="padding: 5px;">
  <label FOR="srch_type">Search: </label>
  <INPUT TYPE="RADIO" ID="cls_srch" NAME="srch_type" VALUE="class" ACCESSKEY="C" CHECKED>
    <label FOR="cls_srch"><U>C</U>lasses</label>
  <INPUT TYPE="RADIO" ID="mmb_srch" NAME="srch_type" VALUE="member" ACCESSKEY="M">
    <label FOR="mmb_srch"><U>M</U>embers</label>
</div>

<div style="padding: 5px;">
  <label FOR="searchField" ACCESSKEY="N"><U>N</U>ame: </label>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">

  <button NAME="b" ACCESSKEY="G" TYPE="SUBMIT">&nbsp;<U>G</U>o&nbsp;</button>
  
</div>


<P STYLE="font-size: 8pt;">Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>
</form>

</div>


<div id="powerSearch" style="display: none; padding: 10px;">

 <div style="padding: 5px;">
  <label FOR="toggle_srch_type">Search: </label>
  <INPUT TYPE="RADIO" ID="cls_srch" NAME="toggle_srch_type" VALUE="class"  ACCESSKEY="C" CHECKED onClick="toggleForm('cls_type_display')">
    <label FOR="cls_srch"><U>C</U>lasses</label>
  <INPUT TYPE="RADIO" ID="mmb_srch" NAME="toggle_srch_type" VALUE="member" ACCESSKEY="M" onClick="toggleForm('member_type_display');">
    <label FOR="mmb_srch"><U>M</U>embers</label>
 </div>

 <div style="padding: 10px;">
   <I>Use any combination of the following selectors to define your advanced query.  Check or uncheck the box on the left to include or exclude a specific criterion in or from a query.</I>
 </div>

<div id="cls_type_display">
<form NAME="advanced_class_search_form" METHOD="POST" ACTION="search.do" style="display: inline;">
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="false" />
<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="class" />
  <table cellspacing="0" cellpadding="3" rules="rows">
<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="class_type" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="class_type"><B>Type:</B> </LABEL>
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="O" VALUE="1" CHECKED><U>O</U>rdinary Classes
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="I" VALUE="2"><U>I</U>nterfaces
  <INPUT TYPE="RADIO" NAME="class_type" ACCESSKEY="X" VALUE="3">E<U>x</U>ceptions
  <INPUT TYPE="RADIO" NAME="class_type" VALUE="4">Error classes
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED onClick="update(this);"></TD>
  <TD>
  <LABEL FOR="searchField" ACCESSKEY="N"><B><U>N</U>ame</B>: </LABEL>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="deprecated"><B>Deprecated</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="abstract"><B>Abstract</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>In Package:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="author" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>Author:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="author" SIZE="25" MAXLENGTH="40">
  </TD>
</tr>
</table>

 </form>

</div>
<div ID="member_type_display" STYLE="display: none;">

<form NAME="advanced_member_search_form" METHOD="POST" ACTION="search.do" style="display: inline;">
<INPUT TYPE="HIDDEN" NAME="simple" VALUE="false" />
<INPUT TYPE="HIDDEN" NAME="srch_type" VALUE="member" />
<table cellspacing="0" cellpadding="3" rules="rows">
<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="member_type" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="class_type"><B>Type:</B> </LABEL>
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="I" VALUE="1" CHECKED>F<U>i</U>eld
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="S" VALUE="2">Con<U>s</U>tructor
  <INPUT TYPE="RADIO" NAME="member_type" ACCESSKEY="O" VALUE="3">Meth<U>o</U>d
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="searchField" CHECKED onClick="update(this);"></TD>
  <TD>
  <LABEL FOR="searchField" ACCESSKEY="N"><B><U>N</U>ame</B>: </LABEL>
  <INPUT TYPE="TEXT" ID="searchField" NAME="searchField" SIZE="40" MAXLENGTH="100">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="deprecated" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="deprecated"><B>Deprecated</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="deprecated" VALUE="1">
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="static" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="static"><B>Static</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="static" VALUE="1">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="final" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="final"><B>Final</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="final" VALUE="1">
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="package_name" onClick="update(this)"></TD>
  <TD>
  <LABEL><B>In Package:</B> </LABEL>
  <INPUT TYPE="TEXT" NAME="package_name" SIZE="40" MAXLENGTH="60">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="synchronized" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="synchronized"><B>Synchronized</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="synchronized" VALUE="1">
  </TD>
</tr>

<tr class="even">
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="native" onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="native"><B>Native</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="native" VALUE="1">
  </TD>
</tr>

<tr>
  <TD><INPUT TYPE="CHECKBOX" NAME="selector" value="abstract" CHECKED onClick="update(this)"></TD>
  <TD>
  <LABEL FOR="abstract"><B>Abstract</B>: </LABEL>
  <INPUT TYPE="CHECKBOX" NAME="abstract" VALUE="1">
  </TD>
</tr>

</table>

 </form>

</div>

<div style="margin-left: 20px; margin-top: 15px;">
  <button NAME="b" ACCESSKEY="G" onClick="submitform();">&nbsp;<U>G</U>o&nbsp;</button>
</div>

<P STYLE="font-size: 8pt;">Names may be entered either in fully-qualified or non-qualified form.  Usage of asterisks are supported, as in "String*" or "getIndex*"
</P>

</div>


</td>
</tr>
</tbody>
</table>


<script>
  init();
</script>


