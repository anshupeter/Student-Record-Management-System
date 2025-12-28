Write-Host "Creating launcher scripts (supports JAR, Maven, or javac)" -ForegroundColor Cyan

Set-Location -Path (Split-Path -Parent $MyInvocation.MyCommand.Path)

# Batch wrapper (same as create-exe.ps1)
$batch = @'
@echo off
cd /d "%~dp0"
if exist "StudentGUI.jar" (
    java -jar StudentGUI.jar
    goto :EOF
)

mvn -q -v >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Building with Maven...
    mvn -q package
    if exist "target\student-manager-1.0-SNAPSHOT.jar" (
        java -jar target\student-manager-1.0-SNAPSHOT.jar
        goto :EOF
    )
)

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

# Silent VBScript
$vbs = @'
Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")
strCurrentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)
strJarPath = strCurrentDir & "\" & "StudentGUI.jar"
If objFSO.FileExists(strJarPath) Then
    objShell.Run "java -jar " & Chr(34) & strJarPath & Chr(34), 0, False
Else
    MsgBox "StudentGUI.jar not found. Use StudentGUI.bat to build/run the project.", vbExclamation
End If
'@

$vbs | Out-File -FilePath "StudentGUI.vbs" -Encoding ASCII
Write-Host "Created StudentGUI.vbs (silent mode)" -ForegroundColor Green

Write-Host "Done. Use StudentGUI.bat to run (supports Maven and javac fallbacks)." -ForegroundColor Green