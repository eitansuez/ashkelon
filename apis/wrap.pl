#!/usr/bin/perl

my $package_list = $ARGV[0]; 
if (!$package_list)
{
  print "Usage: perl wrap.pl <package-list-file> \n";
  exit(0);
}

open(PKGLIST, $package_list);

while (<PKGLIST>)
{
  chomp;
  print "<package>$_</package>\n";
}


close(PKGLIST);

