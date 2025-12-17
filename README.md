# Student Record Management System

This project contains both console-based and GUI-based versions of a Student Record Management System written in Java.

## Files Overview

### Core Classes
- **`Student.java`** - Student data model class with encapsulated fields and methods
- **`StudentManager.java`** - Console-based manager for CRUD operations
- **`Main.java`** - Console application entry point
- **`StudentGUI.java`** - GUI-based application using Java Swing

## Features

### Console Version (Main.java)
- Add new student records
- View all student records
- Search students by roll number
- Update existing student information
- Delete student records
- Simple text-based menu interface

### GUI Version (StudentGUI.java)
- Modern graphical user interface
- Data table to display all students
- Input forms for student information
- Click-to-select functionality
- Real-time search feature
- Confirmation dialogs for deletions
- Error handling with user-friendly messages
- **Data persistence using file-based storage**
- **Automatic data loading on startup**
- **Automatic data saving on changes**

## How to Run

### Console Version
```bash
# Compile
javac Student.java StudentManager.java Main.java

# Run
java Main
```

### GUI Version
```bash
# Compile
javac Student.java StudentGUI.java

# Run
java StudentGUI
```

## GUI Features
- **Add Student**: Enter roll number, name, and marks, then click "Add Student"
- **Update Student**: Select a row in the table, modify the fields, then click "Update Student"
- **Delete Student**: Select a row and click "Delete Student" (with confirmation dialog)
- **Search Student**: Click "Search Student" and enter a roll number to find and highlight
- **Clear Fields**: Reset all input fields and deselect table rows

## Technical Details
- Uses Java Swing for GUI components
- File-based data storage using `students.txt`
- Automatic data persistence (saves on every change)
- Data automatically loaded when application starts
- ArrayList for in-memory data storage
- Object-oriented design with proper encapsulation
- Input validation and error handling
- Table-based data display with selection support

## Data Storage
The GUI version automatically saves student data to a file called `students.txt` in the same directory as the application. The data is:
- **Automatically saved** when you add, update, or delete students
- **Automatically loaded** when you start the application
- **Stored in CSV format** for easy reading and portability
- **Persistent across application restarts** - your data won't be lost!

## Requirements
- Java 8 or higher
- No external dependencies required