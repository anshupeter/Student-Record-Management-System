@echo off
title Student Record Management System
cd /d "%~dp0"

REM Try to run an existing JAR first
if exist "StudentGUI.jar" (
	java -jar StudentGUI.jar
	goto :EOF
)

REM Try Maven (recommended)
mvn -q -v >nul 2>&1
if %ERRORLEVEL% EQU 0 (
	echo Building with Maven...
	mvn -q package
	if exist "target\student-manager-1.0-SNAPSHOT.jar" (
		java -jar target\student-manager-1.0-SNAPSHOT.jar
		goto :EOF
	)
)

REM Try compiling directly with javac (fallback)
javac -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
	echo Compiling with javac...
	if not exist out mkdir out
	javac -d out src\main\java\com\studentmanager\*.java
	if %ERRORLEVEL% EQU 0 (
		java -cp out com.studentmanager.StudentGUI
		goto :EOF
	)
)

echo.
echo Could not start application. Ensure one of the following is available:
echo  - StudentGUI.jar in the application directory
echo  - Maven (to build and run the project)
echo  - JDK (javac/java) to compile and run from source
echo.
pause
