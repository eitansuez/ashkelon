/**
 * March 2001
 * Javascript screen/window logic (use for popping up dialogs)
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 *
 * Requirements:
 *  none.
 */
 
 var bareDocTemplate = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" + 
    "<HTML>\n" + 
    "<HEAD>\n" + 
    " <TITLE></TITLE>\n" +
    " <LINK REL=\"stylesheet\" TYPE=\"text/css\" HREF=\"global.css\"></LINK>\n" +
    " <SCRIPT>\n" +
    "   self.childName = \"\";\n" + 
    " </SCRIPT>\n" +
    "</HEAD>\n" +
    "\n" +
    "<BODY onBeforeUnload=\"window.opener.onChildUnload(self);\">\n" +
    "\n" +
    "<DIV ID=\"winbody\" STYLE=\"position:absolute; left:0; top:0;\">\n" + 
    "</DIV>\n" +
    "\n" +
    "</BODY>\n" +
    "\n" +
    "</HTML>\n";

var childWins = new Object();

function onChildUnload(child)
{
  for (var childname in childWins)
  {
    if (childWins[childname] == child)
    {
      childWins[childname] = null;
    }
  }
}

function openChild(winname, target, opts)
{
  try
  {
    if (childWins[winname] == null)
    {
      var defaultopts = "width=200,height=350,resizable,scrollbars=auto";
      if (opts == "" || opts == null)
      {
        opts = defaultopts;
      }
      childWins[winname] = window.open("", winname, opts, true);
      winPack(childWins[winname]);
    }
    
    childWins[winname].document.writeln(bareDocTemplate);
    var doc = childWins[winname].document;
    doc.title = winname;
    var winbody = doc.all("winbody");
    winbody.innerHTML = target.innerHTML;
  } 
  catch (ex)
  {
    //alert("Exception: "+ex.description);
    if (childWins[winname] != null)
    {
      closeChildWin(winname);
    }
  }
}

function isChildWinOpen(childname)
{
  return (childWins[childname] != null);
}

function closeChildWin(winname)
{
   childWins[winname].close();
   childWins[winname] = null;
}


function getWindowPos()
{
  var loc = new Array(window.screenLeft, window.screenTop);
  return loc;
}


function getAbsoluteOffsets(element, relativeToPage)
{
  var loc = new Array(element.offsetLeft, element.offsetTop);
  if (element.offsetParent == null)
  {
    if (!relativeToPage)
    {
      windowLoc = getWindowPos();
      loc[0] += windowLoc[0];
      loc[1] += windowLoc[1];
    }
    return loc;
  }
  else
  {
    var parentLoc = getAbsoluteOffsets(element.offsetParent);
    loc[0] += parentLoc[0];
    loc[1] += parentLoc[1];
    return loc;
  }
}


function winPack(win)
{
  var winbody = win.document.body;
  win.resizeTo(winbody.offsetWidth , winbody.offsetHeight);
}

