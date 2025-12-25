import java.util.*;
import java.text.DecimalFormat;
import java.io.*;

// ==================== USER CLASS ====================
class User {
    private String username;
    private String password;
    private String role; // "admin" or "teacher"
    private String name;
    
    public User(String username, String password, String role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    
    // Setter for password (for password change)
    public void setPassword(String password) { this.password = password; }

    // [FIXED] Added setter for role
    public void setRole(String role) { this.role = role; }
    
    @Override
    public String toString() {
        return String.format("%-15s | %-10s | %-20s", username, role, name);
    }
}

// ==================== LOGIN SYSTEM ====================
class LoginSystem {
    private List<User> users;
    private User currentUser;
    private Scanner scanner;
    private static final String USER_FILE = "users.dat";
    
    public LoginSystem() {
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadUsers();
        initializeDefaultUsers();
    }
    
    // Initialize default users
    private void initializeDefaultUsers() {
        if (users.isEmpty()) {
            // Default admin user
            users.add(new User("admin", "admin123", "admin", "System Administrator"));
            // Default teacher user
            users.add(new User("teacher", "teacher123", "teacher", "John Teacher"));
            // Default student user (view-only)
            users.add(new User("student", "student123", "student", "Alice Student"));
            
            saveUsers();
            System.out.println("Default users created:");
            System.out.println("Admin: admin / admin123");
            System.out.println("Teacher: teacher / teacher123");
            System.out.println("Student: student / student123");
        }
    }
    
