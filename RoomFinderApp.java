import java.sql.*;  // Import necessary classes for JDBC
import java.util.Scanner;  // Import Scanner for user input

public class RoomFinderApp {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/RoomFinder";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    public static void main(String[] args) {
        // Try-with-resources to automatically close resources
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {  // Infinite loop for the menu
                // Display the menu options
                System.out.println("\nRoom Finder Application");
                System.out.println("1. Register Room");
                System.out.println("2. Search Rooms");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();  // Get user input for menu choice

                // Switch case to handle user choice
                switch (choice) {
                    case 1:
                        registerRoom(connection, scanner);  // Call method to register a room
                        break;
                    case 2:
                        searchRooms(connection, scanner);  // Call method to search for rooms
                        break;
                    case 3:
                        System.out.println("Exiting...");  // Exit the application
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");  // Invalid input handling
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print SQL exception stack trace
        }
    }

    // Method to register a room
    private static void registerRoom(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();  // Consume newline character left by nextInt()
            // Prompt user for room details
            System.out.print("Enter Room Type (e.g., 1 BHK, 2 BHK): ");
            String roomType = scanner.nextLine();  // Read room type
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();  // Read room price
            scanner.nextLine();  // Consume newline
            System.out.print("Enter Location: ");
            String location = scanner.nextLine();  // Read location
            System.out.print("Enter Contact Details: ");
            String contactDetails = scanner.nextLine();  // Read contact details

            // SQL query to insert room details into the database
            String sql = "INSERT INTO Rooms (roomType, price, location, contactDetails) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // Set parameters for the PreparedStatement
                stmt.setString(1, roomType);
                stmt.setDouble(2, price);
                stmt.setString(3, location);
                stmt.setString(4, contactDetails);
                stmt.executeUpdate();  // Execute the insert operation
                System.out.println("Room registered successfully.");  // Success message
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print SQL exception stack trace
        }
    }

    // Method to search for rooms based on location
    private static void searchRooms(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();  // Consume newline
            System.out.print("Enter Location to search: ");  // Prompt for location
            String location = scanner.nextLine();  // Read location input

            // SQL query to select rooms matching the location
            String sql = "SELECT * FROM Rooms WHERE location LIKE ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "%" + location + "%");  // Use wildcard search for flexibility
                try (ResultSet rs = stmt.executeQuery()) {  // Execute query and get result set
                    boolean roomsFound = false;  // Flag to check if any rooms are found
                    while (rs.next()) {  // Iterate through the result set
                        roomsFound = true;  // Set flag to true if at least one room is found
                        // Display room details
                        System.out.println("\nRoom ID: " + rs.getInt("id"));
                        System.out.println("Room Type: " + rs.getString("roomType"));
                        System.out.println("Price: " + rs.getDouble("price"));
                        System.out.println("Location: " + rs.getString("location"));
                        System.out.println("Contact Details: " + rs.getString("contactDetails"));
                    }
                    if (!roomsFound) {
                        System.out.println("No rooms found in the specified location.");  // Message if no rooms are found
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print SQL exception stack trace
        }
    }
}
