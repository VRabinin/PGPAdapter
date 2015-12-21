@ECHO OFF
REM Java development environment should be installed and accessible.  %JAVA_HOME% should be set to java build enviroment
REM for example, 

set LINK_DIR=d:\Install\SAP\BODS\pgpadapter_build

set JAVA_HOME=c:\Program Files\Java\jdk1.7.0_71

set SRC=src
set CLASSES=..\bin
set LIB=d:\Install\SAP\BODS\AdapterSDK\lib
set LIBEXT=d:\Install\SAP\BODS\AdapterSDK\lib
set JAVABIN=%JAVA_HOME%\bin
set JREBIN=c:\PROGRA~1\Java\jre1.8.0_31\bin\
set CLASSPATH=%CLASSES%;%LIBEXT%\xercesImpl.jar;%LIB%\acta_adapter_sdk.jar;%LIB%\acta_broker_client.jar;%LIB%\acta_tool.jar;%LIB%\activation.jar;%LIB%\mailapi.jar;


REM Making JAR files...

:J1
echo Making Adapter.JAR files...
"%JAVABIN%\jar" cf "%LINK_DIR%\lib\pgpadapter.jar" -C %CLASSES% com\acta\adapter\pgp





if not errorlevel 1 goto D1
echo An ERROR occurred while Making Adapter.JAR files
goto EXIT

:D1
pause
echo Generating the template configuration file for Adapter
"%JREBIN%\java" -classpath "%CLASSPATH%;%LINK_DIR%\lib\pgpadapter.jar" com.acta.adapter.sdk.AdapterMain -a com.acta.adapter.pgp.PGPAdapter -d "%LINK_DIR%\adapters\config\templates\PGPAdapter.xml" > nul
if exist "%LINK_DIR%\adapters\config\templates\PGPAdapter.xml" goto D2
echo An ERROR occurred while building Adapter configuration template  
goto EXIT

:D2
pause
echo Generating the startup configuration template file for Adapter
"%JREBIN%\java" -classpath "%CLASSPATH%;%LINK_DIR%\lib\pgpadapter.jar" com.acta.adapter.sdkutil.CreateStartupTemplate "%LINK_DIR%" PGPAdapter
goto EXIT

:SUCCESS
echo The package sf_adapter.jar and configuration template TestAdapter.xml were successfully created.
pause
:EXIT
pause
