<%@ page info="component" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<SCRIPT>
function toggleLegend()
{
  if (isChildWinOpen("Legend"))
    closeChildWin("Legend");
  else
    showLegend();
}
  
function showLegend()
{
  var opts = "width=150,height=400,resizable,scrollbars=auto";
  try
  {
    var legend = getElementById("legend");
    var loc = getAbsoluteOffsets(legend);
    //alert("left: "+loc[0]+"; top: "+loc[1]);
    opts += ",left="+loc[0]+",top="+loc[1];
  } catch (ex)
  {
    alert("Exception: "+ex);
  }
  openChild('Legend', getElementById('legend'), opts);
}
</SCRIPT>


<div style="position:relative; visibility: hidden">
  <div id="legend" style="position:absolute; left: -100; visibility: hidden;">

<FIELDSET STYLE="padding: 5px; border-color: #000000;" WIDTH="200" CLASS="legend">
<LEGEND>Element Legend</LEGEND>

<FIELDSET STYLE="padding: 5px;">
<LEGEND>Packages</LEGEND>
<TABLE>
<TR>
  <TD>
    <HR CLASS="package" SIZE="12" WIDTH="12">
  </TD>
  <TD>
    Package
  </TD>
</TR>
</TABLE>
</FIELDSET>

<FIELDSET STYLE="padding: 5px;">
<LEGEND>Class Types</LEGEND>
<TABLE BORDER="0">
<TR>
<TD>
<HR CLASS="ordinaryClass" SIZE="12" WIDTH="12">
</TD>
<TD>
Class
</TD>
</TR>
<TR>
<TD>
<HR CLASS="interface" SIZE="12" WIDTH="12">
</TD>
<TD>
Interface
</TD>
</TR>
<TR>
<TD>
<HR CLASS="exception" SIZE="12" WIDTH="12">
</TD>
<TD>
Exception
</TD>
</TR>
<TR>
<TD>
<HR CLASS="errorClass" SIZE="12" WIDTH="12">
</TD>
<TD>
Error
</TD>
</TR>
</TABLE>
</FIELDSET>

<FIELDSET STYLE="padding: 5px;">
<LEGEND>Members</LEGEND>
<TABLE BORDER="0">
<TR>
  <TD>
    <HR CLASS="method" SIZE="12" WIDTH="12">
  </TD>
  <TD>
    Method
  </TD>
</TR>
<TR>
  <TD>
    <HR CLASS="constructor" SIZE="12" WIDTH="12">
  </TD>
  <TD>
    Constructor
  </TD>
</TR>
<TR>
  <TD>
    <HR CLASS="field" SIZE="12" WIDTH="12">
  </TD>
  <TD>
    Field
  </TD>
</TR>
</TABLE>
</FIELDSET>


</FIELDSET>

  </div>
</div>

