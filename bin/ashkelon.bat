@echo off
setlocal

set CMD=%1

if not defined CMD set CMD=HELP

rem assume dbdoc home is set to installation dir
if not defined ASHK_HOME goto error2
if not defined SOURCEPATH goto error

set CP=%JAVA_HOME%\lib\tools.jar;%ASHK_HOME%\build\ashkelon-mgr.jar;%ASHK_HOME%\build\ashkelon-model.jar;%ASHK_HOME%\build\jdbc-driver.jar
for %%i in (%ASHK_HOME%\lib\manager\*.jar) do call cp.bat %%i
for %%i in (%ASHK_HOME%\lib\static\*.jar) do call cp.bat %%i
for %%i in (%ASHK_HOME%\lib\webapp\*.jar) do call cp.bat %%i
set CP=%CP%;%ASHK_HOME%\build\classes

if %CMD%==add set SRCPATHINFO=--sourcepath "%SOURCEPATH%"
if %CMD%==add set CLSPATHINFO=--classpath "%CP%"

java -Xmx150m -cp %CP% org.ashkelon.manager.AshkelonCmd %CMD% %SRCPATHINFO% %CLSPATHINFO% %2 %3 %4 %5 %6 %7 %8 %9

goto end

:error
echo SOURCEPATH not defined
goto end

:error2
echo ASHK_HOME not defined

:end

endlocal
