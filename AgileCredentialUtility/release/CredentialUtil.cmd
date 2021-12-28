
@echo off
SETLOCAL

call setEnv.cmd

set CURRENT_DIR=%cd%
cd %~dp0\..
set PARENT_DIR=%cd%
cd %CURRENT_DIR%

"%JAVA_HOME%\bin\java" -classpath "%CLASSPATH%" -Ddomain.home=%PARENT_DIR% -Dprint.error=n com.agile.support.CredentialUtil %*
echo:
 
:finish

ENDLOCAL
