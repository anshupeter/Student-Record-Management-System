Write-Host "Student Record Management System - EXE Creator" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

# Check if JAR file exists
if (!(Test-Path "StudentGUI.jar")) {
    Write-Host "Error: StudentGUI.jar not found!" -ForegroundColor Red
    exit 1
}

Write-Host "Found StudentGUI.jar" -ForegroundColor Green

# Create batch file
@'
@echo off
title Student Record Management System
cd /d "%~dp0"
java -jar StudentGUI.jar
pause
'@ | Out-File -FilePath "StudentGUI.bat" -Encoding ASCII

Write-Host "Created StudentGUI.bat" -ForegroundColor Green

# Create VBScript for silent execution
@'
Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")
strCurrentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)
strJarPath = strCurrentDir & "\StudentGUI.jar"
If objFSO.FileExists(strJarPath) Then
    objShell.Run "java -jar """ & strJarPath & """", 0, False
Else
    MsgBox "StudentGUI.jar not found!", vbCritical
End If
'@ | Out-File -FilePath "StudentGUI.vbs" -Encoding ASCII

Write-Host "Created StudentGUI.vbs (silent mode)" -ForegroundColor Green

Write-Host ""
Write-Host "Executable files created:" -ForegroundColor Yellow
Write-Host "1. StudentGUI.bat - Double-click to run (shows console)" -ForegroundColor White
Write-Host "2. StudentGUI.vbs - Double-click to run (silent mode)" -ForegroundColor White
Write-Host "3. StudentGUI.jar - Original JAR file" -ForegroundColor White