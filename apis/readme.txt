3/20/2005

Javadoc understands the notion of packages and classes and
methods, etc..  However, it doesn't concern itself with documenting
multiple apis as one set of documentation.

ashkelon on the other hand does.  So ashkelon extends the hierarchy
and formally introduces the notion of an api, which is essentially
a collection of related packages.

the way you populate apis into ashkelon is by feeding ashkelon
an api.xml file.  the basic way this works is by invoking the
'ashkelon add' command like so:
  ashkelon add jibx.xml
    (for example) 

see the documentation for more information. 

Tip:
 - don't use the j2se source that comes with the jdk.
  instead, download the source from:
    http://wwws.sun.com/software/java2/index.html

also, some of the .java files in j2se are built as part of the
j2se build process.  that is, some of them are implemented natively
in c.  if you really want complete javadocs for j2se, you'll have
to make a specific target (i forget which at the moment).


