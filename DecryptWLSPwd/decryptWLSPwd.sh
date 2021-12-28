# Set all env variables
. ./setEnv.sh

CURRENT_DIR=`pwd`
PARENT_DIR=`dirname $CURRENT_DIR`

CLASSPATH=$CLASSPATH
export CLASSPATH

"$JAVA_HOME/bin/java" -ms64m -mx64m -classpath $CLASSPATH weblogic.WLST decryptWLSPwd.py $PARENT_DIR $*
