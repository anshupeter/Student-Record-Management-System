# Student Record Management System - EXE Creator
# This script helps create an executable file for the Java application

Write-Host "Student Record Management System - EXE Creator" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Check if JAR file exists
if (!(Test-Path "StudentGUI.jar")) {
    Write-Host "Error: StudentGUI.jar not found!" -ForegroundColor Red
    Write-Host "Please run this script from the directory containing StudentGUI.jar" -ForegroundColor Red
    exit 1
}

Write-Host "✓ StudentGUI.jar found" -ForegroundColor Green

# Method 1: Create a batch file wrapper (simplest method)
Write-Host ""
Write-Host "Creating batch file wrapper..." -ForegroundColor Yellow

$batchContent = @"
@echo off
title Student Record Management System
cd /d "%~dp0"
if not exist "StudentGUI.jar" (
    echo Error: StudentGUI.jar not found!
    pause
    exit /b 1
)
java -jar StudentGUI.jar
if errorlevel 1 (
    echo.
    echo Error: Java is not installed or not in PATH!
    echo Please install Java JRE 8 or higher.
    echo.
    pause
)
"@

$batchContent | Out-File -FilePath "StudentGUI.bat" -Encoding ASCII

Write-Host "✓ Created StudentGUI.bat" -ForegroundColor Green

# Method 2: Create PowerShell script wrapper
Write-Host ""
Write-Host "Creating PowerShell script wrapper..." -ForegroundColor Yellow

$psContent = @"
# Student Record Management System Launcher
param([switch]$NoExit)

Set-Location -Path (Split-Path -Parent `$MyInvocation.MyCommand.Path)

if (!(Test-Path "StudentGUI.jar")) {
    Write-Host "Error: StudentGUI.jar not found!" -ForegroundColor Red
    if (!`$NoExit) { Read-Host "Press Enter to exit" }
    exit 1
}

try {
    Write-Host "Starting Student Record Management System..." -ForegroundColor Green
    java -jar StudentGUI.jar
} catch {
    Write-Host "Error: Could not start the application!" -ForegroundColor Red
    Write-Host "Make sure Java JRE 8+ is installed and in your PATH." -ForegroundColor Yellow
    if (!`$NoExit) { Read-Host "Press Enter to exit" }
}
"@

$psContent | Out-File -FilePath "StudentGUI.ps1" -Encoding UTF8

Write-Host "✓ Created StudentGUI.ps1" -ForegroundColor Green

# Method 3: Create a VBScript wrapper (runs without console window)
Write-Host ""
Write-Host "Creating VBScript wrapper (silent mode)..." -ForegroundColor Yellow

$vbsContent = @"
' Student Record Management System Silent Launcher
Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Get the directory where this script is located
strCurrentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)

' Check if JAR file exists
strJarPath = strCurrentDir & "\StudentGUI.jar"
If Not objFSO.FileExists(strJarPath) Then
    MsgBox "Error: StudentGUI.jar not found!" & vbCrLf & "Please ensure the JAR file is in the same directory as this script.", vbCritical, "Student Management System"
    WScript.Quit 1
End If

' Launch the Java application silently (no console window)
strCommand = "java -jar """ & strJarPath & """"
objShell.Run strCommand, 0, False
"@

$vbsContent | Out-File -FilePath "StudentGUI-Silent.vbs" -Encoding ASCII

Write-Host "✓ Created StudentGUI-Silent.vbs (runs without console)" -ForegroundColor Green

Write-Host ""
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "Executable files created successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "You now have several ways to run your application:" -ForegroundColor White
Write-Host ""
Write-Host "1. StudentGUI.bat        - Batch file (shows console)" -ForegroundColor Yellow
Write-Host "2. StudentGUI.ps1        - PowerShell script" -ForegroundColor Yellow  
Write-Host "3. StudentGUI-Silent.vbs - VB Script (no console window)" -ForegroundColor Yellow
Write-Host "4. StudentGUI.jar        - Original JAR file" -ForegroundColor Yellow
Write-Host ""
Write-Host "For true EXE creation, you can use tools like:" -ForegroundColor Cyan
Write-Host "• Launch4j (free): http://launch4j.sourceforge.net/" -ForegroundColor White
Write-Host "• jpackage (Java 14+): Built into newer Java versions" -ForegroundColor White
Write-Host "• exe4j (commercial): http://www.ej-technologies.com/products/exe4j/overview.html" -ForegroundColor White
Write-Host ""
Write-Host "Recommended: Use StudentGUI-Silent.vbs for the best user experience!" -ForegroundColor Green
Write-Host ""