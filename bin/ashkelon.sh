#!/bin/sh
#
# ashkelon:	Run ashkelon
#
# Heimir Thor Sverrisson 25.07.2002
#
#set -x
#
CMD=$1

# Default command is help (no arguments present)
if [ "$CMD" = "" ]
then
    CMD=HELP
fi

# The install directory root must be defined
if [ "$DBDOC_HOME" = "" ]
then
    echo "DBDOC_HOME not defined"
    exit 1
fi

# The Java home directory must be defined
if [ "$JAVA_HOME" = "" ]
then
    echo "JAVA_HOME not defined"
    exit 2
fi

CP=${JAVA_HOME}/lib/tools.jar:${DBDOC_HOME}/build/ashkelon.jar
for i in ${DBDOC_HOME}/lib/*.jar
do
   CP=${CP}:$i 
done

if [ "$CMD" = "add" ]
then
    if [ "$SOURCEPATH" = "" ]
    then
	echo "SOURCEPATH not defined"
	exit 2
    fi
    SRCPATHINFO="-sourcepath ${SOURCEPATH}"
    CLASSPATHINFO="-classpath ${CLASSPATH}"

fi # CMD == add

# Shift argument list (if not empty) so we can use $* in command
if [ $# -gt 0 ]
then
    shift
fi

# Run java with 150MB memory limit
${JAVA_HOME}/bin/java -Xmx150m -cp $CP org.ashkelon.Ashkelon $CMD $SRCPATHINFO $CLSPATHINFO $*

