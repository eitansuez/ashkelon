/**
 * March 2001
 * Global javascript file for common library functions
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 */

var IE = (document.all != null);
var ELEMENT_TYPE = 1; // DOM Node Type (as in element, attribute, text, etc..)


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

Object.prototype.getElementsByClassName = function ( className )
{
    var matches = new Array();
    var alltags = this.getElementsByTagName( "*" );
    for (var i=0; i<alltags.length; i++)
    {
      if (contains(alltags[i].className.toLowerCase(), className.toLowerCase()))
      {
        matches[matches.length] = alltags[i];
      }
    }
    return matches;
}

function addClass(node, className)
{
  if (!contains(node.className, className))
  {
    node.className += " " + className;
  }
}
function removeClass(node, className)
{
  if (contains(node.className, className))
  {
    node.className = strip(node.className, className);
  }
}

function strip(text, part)
{
  var idx = text.indexOf(part);
  if (idx < 0) return text;
  return text.substring(0, idx) + text.substring(idx + part.length);
}

function getEvtTarget(evt)
{
  if (document.all) return window.event.srcElement; // for M$IE
  if (evt.target.nodeType == Node.TEXT_NODE) return evt.target.parentNode; // for Safari
  return evt.target;
}

