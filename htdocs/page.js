/**
 * Common page-related javascript functions
 *
 * @author Eitan Suez
 */

  function configureExpandCollapse()
  {
    if (IE)
      document.onclick = expandCollapse;
    else
      document.addEventListener("click", expandCollapse, false);
  }

  function enableTabs(cmds)
  {
    for (var i=0; i<cmds.length; i++)
    {
      var tag = getElementById(cmds[i]+"_tabchild");
      tag.disabled = false;
    }
  }

  function initTabs(tagid, title)
  {
    var context = readCookie("pagecontext");
    if (location.href == context)
    {  // must be a reload or a back button press
      togglePage(readCookie("tabid"), title);
    }
    else
    {
      togglePage(tagid, title);
    }
    enableTabs(cmds);
  }


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
        setCookie("pagecontext",location.href);
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

