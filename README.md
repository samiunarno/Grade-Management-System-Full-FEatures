# JavaPort Suite

This project is a modern web application port of two Java applications: a Swing-based **Professional Calculator** and a Console-based **Student Grade Management System**. It utilizes React, TypeScript, and Tailwind CSS to replicate and enhance the original functionality.

## Features

### 1. Professional Calculator
Replicates the `ProfessionalCalculator.java` Swing application.
*   **Dual Theme Support:** Toggle between Dark and Light modes with persistent state.
*   **History & Logic:** Standard arithmetic operations with order of operations support.
*   **UI/UX:** Styled to match the original `RoundRectangle2D` Java Swing aesthetic with modern CSS gradients.
*   **Responsive:** Fully responsive layout adapting to mobile and desktop.

### 2. Grade Management System
Replicates the `StudentSys.java` logic.
*   **Role-Based Access Control (RBAC):**
    *   **Admin:** Full access to manage users and students.
    *   **Teacher:** Can add students, grade them, and view statistics.
    *   **Student:** Read-only access to view class data.
*   **Student Management:** Create, read, and delete student records.
*   **Grading Logic:** Automatic calculation of Averages and Letter Grades (A-F).
*   **Class Statistics:** Visual breakdown of grade distributions and top/bottom performers.
*   **Search & Sort:** Filter students by name/ID and sort by performance.

## Tech Stack

*   **Framework:** React 18
*   **Language:** TypeScript
*   **Styling:** Tailwind CSS
*   **Icons:** Lucide React
*   **Charts:** Recharts (for Grade Statistics)

## Default Credentials

The system comes pre-loaded with the following accounts (matching the original Java code):

| Role    | Username | Password   |
| :------ | :------- | :--------- |
| Admin   | admin    | admin123   |
| Teacher | teacher  | teacher123 |
| Student | student  | student123 |
