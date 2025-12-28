Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")
strCurrentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)
strJarPath = strCurrentDir & "\StudentGUI.jar"
If objFSO.FileExists(strJarPath) Then
    objShell.Run "java -jar """ & strJarPath & """", 0, False
Else
    MsgBox "StudentGUI.jar not found!", vbCritical
End If
