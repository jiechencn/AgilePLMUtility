@echo off

@rem Use this script to encrypt a user's password so it can be copy/pasted in agile.properties file.

SETLOCAL

call setEnv.cmd

"%JAVA_HOME%\bin\java" -classpath "%CLASSPATH%" com.agile.support.AgileSSOValidator %*
echo:

:finish

ENDLOCAL