    // Load users from file
    private void loadUsers() {
        try {
            File file = new File(USER_FILE);
            if (file.exists()) {
                Scanner fileScanner = new Scanner(file);
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                    }
                }
                fileScanner.close();
            }
        } catch (IOException e) {
            System.out.println("Could not load user database.");
        }
    }
    
    // Save users to file
    public void saveUsers() { // Changed to public so GradeManagementSystem can use it
        try {
            PrintWriter writer = new PrintWriter(USER_FILE);
            for (User user : users) {
                writer.println(user.getUsername() + "," + 
                              user.getPassword() + "," + 
                              user.getRole() + "," + 
                              user.getName());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save user database.");
        }
    }
    
    // Login menu
    public boolean showLoginMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           LOGIN SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. Login");
            System.out.println("2. Register (New User)");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit System");
            System.out.println("=".repeat(50));
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    if (login()) {
                        return true;
                    }
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    forgotPassword();
                    break;
                case "4":
                    System.out.println("Exiting system...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    // Login function
    private boolean login() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("LOGIN");
        System.out.println("-".repeat(40));
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("\n✓ Login successful!");
                System.out.println("Welcome, " + user.getName() + "!");
                System.out.println("Role: " + user.getRole().toUpperCase());
                return true;
            }
        }
        
        System.out.println("✗ Invalid username or password!");
        return false;
    }
    
    // Register new user
    private void register() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("REGISTER NEW USER");
        System.out.println("-".repeat(40));
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        // Check if username exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("✗ Username already exists!");
                return;
            }
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        if (password.length() < 6) {
            System.out.println("✗ Password must be at least 6 characters!");
            return;
        }
        
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("✗ Passwords do not match!");
            return;
        }
        
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine().trim();
        
        // Default role is "student"
        String role = "student";
        
        // Ask for registration code for admin/teacher roles
        System.out.print("Enter registration code (or press Enter for student): ");
        String code = scanner.nextLine().trim();
        
        if (code.equals("ADMIN2024")) {
            role = "admin";
        } else if (code.equals("TEACHER2024")) {
            role = "teacher";
        }
        
        User newUser = new User(username, password, role, name);
        users.add(newUser);
        saveUsers();
        
        System.out.println("✓ Registration successful!");
        System.out.println("You can now login with your credentials.");
    }
    
    // Forgot password
    private void forgotPassword() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("FORGOT PASSWORD");
        System.out.println("-".repeat(40));
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine().trim();
                
                if (user.getName().equalsIgnoreCase(name)) {
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine().trim();
                    
                    System.out.print("Confirm new password: ");
                    String confirmPassword = scanner.nextLine().trim();
                    
                    if (newPassword.equals(confirmPassword)) {
                        user.setPassword(newPassword);
                        saveUsers();
                        System.out.println("✓ Password reset successful!");
                    } else {
                        System.out.println("✗ Passwords do not match!");
                    }
                    return;
                }
            }
        }
        
        System.out.println("✗ User not found or information incorrect!");
    }
    
    // Get current user
    public User getCurrentUser() {
        return currentUser;
    }
    
    // User management (admin only)
    public void manageUsers() {
        if (!currentUser.getRole().equals("admin")) {
            System.out.println("✗ Access denied! Admin only.");
            return;
        }
        
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("           USER MANAGEMENT");
            System.out.println("=".repeat(60));
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. Change User Role");
            System.out.println("4. Reset User Password");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(60));
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1": viewAllUsers(); break;
                case "2": deleteUser(); break;
                case "3": changeUserRole(); break;
                case "4": resetUserPassword(); break;
                case "5": return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    private void viewAllUsers() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ALL REGISTERED USERS");
        System.out.println("-".repeat(50));
        System.out.printf("%-15s | %-10s | %-20s\n", "Username", "Role", "Name");
        System.out.println("-".repeat(50));
        
        for (User user : users) {
            System.out.println(user);
        }
        
        System.out.println("-".repeat(50));
        System.out.println("Total Users: " + users.size());
    }
    
    private void deleteUser() {
        viewAllUsers();
        System.out.print("\nEnter username to delete: ");
        String username = scanner.nextLine().trim();
        
        // Prevent deleting current user
        if (username.equals(currentUser.getUsername())) {
            System.out.println("✗ Cannot delete your own account!");
            return;
        }
        
        User toDelete = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                toDelete = user;
                break;
            }
        }
        
        if (toDelete != null) {
            System.out.print("Are you sure you want to delete " + username + "? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.equals("yes")) {
                users.remove(toDelete);
                saveUsers();
                System.out.println("✓ User deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("✗ User not found!");
        }
    }
    
    // [FIXED] Updated to implement role changing
    private void changeUserRole() {
        viewAllUsers();
        System.out.print("\nEnter username to change role: ");
        String username = scanner.nextLine().trim();
        
        User toChange = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                toChange = user;
                break;
            }
        }
        
        if (toChange != null) {
            System.out.println("Current role: " + toChange.getRole());
            System.out.println("Available roles: admin, teacher, student");
            System.out.print("Enter new role: ");
            String newRole = scanner.nextLine().trim().toLowerCase();
            
            if (newRole.equals("admin") || newRole.equals("teacher") || newRole.equals("student")) {
                // Now using the setRole method
                toChange.setRole(newRole);
                saveUsers();
                System.out.println("✓ Role updated successfully to: " + newRole);
            } else {
                System.out.println("✗ Invalid role!");
            }
        } else {
            System.out.println("✗ User not found!");
        }
    }
    
    private void resetUserPassword() {
        viewAllUsers();
        System.out.print("\nEnter username to reset password: ");
        String username = scanner.nextLine().trim();
        
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine().trim();
                
                System.out.print("Confirm new password: ");
                String confirmPassword = scanner.nextLine().trim();
                
                if (newPassword.equals(confirmPassword)) {
                    user.setPassword(newPassword);
                    saveUsers();
                    System.out.println("✓ Password reset successfully!");
                } else {
                    System.out.println("✗ Passwords do not match!");
                }
                return;
            }
        }
        
        System.out.println("✗ User not found!");
    }
}

// ==================== STUDENT CLASS ====================
class Student {
    private String id;
    private String name;
    private List<Double> grades;
    private String gradeLevel;
    private double average;
    private static int studentCounter = 1000;
    
    public Student(String name) {
        this.id = "STU" + studentCounter++;
        this.name = name;
        this.grades = new ArrayList<>();
        this.gradeLevel = "N/A";
        this.average = 0.0;
    }
    
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
        this.gradeLevel = "N/A";
        this.average = 0.0;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<Double> getGrades() { return grades; }
    public String getGradeLevel() { return gradeLevel; }
    public double getAverage() { return average; }
    
