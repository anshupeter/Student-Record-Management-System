# Student Record Management System

A small Java application that provides both a console (CLI) and a graphical (Swing) interface for managing student records. The project demonstrates basic CRUD operations, file-based persistence, and a simple Swing UI for convenient interaction.

---

## Features

- Console (CLI) version:
  - Add, view, search, update and delete student records
  - Simple text-based menu for interaction

- GUI (Swing) version:
  - Table view of students with click-to-select support
  - Input forms for student details (roll number, name, marks)
  - Real-time search and selection highlighting
  - Confirmation dialogs for destructive actions
  - Error handling and input validation
  - Automatic load/save to a CSV file (`students.txt`)

---

## Files overview

- `src/main/java/com/studentmanager/Student.java` — Student data model
- `src/main/java/com/studentmanager/StudentManager.java` — Console-based manager and persistence helpers
- `src/main/java/com/studentmanager/Main.java` — Console application entry point
- `src/main/java/com/studentmanager/StudentGUI.java` — Swing GUI application
- `src/main/resources/students.txt` — Sample or packaged student data
- `students.txt` — Project-level student data file used at runtime
- `pom.xml` — Maven build configuration
- `StudentGUI.bat`, `run-student-gui.bat`, `StudentGUI-Enhanced.bat` — Windows launchers

---

## Technical Details

- Java Swing is used for the GUI components.
- Data is stored in a simple CSV format in `students.txt` and loaded into an `ArrayList` at runtime.
- Changes are persisted automatically on add/update/delete operations.
- Object-oriented structure with encapsulation and input validation.

---

## Requirements

- Java JDK 8 or later
- Maven (recommended for building and packaging)
- No external libraries required

---

## Build & Run

Preferred (Maven):

```bash
mvn clean package
```

Run the GUI or CLI JAR from the `target/` directory (example):

```bash
java -jar target\\student-manager-1.0-SNAPSHOT.jar
```

Run the CLI-specific JAR (if present):

```bash
javac -d out src\main\java\com\studentmanager\*.java
java -cp out com.studentmanager.Main
```

Windows launcher (tries JAR → Maven → javac):

```powershell
StudentGUI.bat
```

Fallback (compile and run with `javac`):

```bash
javac -d out src\\main\\java\\com\\studentmanager\\*.java
java -cp out com.studentmanager.StudentGUI
```

## GUI Usage (quick)

- Add Student: fill roll number, name and marks, then click **Add Student**.
- Update Student: select a table row, edit fields, then click **Update Student**.
- Delete Student: select a table row and click **Delete Student** (confirm when prompted).
- Search Student: use the search field or **Search Student** button to locate by roll number.
- Clear Fields: reset input fields and table selection.

---

## Data Storage

- The GUI uses `students.txt` in the application directory as the canonical data store.
- Data is saved in CSV format and is loaded automatically on startup.
- The file is updated after each change, ensuring persistence across restarts.

---

## Packaging notes

- If you want a self-contained runnable JAR, configure the Maven `maven-jar-plugin` or `maven-shade-plugin` and set the `Main-Class` in the manifest.
- I can add a Maven plugin configuration to `pom.xml` on request.

---

## Contributing

- Improvements, bug fixes, and documentation updates are welcome. Please open issues or submit pull requests.

---

## License

No license is specified. Add a `LICENSE` file to apply an open-source license.

---
