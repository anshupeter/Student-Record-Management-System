@echo off
title Student Record Management System
echo.
echo ===============================================
echo   Student Record Management System v2.0
echo ===============================================
echo.
echo Starting application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo.
    echo Please install Java JRE 8 or higher from:
    echo https://www.oracle.com/java/technologies/javase-downloads.html
    echo.
    echo Or install OpenJDK from:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check if JAR file exists
if not exist "StudentGUI.jar" (
    echo ERROR: StudentGUI.jar not found!
    echo Please ensure StudentGUI.jar is in the same directory as this batch file.
    echo.
    pause
    exit /b 1
)

REM Launch the application
echo Java detected. Launching Student Management System...
echo.
java -jar StudentGUI.jar

REM Check if application closed normally
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application encountered an error.
    echo Error code: %ERRORLEVEL%
    echo.
)

echo.
echo Thank you for using Student Record Management System!
pause