    // Add a grade
    public boolean addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            grades.add(grade);
            calculateAverage();
            assignGradeLevel();
            return true;
        }
        return false;
    }
    
    // Calculate average
    private void calculateAverage() {
        if (grades.isEmpty()) {
            average = 0.0;
            return;
        }
        
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        average = sum / grades.size();
    }
    
    // Assign grade level
    private void assignGradeLevel() {
        if (grades.isEmpty()) {
            gradeLevel = "N/A";
            return;
        }
        
        if (average >= 90) gradeLevel = "A";
        else if (average >= 80) gradeLevel = "B";
        else if (average >= 70) gradeLevel = "C";
        else if (average >= 60) gradeLevel = "D";
        else gradeLevel = "F";
    }
    
    // Display student info
    public void displayInfo() {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("\n════════════════════════════════════════");
        System.out.println("STUDENT INFORMATION");
        System.out.println("════════════════════════════════════════");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Grades: " + grades);
        System.out.println("Average: " + df.format(average));
        System.out.println("Grade Level: " + gradeLevel);
        System.out.println("════════════════════════════════════════");
    }
    
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.format("%-10s | %-20s | Avg: %6s | Grade: %s", 
            id, name, df.format(average), gradeLevel);
    }
}

// ==================== GRADE MANAGEMENT SYSTEM ====================
class GradeManagementSystem {
    private List<Student> students;
    private Scanner scanner;
    private DecimalFormat df;
    private User currentUser;
    
    public GradeManagementSystem(User currentUser) {
        this.students = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.df = new DecimalFormat("#.##");
        this.currentUser = currentUser;
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        Student s1 = new Student("John Doe");
        s1.addGrade(85.5);
        s1.addGrade(92.0);
        s1.addGrade(78.5);
        
        Student s2 = new Student("Jane Smith");
        s2.addGrade(95.0);
        s2.addGrade(88.5);
        s2.addGrade(91.0);
        
        Student s3 = new Student("Bob Johnson");
        s3.addGrade(72.0);
        s3.addGrade(68.5);
        s3.addGrade(74.0);
        
        students.add(s1);
        students.add(s2);
        students.add(s3);
    }
    
    // Check permission based on role
    private boolean checkPermission(String action) {
        String role = currentUser.getRole();
        
        switch (action) {
            case "add_student":
            case "add_grade":
            case "update_student":
            case "remove_student":
                return role.equals("admin") || role.equals("teacher");
            case "view_students":
            case "view_details":
            case "search":
            case "sort":
            case "statistics":
                return role.equals("admin") || role.equals("teacher") || role.equals("student");
            case "manage_users":
                return role.equals("admin");
            default:
                return false;
        }
    }
    
