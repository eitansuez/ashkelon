/**
 * March 2001
 * Javascript cookie logic
 *
 * Copyright 2001, UptoData Inc.
 * Author: Eitan Suez
 *
 * Requirements:
 *  including document must have the following body signature:
 *    <BODY onLoad="loadCookies();" onUnload="saveCookies();">
 *  in addition to whatever else onloads or onunloads
 */

var document_cookies;

function loadCookies()
{
  document_cookies = new Object();
  var cookie = document.cookie;
  //alert("loading cookies: "+cookie);
  if (cookie != null)
  {
    var cookies = cookie.split("; ");
    var pair;
    for (var i=0; i<cookies.length; i++)
    {
      //alert("cookie is: "+cookies[i]);
      if (cookies[i] == null || cookies[i] == "")
        continue;
      //alert("about to split: "+cookies[i]);
      pair = cookies[i].split("=");
      //alert("cookies[i] split to: "+pair[0]+" and "+pair[1]);
      if (pair[0] == null || pair[0] == "")
        continue;
      if (pair[1] == null || pair[1] == "")
        continue;
        
      //alert("loading cookie: "+pair[0]+"="+unescape(pair[1]));
      //alert("pairs [0] is: ."+pair[0]+".");
      document_cookies[pair[0]] = unescape(pair[1]);
    }
  }
  //debug(document_cookies);
}

function saveCookies()
{
  var pairs = new Array();
  var cookie = "";
  for (var name in document_cookies)
  {
    if (name == null || name == "")
     continue;
    cookie = name + "=" + escape(document_cookies[name]) + ";";
    document.cookie = cookie;
    //alert("saving cookie: " + cookie);
  }
}

function setCookie(name, value)
{
  document_cookies[name] = value;
  //alert("set cookie "+name+" to "+value);
}

function readCookie(name)
{
  var value = document_cookies[name];
  //alert("read cookie "+name+"; value is: "+value);
  return value;
}

function resetCookies()
{
  for (var x in document_cookies)
  {
    setCookie(x, " ; expires=-1");
  }
  saveCookies();
  loadCookies();
}

function showCookies()
{
  var displaystr = "";
  for (var x in document_cookies)
  {
    displaystr += x+"="+document_cookies[x]+";";
  }
  alert(displaystr);
}


