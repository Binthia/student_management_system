import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = "Binthia";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void createTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    age INT,
                    department VARCHAR(255)
                )
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(String name, int age, String department) {
        String sql = "INSERT INTO students (name, age, department) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, department);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Department: %s\n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudent(int id, String name, int age, String department) {
        String sql = "UPDATE students SET name = ?, age = ?, department = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, department);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Student updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Student deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createTable();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();
                    addStudent(name, age, department);
                }
                case "2" -> viewStudents();
                case "3" -> {
                    System.out.print("Enter Student ID to Update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter New Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter New Age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter New Department: ");
                    String department = scanner.nextLine();
                    updateStudent(id, name, age, department);
                }
                case "4" -> {
                    System.out.print("Enter Student ID to Delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    deleteStudent(id);
                }
                case "5" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
