#!/bin/bash

# executable path
DIR="$( cd "$( dirname "$0"  )" && pwd  )"
JAR_PATH=$DIR/blog-1.0.3.jar

#configs
ServerPort=6020
#mysql configs
MysqlPort=3306
MysqlHost=127.0.0.1
MysqlSchema=blog
MysqlUsername=blog
MysqlPassword=blog123
MysqlUrl="jdbc:mysql://$MysqlHost:$MysqlPort/$MysqlSchema?useUnicode=true&characterEncoding=UTF-8&useSSL=false"
#tank configs
TankUrl="https://tank.eyeblue.cn"
TankEmail=blog_dev@tank.eyeblue.cn
TankPassword=123456
#admin configs
AdminUsername=admin
AdminEmail=admin@blog.eyeblue.cn
AdminPassword=123456
#email configs
MailProtocol=smtps
MailHost=smtp.126.com
MailPort=465
MailUsername=eyeblue@126.com
MailPassword=eyeblue_password
MailDefaultEncoding=UTF-8

OPTS="-Xmx512m -Dserver.port=$ServerPort -Dspring.datasource.url=$MysqlUrl -Dspring.datasource.username=$MysqlUsername -Dspring.datasource.password=$MysqlPassword -Dtank.url=$TankUrl -Dtank.email=$TankEmail -Dtank.password=$TankPassword -Dadmin.username=$AdminUsername -Dadmin.email=$AdminEmail -Dadmin.password=$AdminPassword -Dspring.mail.protocol=$MailProtocol -Dspring.mail.host=$MailHost -Dspring.mail.port=$MailPort -Dspring.mail.username=$MailUsername -Dspring.mail.password=$MailPassword -Dspring.mail.default-encoding=$MailDefaultEncoding"


echo $OPTS


JAVA='java'
if [ -z `which java` ]; then
    if [ -z $JAVA_HOME ];
        then
            JAVA=$JAVA_HOME/bin/java
        else
            echo 'Cannot find java command and JAVA_HOME.'
    fi
fi

if [ ! -z `java -version 2>&1 | grep 'java version' | awk '{print $3}' | egrep '1.[8-9].\d*'` ]; then
    nohup $JAVA $OPTS -jar $JAR_PATH >/dev/null 2>&1 &
    echo $JAR_PATH
    echo 'Started successfully.'
else
        echo 'Java version not support, must be 1.8 or 1.8+.'
fi