    // Main menu with role-based access
    public void displayMainMenu(LoginSystem loginSystem) {
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("    STUDENT GRADE MANAGEMENT SYSTEM");
            System.out.println("    Logged in as: " + currentUser.getName() + 
                             " (" + currentUser.getRole().toUpperCase() + ")");
            System.out.println("=".repeat(60));
            
            System.out.println("STUDENT MANAGEMENT");
            System.out.println("1. Add New Student");
            System.out.println("2. Add Grade to Student");
            System.out.println("3. View All Students");
            System.out.println("4. View Student Details");
            System.out.println("5. Update Student Information");
            System.out.println("6. Remove Student");
            System.out.println("7. Calculate Class Statistics");
            System.out.println("8. Search Students");
            System.out.println("9. Sort Students");
            
            if (checkPermission("manage_users")) {
                System.out.println("\nUSER MANAGEMENT");
                System.out.println("10. Manage Users");
            }
            
            System.out.println("\nACCOUNT MANAGEMENT");
            System.out.println("11. Change Password");
            System.out.println("12. Logout");
            System.out.println("13. Exit System");
            System.out.println("=".repeat(60));
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1": 
                    if (checkPermission("add_student")) addNewStudent();
                    else System.out.println("✗ Access denied! Teacher/Admin only.");
                    break;
                case "2": 
                    if (checkPermission("add_grade")) addGradeToStudent();
                    else System.out.println("✗ Access denied! Teacher/Admin only.");
                    break;
                case "3": 
                    if (checkPermission("view_students")) viewAllStudents();
                    else System.out.println("✗ Access denied!");
                    break;
                case "4": 
                    if (checkPermission("view_details")) viewStudentDetails();
                    else System.out.println("✗ Access denied!");
                    break;
                case "5": 
                    if (checkPermission("update_student")) updateStudentInfo();
                    else System.out.println("✗ Access denied! Teacher/Admin only.");
                    break;
                case "6": 
                    if (checkPermission("remove_student")) removeStudent();
                    else System.out.println("✗ Access denied! Teacher/Admin only.");
                    break;
                case "7": 
                    if (checkPermission("statistics")) calculateClassStatistics();
                    else System.out.println("✗ Access denied!");
                    break;
                case "8": 
                    if (checkPermission("search")) searchStudents();
                    else System.out.println("✗ Access denied!");
                    break;
                case "9": 
                    if (checkPermission("sort")) sortStudents();
                    else System.out.println("✗ Access denied!");
                    break;
                case "10": 
                    if (checkPermission("manage_users")) loginSystem.manageUsers();
                    else System.out.println("✗ Access denied! Admin only.");
                    break;
                case "11": changePassword(loginSystem); break;
                case "12": return; // Logout
                case "13": 
                    System.out.println("Exiting system...");
                    System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    // ==================== PASSWORD CHANGE ====================
    private void changePassword(LoginSystem loginSystem) {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("CHANGE PASSWORD");
        System.out.println("-".repeat(40));
        
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();
        
        if (!currentPassword.equals(currentUser.getPassword())) {
            System.out.println("✗ Incorrect current password!");
            return;
        }
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();
        
        if (newPassword.length() < 6) {
            System.out.println("✗ Password must be at least 6 characters!");
            return;
        }
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("✗ Passwords do not match!");
            return;
        }
        
        currentUser.setPassword(newPassword);
        loginSystem.saveUsers();
        System.out.println("✓ Password changed successfully!");
    }
    
    // ==================== STUDENT MANAGEMENT METHODS ====================
    private void addNewStudent() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("ADD NEW STUDENT");
        System.out.println("-".repeat(40));
        
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        System.out.print("Enter custom ID (or press Enter for auto-generated): ");
        String customId = scanner.nextLine().trim();
        
        Student student;
        if (!customId.isEmpty()) {
            student = new Student(customId, name);
        } else {
            student = new Student(name);
        }
        
        students.add(student);
        System.out.println("✓ Student added successfully!");
        System.out.println("Student ID: " + student.getId());
        
