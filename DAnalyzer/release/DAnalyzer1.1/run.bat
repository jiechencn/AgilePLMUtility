REM -Dinclude.agile=true | false
REM -Dldap.referral=follow  | ignore | throw
REM -Dldap.countlimit=1001

set JAVA_HOME=D:\\Analyzer\\jdk1.7.0_55

"%JAVA_HOME%/bin/java" -jar -Dinclude.agile=true -Dldap.referral=follow DAnalyzer.jar