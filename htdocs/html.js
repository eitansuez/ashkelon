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

function cleanTitlesOld(tag)
{
    if (!tag)
      tag = "a";
    var hrefs = getElementsByTagName(tag);
    var href, attribute, tag, cleanedtext;
    
    for (var i=0; i<hrefs.length; i++)
    {
      href = hrefs[i];
      attribute = href.getAttribute("title");
      tag = document.createElement("span");
      tag.innerHTML=attribute;
      cleanedtext = tag.innerHTML;
      hrefs[i].setAttribute("title", cleanedtext, false);
    }
}

function cleanTitles(tagname)
{
   if (!tagname) tagname = "a";
   var tags = getElementsByTagName(tagname);
   for (var i=0; i<tags.length; i++)
     cleanSingleTitle(tags[i]);
}

function cleanSingleTitle(tag)
{
  var title = tag.getAttribute("title");
  if (!title) return;
  title = title.replace("\n", "");
  var startIdx = 0;
  while (true)
  {
    var idx1 = title.indexOf("<", startIdx);
    if (idx1 >= startIdx)
    {
      var idx2 = title.indexOf(">", idx1);
      if (idx2 > idx1)
      {
        title = title.substring(0, idx1) + title.substring(idx2 + 1);
        startIdx = idx1 + 1;
      }
      else { break; } // end inner if
    }
    else { break; } // end outer if
  } // end while
  tag.setAttribute("title", title);
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
