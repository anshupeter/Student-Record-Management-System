# Student Record Management System - EXE Creator
# This script helps create an executable file for the Java application

Write-Host "Student Record Management System - EXE Creator" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Create launcher scripts that support JAR, Maven build, or direct javac/java run

Set-Location -Path (Split-Path -Parent $MyInvocation.MyCommand.Path)

Write-Host "Creating launcher wrappers (supports JAR, Maven, or javac)..." -ForegroundColor Cyan

# Batch wrapper
$batch = @'
@echo off
cd /d "%~dp0"
REM Prefer existing JAR
if exist "StudentGUI.jar" (
    java -jar StudentGUI.jar
    goto :EOF
)

REM Try Maven
mvn -q -v >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Building with Maven...
    mvn -q package
    if exist "target\student-manager-1.0-SNAPSHOT.jar" (
        java -jar target\student-manager-1.0-SNAPSHOT.jar
        goto :EOF
    )
)

REM Fallback to javac
javac -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    if not exist out mkdir out
    javac -d out src\main\java\com\studentmanager\*.java
    if %ERRORLEVEL% EQU 0 (
        java -cp out com.studentmanager.StudentGUI
        goto :EOF
    )
)

echo Could not start application. Ensure JAR exists or install Maven/JDK.
pause
'@

$batch | Out-File -FilePath "StudentGUI.bat" -Encoding ASCII
Write-Host "Created StudentGUI.bat" -ForegroundColor Green

# PowerShell wrapper
$ps = @'
Set-Location -Path (Split-Path -Parent $MyInvocation.MyCommand.Path)

if (Test-Path "StudentGUI.jar") {
    java -jar StudentGUI.jar
    return
}

try {
    & mvn -q -v >$null 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Building with Maven..."
        & mvn -q package
        $jar = Join-Path -Path (Get-Location) -ChildPath 'target\student-manager-1.0-SNAPSHOT.jar'
        if (Test-Path $jar) {
            & java -jar $jar
            return
        }
    }
} catch {}

try {
    & javac -version >$null 2>&1
    if ($LASTEXITCODE -eq 0) {
        if (-not (Test-Path .\out)) { New-Item -ItemType Directory -Path .\out | Out-Null }
        & javac -d out src\main\java\com\studentmanager\*.java
        if ($LASTEXITCODE -eq 0) {
            & java -cp out com.studentmanager.StudentGUI
            return
        }
    }
} catch {}

Write-Host "Could not start application. Ensure JAR exists or install Maven/JDK." -ForegroundColor Yellow
'@

$ps | Out-File -FilePath "StudentGUI.ps1" -Encoding UTF8
Write-Host "Created StudentGUI.ps1" -ForegroundColor Green

# VBScript silent launcher
$vbs = @'
Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")
strCurrentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)

strJarPath = strCurrentDir & "\" & "StudentGUI.jar"
If objFSO.FileExists(strJarPath) Then
    objShell.Run "java -jar " & Chr(34) & strJarPath & Chr(34), 0, False
Else
    MsgBox "StudentGUI.jar not found. Please run StudentGUI.bat to build/run the project.", vbExclamation, "Launcher"
End If
'@

$vbs | Out-File -FilePath "StudentGUI-Silent.vbs" -Encoding ASCII
Write-Host "Created StudentGUI-Silent.vbs" -ForegroundColor Green
Write-Host "Done. Use StudentGUI.bat or StudentGUI.ps1 to run (supports Maven and javac fallbacks)." -ForegroundColor Green