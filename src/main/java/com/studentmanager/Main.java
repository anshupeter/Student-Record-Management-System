package com.studentmanager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Student Record Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: manager.addStudent(); break;
                case 2: manager.viewStudents(); break;
                case 3: manager.searchStudent(); break;
                case 4: manager.updateStudent(); break;
                case 5: manager.deleteStudent(); break;
                case 6: System.out.println("Exiting Program. Goodbye!"); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }
}
