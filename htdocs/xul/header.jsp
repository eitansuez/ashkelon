<%@ page info="header" %>

 <hbox id="header">
  <toolbox flex="1">
   <toolbar>
    <toolbarbutton label="APIs" accesskey="a" oncommand="navto('apis');" />
    <toolbarbutton label="Packages" accesskey="p" oncommand="navto('pkg');" />
    <toolbarbutton label="Search" accesskey="s" oncommand="navto('search');" />
    <toolbarbutton label="Index" accesskey="i" oncommand="navto('idx');" />
    <toolbarbutton label="Stats" accesskey="t" oncommand="navto('stats.general');" />
    <spacer flex="1" />
    <toolbarbutton label="Contact" accesskey="c" oncommand="navto('contact');" />
    <toolbarbutton label="Settings" accesskey="g" oncommand="navto('config');" />
    <toolbarbutton label="Help" accesskey="h" oncommand="navto('help');" />
   </toolbar>
  </toolbox>
 </hbox>