        System.out.print("\nAdd grades now? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("yes") || response.equals("y")) {
            addGradesToSpecificStudent(student);
        }
    }
    
    private void addGradeToStudent() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("ADD GRADE TO STUDENT");
        System.out.println("-".repeat(40));
        
        Student student = findStudent();
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        addGradesToSpecificStudent(student);
    }
    
    private void addGradesToSpecificStudent(Student student) {
        System.out.println("\nAdding grades for: " + student.getName() + " (ID: " + student.getId() + ")");
        
        while (true) {
            try {
                System.out.print("Enter grade (0-100) or -1 to stop: ");
                double grade = Double.parseDouble(scanner.nextLine().trim());
                
                if (grade == -1) {
                    break;
                }
                
                if (student.addGrade(grade)) {
                    System.out.println("✓ Grade added: " + grade);
                } else {
                    System.out.println("✗ Invalid grade! Must be between 0 and 100.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid input! Please enter a number.");
            }
        }
        
        student.displayInfo();
    }
    
    private void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                         ALL STUDENTS");
        System.out.println("=".repeat(70));
        System.out.printf("%-10s | %-20s | %-10s | %-8s | %s\n", 
            "ID", "Name", "Average", "Grade", "Grade Count");
        System.out.println("-".repeat(70));
        
        for (Student student : students) {
            System.out.printf("%-10s | %-20s | %-10s | %-8s | %d\n",
                student.getId(),
                student.getName(),
                df.format(student.getAverage()),
                student.getGradeLevel(),
                student.getGrades().size());
        }
        
        System.out.println("-".repeat(70));
        System.out.println("Total Students: " + students.size());
        System.out.println("=".repeat(70));
    }
    
    private void viewStudentDetails() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("VIEW STUDENT DETAILS");
        System.out.println("-".repeat(40));
        
        Student student = findStudent();
        if (student != null) {
            student.displayInfo();
        } else {
            System.out.println("Student not found!");
        }
    }
    
    private void updateStudentInfo() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("UPDATE STUDENT INFORMATION");
        System.out.println("-".repeat(40));
        
        Student student = findStudent();
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\nCurrent name: " + student.getName());
        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = scanner.nextLine().trim();
        
        if (!newName.isEmpty()) {
            System.out.println("Name update functionality would be implemented here.");
        }
        
        System.out.println("\nOptions:");
        System.out.println("1. Add more grades");
        System.out.println("2. Remove grades");
        System.out.println("3. Cancel");
        System.out.print("Choose option: ");
        
        String option = scanner.nextLine().trim();
        switch (option) {
            case "1":
                addGradesToSpecificStudent(student);
                break;
            case "2":
                removeGrades(student);
                break;
            case "3":
                System.out.println("Update cancelled.");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private void removeGrades(Student student) {
        List<Double> grades = student.getGrades();
        if (grades.isEmpty()) {
            System.out.println("No grades to remove!");
            return;
        }
        
        System.out.println("\nCurrent grades: " + grades);
        System.out.print("Enter grade index to remove (0-" + (grades.size()-1) + "): ");
        
        try {
            int index = Integer.parseInt(scanner.nextLine().trim());
            if (index >= 0 && index < grades.size()) {
                grades.remove(index);
                System.out.println("✓ Grade removed.");
                student.displayInfo();
            } else {
                System.out.println("Invalid index!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
    
    private void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("REMOVE STUDENT");
        System.out.println("-".repeat(40));
        
        Student student = findStudent();
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.print("Are you sure you want to remove " + student.getName() + "? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            students.remove(student);
            System.out.println("✓ Student removed successfully!");
        } else {
            System.out.println("Removal cancelled.");
        }
    }
    
    private void calculateClassStatistics() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("      CLASS STATISTICS");
        System.out.println("=".repeat(50));
        
        double totalAverage = 0;
        int totalGrades = 0;
        int aCount = 0, bCount = 0, cCount = 0, dCount = 0, fCount = 0;
        double highestAverage = Double.MIN_VALUE;
        double lowestAverage = Double.MAX_VALUE;
        Student topStudent = null;
        Student bottomStudent = null;
        
        for (Student student : students) {
            double avg = student.getAverage();
            totalAverage += avg;
            totalGrades += student.getGrades().size();
            
            switch (student.getGradeLevel()) {
                case "A": aCount++; break;
                case "B": bCount++; break;
                case "C": cCount++; break;
                case "D": dCount++; break;
                case "F": fCount++; break;
            }
            
            if (avg > highestAverage) {
                highestAverage = avg;
                topStudent = student;
            }
            if (avg < lowestAverage) {
                lowestAverage = avg;
                bottomStudent = student;
            }
        }
        
        double classAverage = totalAverage / students.size();
        
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Grades Recorded: " + totalGrades);
        System.out.printf("Class Average: %.2f\n", classAverage);
        
        System.out.println("\nGrade Distribution:");
        System.out.println("A: " + aCount + " students");
        System.out.println("B: " + bCount + " students");
        System.out.println("C: " + cCount + " students");
        System.out.println("D: " + dCount + " students");
        System.out.println("F: " + fCount + " students");
        
        if (topStudent != null) {
            System.out.println("\nTop Performing Student:");
            System.out.println("  Name: " + topStudent.getName());
            System.out.printf("  Average: %.2f\n", topStudent.getAverage());
            System.out.println("  Grade Level: " + topStudent.getGradeLevel());
        }
        
        if (bottomStudent != null) {
            System.out.println("\nNeeds Improvement:");
            System.out.println("  Name: " + bottomStudent.getName());
            System.out.printf("  Average: %.2f\n", bottomStudent.getAverage());
            System.out.println("  Grade Level: " + bottomStudent.getGradeLevel());
        }
        
        System.out.println("=".repeat(50));
    }
    
    private void searchStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("SEARCH STUDENTS");
        System.out.println("-".repeat(40));
        System.out.println("1. Search by Name");
        System.out.println("2. Search by ID");
        System.out.println("3. Search by Grade Level");
        System.out.print("Choose option: ");
        
        String option = scanner.nextLine().trim();
        List<Student> results = new ArrayList<>();
        
        switch (option) {
            case "1":
                System.out.print("Enter name to search: ");
                String nameQuery = scanner.nextLine().trim().toLowerCase();
                for (Student s : students) {
                    if (s.getName().toLowerCase().contains(nameQuery)) {
                        results.add(s);
                    }
                }
                break;
                
            case "2":
                System.out.print("Enter ID to search: ");
                String idQuery = scanner.nextLine().trim();
                for (Student s : students) {
                    if (s.getId().equalsIgnoreCase(idQuery)) {
                        results.add(s);
                    }
                }
                break;
                
            case "3":
                System.out.print("Enter grade level (A/B/C/D/F): ");
                String gradeQuery = scanner.nextLine().trim().toUpperCase();
                for (Student s : students) {
                    if (s.getGradeLevel().equals(gradeQuery)) {
                        results.add(s);
                    }
                }
                break;
                
            default:
                System.out.println("Invalid option!");
                return;
        }
        
        displaySearchResults(results);
    }
    
    private void displaySearchResults(List<Student> results) {
        if (results.isEmpty()) {
            System.out.println("No students found!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SEARCH RESULTS (" + results.size() + " found)");
        System.out.println("=".repeat(70));
        
        for (Student student : results) {
            System.out.println(student);
        }
        
        System.out.println("=".repeat(70));
    }
    
    private void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system!");
            return;
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("SORT STUDENTS");
        System.out.println("-".repeat(40));
        System.out.println("1. Sort by Name (A-Z)");
        System.out.println("2. Sort by Average (High to Low)");
        System.out.println("3. Sort by Average (Low to High)");
        System.out.println("4. Sort by Grade Level");
        System.out.print("Choose option: ");
        
        String option = scanner.nextLine().trim();
        List<Student> sortedList = new ArrayList<>(students);
        
        switch (option) {
            case "1":
                sortedList.sort(Comparator.comparing(Student::getName));
                System.out.println("Sorted by Name (A-Z):");
                break;
                
            case "2":
                sortedList.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
                System.out.println("Sorted by Average (High to Low):");
                break;
                
            case "3":
                sortedList.sort(Comparator.comparingDouble(Student::getAverage));
                System.out.println("Sorted by Average (Low to High):");
                break;
                
            case "4":
                sortedList.sort(Comparator.comparing(Student::getGradeLevel));
                System.out.println("Sorted by Grade Level:");
                break;
                
            default:
                System.out.println("Invalid option!");
                return;
        }
        
        displaySortedResults(sortedList);
    }
    
    private void displaySortedResults(List<Student> sortedList) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SORTED STUDENT LIST");
        System.out.println("=".repeat(70));
        
        for (int i = 0; i < sortedList.size(); i++) {
            System.out.printf("%2d. %s\n", i + 1, sortedList.get(i));
        }
        
        System.out.println("=".repeat(70));
    }
    
    private Student findStudent() {
        System.out.print("Enter student ID or name: ");
        String query = scanner.nextLine().trim();
        
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(query)) {
                return student;
            }
        }
        
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(query)) {
                return student;
            }
        }
        
        System.out.println("Similar names:");
        boolean foundSimilar = false;
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                System.out.println("  - " + student.getName() + " (ID: " + student.getId() + ")");
                foundSimilar = true;
            }
        }
        
        if (!foundSimilar) {
            System.out.println("No students found!");
        }
        
        return null;
    }
}

// ==================== MAIN CLASS ====================
public class StudentSys {
    public static void main(String[] args) {
        System.out.println("\n" + "*".repeat(70));
        System.out.println("    WELCOME TO STUDENT GRADE MANAGEMENT SYSTEM WITH LOGIN");
        System.out.println("*".repeat(70));
        
        LoginSystem loginSystem = new LoginSystem();
        
        // Main application loop
        while (true) {
            if (loginSystem.showLoginMenu()) {
                User currentUser = loginSystem.getCurrentUser();
                GradeManagementSystem gradeSystem = new GradeManagementSystem(currentUser);
                gradeSystem.displayMainMenu(loginSystem);
                
                System.out.println("\nLogged out successfully!");
            }
        }
    }
}