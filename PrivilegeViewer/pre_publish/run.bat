@echo on

set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.6.0_45

set CLASSPATH=.
"%JAVA_HOME%\bin\java" -ms512m -mx512m -classpath %CLASSPATH% -jar pv.jar
pause

