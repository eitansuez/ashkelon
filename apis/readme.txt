5/7/2002

To build documentation for the whole of j2se
sometimes it may be necessary to do it in chunks
due to the shear size of it (can take up more memory
than heap size in your vm).

Here's a simple method to do this:

a. ashkelon add @java.plist
b. ashkelon add @javax.plist
c. ashkelon add @org.omg.plist
d. ashkelon add @j2se.xml

in step [d] the packages will already have been documented
so the only thing this step will do is create the api 
documentation info.

(javadoc only has the notion of packages and classes and
 so on.  since ashkelon is a multi-api repository, it also
 includes the concept/notion of an api as a database entity
 that contains packages)


Tip:
 - don't use the j2se source that comes with the jdk.
  instead, download the source from:
    http://wwws.sun.com/software/java2/index.html

