@echo off
setlocal
set CP=%CP%;%1

set classpath=.;%DBDOC_HOME%\build
java org.ashkelon.util.ApiXml %1 > %1.xml


endlocal