set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_55\

@echo on
set CLASSPATH=%CLASSPATH%;PwdUtility-0.1.jar
"%JAVA_HOME%\bin\java" -cp %CLASSPATH% oracle.agile.extention.pwd.PwdUtility  
pause