// importing util package and sql package

import java.util.Scanner;
import java.sql.*;

// main class
public class Employee {
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";

    // the employee's id is their employeeid in the database table "employees" in column "employeeid"
    private static int employeeID;

    // main method
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            // establishing database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            boolean matchedID = false;
            HotelRoomManagement hotelRoomManagement = new HotelRoomManagement();

            while (!matchedID) {
                // prompt for the employee ID
                employeeID = promptForEmployeeId();

                // validate employee ID
                boolean isValidEmployee = validateEmployeeId(connection, employeeID);

                if (isValidEmployee) {
                    // successful log-in
                    System.out.println("Log-in successful.");
                    break;
                    // redirect to hotel room management and table viewing

                } else {
                    // invalid employee ID
                    System.out.println("Invalid employee ID. Please try again\n");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex1) {
            throw new RuntimeException(ex1);
        }
        catch (NumberFormatException ex2) {
            System.out.println(ex2.getMessage());
        }

    }

    // log-in method
    private static int promptForEmployeeId() throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean matchedID = false;
        try {
            System.out.print("Hello! Please log-is first." + "\nEnter Employee ID: ");
            employeeID = sc.nextInt();

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return employeeID;
    }

    private static boolean validateEmployeeId(Connection connection, int employeedID) throws SQLException {
        // prepare the SQL statement to validate the employee ID
        String sql = "SELECT COUNT(*) FROM \"hotelReservationOfficial\".\"hotelSchema\".employees WHERE employeeid = ?";

        // create a prepared statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeedID);

        // execute the query and retrieve the result
        ResultSet resultSet = statement.executeQuery();

        // check if the employee ID exists in the database
        boolean isValidEmployee = false;
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            isValidEmployee = count > 0;
        }

        // close the result set and statement
        resultSet.close();
        statement.close();

        return isValidEmployee;
    }
}