function navto(cmd, args)
{
  var myhref = "index.html?cmd=" + cmd;
  if (args)
  {
     myhref += "&" + args;
  }
  location.href = myhref;
}
