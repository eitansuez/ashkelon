@echo off
setlocal

set CMD=%1

if not defined CMD set CMD=HELP

rem assume dbdoc home is set to installation dir
if not defined ASHK_HOME goto error2
if not defined SOURCEPATH goto error

set CP=%JAVA_HOME%\lib\tools.jar;%ASHK_HOME%\build\ashkelon.jar;%ASHK_HOME%\db\mysql\mysql-connector-java-2.0.14-bin.jar
for %%i in (%ASHK_HOME%\lib\*.jar) do call cp.bat %%i

if %CMD%==add set SRCPATHINFO=-sourcepath %SOURCEPATH%
if %CMD%==add set CLSPATHINFO=-classpath %CP%

java -Xmx150m -cp %CP% org.ashkelon.Ashkelon %CMD% %SRCPATHINFO% %CLSPATHINFO% %2 %3 %4 %5 %6 %7 %8 %9

goto end

:error
echo SOURCEPATH not defined
goto end

:error2
echo ASHK_HOME not defined

:end

endlocal
