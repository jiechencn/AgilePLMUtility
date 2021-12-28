# Set all env variables
. ./setEnv.sh

CURRENT_DIR=`pwd`
PARENT_DIR=`dirname $CURRENT_DIR`

CLASSPATH=$CLASSPATH
export CLASSPATH

"$JAVA_HOME/bin/java" -classpath $CLASSPATH -Ddomain.home=$PARENT_DIR -Dprint.error=n com.agile.support.AgileSSOValidator $*


