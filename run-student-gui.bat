@echo off
title Student Record Management System
cd /d "%~dp0"

REM Delegates to StudentGUI.bat which handles jar/maven/javac options
if exist "StudentGUI.bat" (
	call StudentGUI.bat
	goto :EOF
)

REM Fallback: try to run JAR directly
if exist "StudentGUI.jar" (
	java -jar StudentGUI.jar
	goto :EOF
)

echo Could not find StudentGUI.bat or StudentGUI.jar. Use StudentGUI.bat to run.
pause