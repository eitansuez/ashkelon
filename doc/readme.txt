5/7/2002
By Eitan Suez

README for ashkelon
===================

1. database support:  ashkelon currently supports mysql,
     postgres, and oracle.  this app was designed to make adding
     support for other sql dialects relatively painless.
     contributions on this front are encouraged.

1. to learn how to configure & setup ashkelon and to get
   it up and running
   => see the file: install.txt

2. for an idea of the many things that need to be improved
   (or fixed) in ashkelon, see todo.txt

3. for an ashkelon users' guide, see guide.txt or guide.html

4. after you have gotten ashkelon configured and working,
   read apis/readme.txt as a supplement.

5. to send eitan feedback or questions, mailto: eitan@uptodata.com
   to send the entire team feedback, email ashkelon-devs@lists.sourceforge.net 

thanks for downloading ashkelon!

----
Notes:

the ashkelon repository manager (the cmd-line tool) can transparently
pass command-line options to javadoc.  so if you're trying to populate
j2se1.4 or other code that is only compatible with jdk1.4, you can 
do things like pass in a "-source 1.4" option so that javadoc will
correctly work with things like assert statements.

Last Updated July 2003
