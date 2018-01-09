@echo on

setlocal

rem configs
set ServerPort=6020
rem mysql configs
set MysqlPort=3306
set MysqlHost=127.0.0.1
set MysqlSchema=blog
set MysqlUserName=blog
set MysqlPassword=blog123
set MysqlUrl="jdbc:mysql://%MysqlHost%:%MysqlPort%/%MysqlSchema%?useUnicode=true&characterEncoding=UTF-8&useSSL=false"
rem tank configs
set TankUrl="http://tank.eyeblue.cn"
set TankEmail=blog_dev@tank.eyeblue.cn
set TankPassword=123456
rem admin configs
set AdminUsername=admin
set AdminEmail=admin@blog.eyeblue.cn
set AdminPassword=123456
rem email configs
set MailProtocol=smtps
set MailHost=smtp.126.com
set MailPort=465
set MailUsername=eyeblue@126.com
set MailPassword=eyeblue_password
set MailDefaultEncoding=UTF-8


set OPTS=-Xmx512m -Dserver.port=%ServerPort% -Dspring.datasource.url=%MysqlUrl% -Dspring.datasource.username=%MysqlUserName% -Dspring.datasource.password=%MysqlPassword% -Dtank.url=%TankUrl% -Dtank.email=%TankEmail% -Dtank.password=%TankPassword% -Dadmin.username=%AdminUsername% -Dadmin.email=%AdminEmail% -Dadmin.password=%AdminPassword% -Dspring.mail.protocol=%MailProtocol% -Dspring.mail.host=%MailHost% -Dspring.mail.port=%MailPort% -Dspring.mail.username=%MailUsername% -Dspring.mail.password=%MailPassword% -Dspring.mail.default-encoding=%MailDefaultEncoding%


if not "%JRE_HOME%" == "" goto gotJreHome
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo At least one of these environment variable is needed to run this program
goto end

:gotJavaHome
rem No JRE given, use JAVA_HOME as JRE_HOME
set "JRE_HOME=%JAVA_HOME%"

:gotJreHome
rem Check if we have a usable JRE
if not exist "%JRE_HOME%\bin\java.exe" goto noJreHome
if not exist "%JRE_HOME%\bin\javaw.exe" goto noJreHome
goto okJava

:noJreHome
rem Needed at least a JRE
echo The JRE_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto exit

:okJava
rem Don't override _RUNJAVA if the user has set it previously
if not "%_RUNJAVA%" == "" goto gotRunJava
rem Set standard command for invoking Java.
rem Also note the quoting as JRE_HOME may contain spaces.
set _RUNJAVA="%JRE_HOME%\bin\java.exe"
:gotRunJava

if "%TITLE%" == "" set TITLE=blog-1.0.0
set _EXECJAVA=start "%TITLE%" %_RUNJAVA%

%_EXECJAVA% %OPTS% -jar blog-1.0.0.jar
goto end

:end
