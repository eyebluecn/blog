#!/bin/bash

DIR="$( cd "$( dirname "$0"  )" && pwd  )"
JAR_PATH=$DIR/blog-1.0.3.jar

EDASPID=`ps -ef | grep "java .* -jar $JAR_PATH"|grep -v grep |head -n 1 | awk '{print $2}'`
if [ -z $EDASPID ];
then
        echo "Cannot find $JAR_PATH."
else
        kill -9 $EDASPID
        echo $JAR_PATH
        echo 'Shutdown successfully.'
fi