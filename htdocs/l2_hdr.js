/**
 * March 2001
 * Level 2 Header Logic
 *
 * Copyright 2001, UptoData Inc.
 * By: Eitan Suez
 */

function togglePage(tagid, title)
{
  if (isVisible(tagid))  // no need to toggle visibility if already selected
    return;

  for (var i=0; i<cmds.length; i++)
  {
    var tag = getElementById(cmds[i]+"_tabchild");
    if (cmds[i] == tagid)
    {
      setVisible(cmds[i], true)
      tag.className = "tab_selected_tab";
      setCookie("pagecontext",location.search);
      setCookie("tabid",tagid);
    }
    else
    {
      setVisible(cmds[i], false);
      tag.className = "tab_tab";
    }
  }

  document.title = title;
}

