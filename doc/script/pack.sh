#!/bin/bash

PRE_DIR=$(pwd)

VERSION_NAME=blog-1.0.3
FINAL_NAME=$VERSION_NAME.linux.tar.gz

DIR="$( cd "$( dirname "$0"  )" && pwd  )"

cd $DIR
cd ../../
echo "maven install"
./mvnw install
cd ./target

echo "compress to tar.gz"
echo "tar -zcvf ./$FINAL_NAME $VERSION_NAME.jar $DIR/startup.sh $DIR/shutdown.sh"
cp -rf $DIR/startup.sh ./
cp -rf $DIR/shutdown.sh ./
tar -zcvf ./$FINAL_NAME $VERSION_NAME.jar startup.sh shutdown.sh

echo "check the dist file in $(pwd)"
echo "finish pack!"

cd $PRE_DIR