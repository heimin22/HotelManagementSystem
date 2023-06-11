// importing util package and sql package
import java.util.InputMismatchException;
import java.util.regex.*;
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

    private static String formattedID;


    // main method
    public static void main(String[] args) {
        try {
            // using the jdbc for the database connection
            Class.forName("org.postgresql.Driver");
            // establishing database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            formattedID = formatPassword(employeeID);

            // used for the while loops if the id is matched
            boolean matchedID = false;

            // an object for accessing the hotel room management class
            HotelRoomManagement hotelRoomManagement = new HotelRoomManagement();

            // while loop for the entering the employee ID
            while (!matchedID) {
                // prompt for the employee ID
                employeeID = promptForEmployeeId();

                // validate employee ID
                boolean isValidEmployee = validateEmployeeId(connection, employeeID);

                if (isValidEmployee) {
                    // successful log-in
                    String employeeName = getEmployeeName(connection, employeeID);
                    System.out.println("Log-in successful.\n");
                    System.out.println("Hello " + employeeName + "!");
                    hotelRoomManagement.main(args);
                    break;
                    // redirect to hotel room management and table viewing

                }
                else {
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
        // Scanner object
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Hello! Please log-is first." + "\nEnter Employee ID: ");
            employeeID = sc.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Please enter the correct format");
        }
        return employeeID;
    }

    private static boolean validateEmployeeId(Connection connection, int employeeID) throws SQLException {
        // prepare the SQL statement to validate the employee ID
        String sql = "SELECT COUNT(*) FROM \"hotelReservationOfficial\".\"hotelSchema\".employees WHERE employeeid = ?";

        // create a prepared statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, employeeID);

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


    // formatting the password
    private static String formatPassword(int employeeID) {
        // defining the regex pattern for the employee ID. Example format is "123456" so 6 numbers are allowed
        String regexPattern = "(\\d{6})";

        // this will be the pattern object with the following regex pattern which is all numbers that occur 6 times
        Pattern pattern = Pattern.compile(regexPattern);

        // the value of isString is the converted String value of employeeID
        String isString = String.valueOf(employeeID);

        // match the ID against the pattern
        Matcher matcher = pattern.matcher(isString);

        // check if the id matches the following pattern
        if (matcher.matches()) {
            // if the following ID matches the pattern then the value of formatted ID is equals to the matched group
            String formattedID = matcher.group(1);

            // returning the formatted ID
            return formattedID;
        }
        else {
            // if the ID did not match the pattern then it will return the original id
            return isString;
        }
    }

    private static String getEmployeeName(Connection connection, int employeeID) throws SQLException{
        // preparing the sql statement to find the name that matches according to the ID
        String sql = "SELECT employeename FROM \"hotelReservationOfficial\".\"hotelSchema\".employees WHERE employeeID = ?";

        // creating a prepared statement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, employeeID);

        // execute the query and retrieve the result
        ResultSet resultSet = preparedStatement.executeQuery();

        String employeeName = " ";
        // if any ID matches the corresponding employee name then it will be the employeeName's value
        if (resultSet.next()) {
            employeeName = resultSet.getString("employeename");
        }

        // closing the resultSet and preparedStatement
        resultSet.close();
        preparedStatement.close();

        return employeeName;
    }
}

