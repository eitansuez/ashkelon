@echo off
setlocal

for %%i in (%DBDOC_HOME%\plists\*.plist) do call make_xml.bat %%i

endlocal