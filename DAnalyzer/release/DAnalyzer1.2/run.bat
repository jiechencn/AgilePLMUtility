REM -Dinclude.agile=true | false
REM -Dldap.referral=follow  | ignore | throw
REM -Dldap.countlimit=1001

set JAVA_HOME=D:\\analyzer\\java\\jdk1.7

"%JAVA_HOME%/bin/java" -jar -Dinclude.agile=true -Dldap.referral=follow -Dldap.countlimit=1001 DAnalyzer.jar 1>DAnalyzer.log 2>&1