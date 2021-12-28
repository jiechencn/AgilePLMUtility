@echo off
SETLOCAL

call setEnv.cmd

set CURRENT_DIR=%cd%
cd %~dp0\..
set PARENT_DIR=%cd%
cd %CURRENT_DIR%

"%JAVA_HOME%\bin\java" weblogic.WLST decryptWLSPwd.py %PARENT_DIR% %*
echo:

:finish

ENDLOCAL
