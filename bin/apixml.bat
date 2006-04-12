@echo off
setlocal

set CMD=%1

if not defined ASHK_HOME goto error1
if not defined CMD goto error2

set CP=%ASHK_HOME%\build\ashkelon-model.jar;%ASHK_HOME%\lib\manager\jibx-run.jar;%ASHK_HOME%\lib\manager\oro.jar

java -cp %CP% org.ashkelon.util.ApiXml %CMD%

goto end

:error1
echo ASHK_HOME not defined
goto end

:error2
echo please specify package-list
goto end


:end

endlocal