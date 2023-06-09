// importing util package and sql package
import java.util.Scanner;
import java.sql.*;

// main class
public class Employee {
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/HotelManagementDatabase";

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

            // prompt for the employee ID
            employeeID = promptForEmployeeId();

            // validate employee ID
            boolean isValidEmployee = validateEmployeeId(connection, employeeID);


            if (isValidEmployee) {
                // successful log-in
                System.out.println("Log-in successful.");

                // redirect to hotel room management and table viewing
            }
            else {
                // invalid employee ID
                System.out.println("Invalid employee ID. Please try again");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int promptForEmployeeId() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! Please log-is first." + "\nEnter Employee ID:");
        employeeID = sc.nextInt();
        return employeeID;
    }

    private static boolean validateEmployeeId(Connection connection, int employeedID) throws SQLException {
        // prepare the SQL statement to validate the employee ID
        String sql = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";

        // create a prepared statement
        PreparedStatement statement =  connection.prepareStatement(sql);
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