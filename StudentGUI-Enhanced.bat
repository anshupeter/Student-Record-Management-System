@echo off
title Student Record Management System - Enhanced
echo.
echo ===============================================
echo   Student Record Management System v2.0
echo ===============================================
echo.
echo Starting application...
echo.

REM Preferred: delegate to StudentGUI.bat which handles jar/maven/javac
if exist "StudentGUI.bat" (
    call StudentGUI.bat
    goto :EOF
)

REM If StudentGUI.bat missing, try JAR
if exist "StudentGUI.jar" (
    java -jar StudentGUI.jar
    goto :EOF
)

REM Try Maven build-and-run
mvn -q -v >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Building with Maven...
    mvn -q package
    if exist "target\student-manager-1.0-SNAPSHOT.jar" (
        java -jar target\student-manager-1.0-SNAPSHOT.jar
        goto :EOF
    )
)

REM Try compiling with javac
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
echo Could not start application. Ensure a JAR exists or install Maven/JDK.
pause