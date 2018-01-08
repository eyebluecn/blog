#!/bin/bash

PRE_DIR=$(pwd)

VERSION_NAME=blog-1.0.0
FINAL_NAME=$VERSION_NAME.linux.tar.gz

DIR="$( cd "$( dirname "$0"  )" && pwd  )"

echo "compress to tar.gz"
echo "tar -zcvf ./$FINAL_NAME $VERSION_NAME.jar $DIR/startup.sh $DIR/shutdown.sh"
cd $DIR
cd ../../target
tar -zcvf ./$FINAL_NAME $VERSION_NAME.jar $DIR/startup.sh $DIR/shutdown.sh

echo "check the dist file in $(pwd)"
echo "finish pack!"

cd $PRE_DIR