package com.studentmanager;

import java.util.ArrayList;
import java.util.Scanner;

public class StudentManager {
    private ArrayList<Student> students = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void addStudent() {
        System.out.print("Enter Roll Number: ");
        int roll = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();

        students.add(new Student(roll, name, marks));
        System.out.println("Student added successfully!");
    }

    public void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println("\n--- Student Records ---");
            for (Student s : students) {
                s.display();
            }
        }
    }

    public void searchStudent() {
        System.out.print("Enter Roll Number to Search: ");
        int roll = sc.nextInt();

        for (Student s : students) {
            if (s.getRollNumber() == roll) {
                s.display();
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void updateStudent() {
        System.out.print("Enter Roll Number to Update: ");
        int roll = sc.nextInt();

        for (Student s : students) {
            if (s.getRollNumber() == roll) {
                sc.nextLine(); // consume newline
                System.out.print("Enter new name: ");
                String name = sc.nextLine();
                System.out.print("Enter new marks: ");
                double marks = sc.nextDouble();
                s.setName(name);
                s.setMarks(marks);
                System.out.println("Record updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void deleteStudent() {
        System.out.print("Enter Roll Number to Delete: ");
        int roll = sc.nextInt();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNumber() == roll) {
                students.remove(i);
                System.out.println("Record deleted.");
                return;
            }
        }
        System.out.println("Student not found.");
    }
}
