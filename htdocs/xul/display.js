/**
 * March 2001
 * Javascript style display/visibility logic
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 *
 * Requirements:
 *  including document must have the following body signature:
 *     <BODY onClick="expandCollapse();">
 *  in addition to whatever else onloads or onunloads
 */

var visinfo = new Object();
visinfo['display'] = new Array("", "none");
visinfo['visibility'] = new Array("visible", "hidden");

function isVisible(tagid, displaystyle, isargobject)
{
  displaystyle = eraseBlank(displaystyle, "display");
  try {
    var target = (isargobject) ? tagid : getElementById(tagid);
    return (target.style[displaystyle] == visinfo[displaystyle][0]);
  } catch (ex) {
    alert("Error in isVisible: "+ex.description);
  }
}

function setVisible(tagid, yesno, displaystyle, isargobject)
{
  try {
    displaystyle = eraseBlank(displaystyle, "display");
    var target = (isargobject) ? tagid : getElementById(tagid);
    var x = (yesno) ? 0 : 1;
    target.style[displaystyle] = visinfo[displaystyle][x];
  } catch (ex) {}
}

function toggleVisibility(tagid, displaystyle, isargobject)
{
  try {
    var wasvis = isVisible(tagid, displaystyle, isargobject);
    setVisible(tagid, !wasvis, displaystyle, isargobject);
    target.style.zIndex = (wasvis) ? 0 : 1;
    return wasvis;
  } catch (ex) {}
}



function expandCollapse(evt)
{
  var source;
  if (IE)
    source = event.srcElement;
  else
    source = evt.target;

  if (source.className != "collapsible")
    return;

  var tagid = source.getAttribute("CHILD", false);
  
  var children = getElementsById(tagid);
  if (children == null) { // i.e. a leaf
    return;
  }
  
  var isvisible = "";
  if (children.style == null) // it's an array;
  {
    for (var i=0; i<children.length; i++)
    {
      isvisible = toggleDisplay(children[i], 'display', true);
    }
  }
  else
  {
    isvisible = toggleDisplay(children, 'display', true);
  }
  source.style.listStyleImage = (isvisible == "none") ? "url('images/minus.gif')" : "url('images/plus.gif')";
}


function toggleDisplay(tagid, isobject)
{
  try {
    var target;
    if (isobject)
    {
      target = tagid;
    }
    else
    {
      target = getElementById(tagid);
    }
    var isvisible = target.style.display;
    target.style.display = (isvisible == "none") ? "" : "none";
    return isvisible;
  } catch (ex) {}
}


