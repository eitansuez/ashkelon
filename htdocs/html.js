/**
 * March 2001
 * Javascript html utilities
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 *
 * Requirements:
 *  none.
 */

function cleanTitles(tag)
{
    if (!tag)
      tag = "A";
    var hrefs = getElementsByTagName(tag);
    var href, attribute, tag, cleanedtext;
    
    for (var i=0; i<hrefs.length; i++)
    {
      href = hrefs[i];
      attribute = href.getAttribute("TITLE");
      tag = document.createElement("SPAN");
      tag.innerHTML=attribute;
      cleanedtext = tag.innerText;
      hrefs[i].setAttribute("TITLE", cleanedtext, false);
    }
}

/**
 * set up a table with a tbody.  each row in the tbody
 * must contain an id and class attribute containing space separated list of values
 * to filter by.  filters all rows not containing attr in class attribute
 * @param rowid the id of the row (assumption is id's are unique by appending an index
 * @param the string to filter the rows by
 */
function filterRows(rowid, targetmod, divid)
{
  var div = getElementById(divid);
  var tb = div.getElementsByTagName("TABLE")[0].tBodies[0];
  var rows = tb.childNodes;
  //var rows = div.getElementById(rowid+"0").parentElement.children;
  
  var mods;
  for (var i=0; i<rows.length; i++)
  {
    mods = rows[i].className;
    if(!defined(mods)) continue;
    if (contains(mods, targetmod) || (targetmod=="reset"))
    {
      rows[i].style.display = "";
    } else
    {
      rows[i].style.display = "none";
    }
  }
}
