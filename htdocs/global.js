/**
 * March 2001
 * Global javascript file for common library functions
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 */

var IE = (document.all != null);

function eraseBlank(ref, alternate)
{
  if (ref == null || ref == "")
  {
    if (alternate == null)
      return "";
    else
      return alternate;
  }
  else
    return ref;
}

 
function debug(obj)
{
    var props = "";
    for (var property in obj)
    {
      props += property + ": " + obj[property] + "\n";
    }
    props = "Properties of object: \n\t" + props;
    alert(props);
}


function contains(text, part)
{
  return (text.indexOf(part)>-1);
}


function defined(x)
{
  var type = typeof x;
  return !( x == null || x == "" || type == "undefined");
}


function getElementsById(id)
{
  if (IE)
  {
    return document.all[id];
  }
  var all = document.getElementsByTagName("*");
  var match = new Array();
  for (var i=0; i<all.length; i++)
  {
    if (all[i].id == id)
    {
      match.push(all[i]);
    }
  }
  return match;
}

function getElementById(id)
{
  if (IE)
  {
    return document.all[id];
  }
  return document.getElementById(id);
}

function getElementsByTagName(tagname)
{
  if (IE)
  {
    return document.all.tags(tagname);
  }
  return document.getElementsByTagName(tagname);
}
