import org.postgresql.util.PSQLException;

import javax.swing.plaf.nimbus.State;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class HotelRoomManagement {
    // these are the following database table names. These can be used later if needed
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String TABLE_ROOM_MANAGEMENT = "room_management";
    private static final String TABLE_ROOM_SERVICES = "room_services";
    private static final String TABLE_USERS = "users";

    // connection for establishing connection to the database and tables
    private static Connection connection;

    // global scanner
    private static final Scanner sc = new Scanner(System.in);

    // this variable is for the choices on what table will be displayed
    private static int choice;

    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";

    // the following variables that represents the int columns from the following tables
    private static int reservationID, roomID, userID, customerUserID, serviceID, employeeID, floor, capacity;

    // these variables are used for price columns in the
    private static double reservationPrice, servicePrice, payment;

    // the following variables that represents the dates about the check-in, check-out, and the reservation date
    private static Date startDate, endDate, reservationDate;

    // these variables are representing the customer's name, service name, names of the employees, their position, and the phone numbers of the customers
    private static String customerName, phoneNumber, roomNumber, roomService, serviceName, employeeName, employeePosition;

    // the timestamp is when the customer user information was created
    private static Timestamp createdAt;

    // this variable is used to confirm if the room is available or not
    private static boolean isAvailable;

    // constructor for the connection
    public HotelRoomManagement(Connection connection) {
        this.connection = connection;
    }

    // main method
    public static void main(String[] args) throws SQLException {
        System.out.println("\n------ Hotel Room Management ------");

        // establishing the connection for the database
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // a do-while loop for the choices
        do {
            // as the user will select the following choice, each cases has an exception handler to handle any exceptions
            try {
                // displaying the menu
                displayMenu();
                // using the choice for calling the getUseChoice method and assigning the value for the variable
                choice = getUserChoice();
                // switch case
                switch (choice) {
                    case 1:
                        try {
                            displayReservedRooms();
                        }
                        // a handler if the table doesn't exist same as the following cases below
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        // a handler for any sql errors same as the following cases below too
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 2:
                        try {
                            displayCustomerRecords();
                        }
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 3:
                        try {
                            displayRooms();
                        }
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 4:
                        try {
                            displayRoomServices();
                        }
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 5:
                        try {
                            displayEmployeeList();
                        }
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 6:
                        try {
                            displayReservedRecords();
                        }
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        catch (SQLException ex1) {
                            throw new RuntimeException(ex1);
                        }
                        break;
                    case 7:
                        System.out.println("Exiting Hotel Room Reservation Management...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.\n");
                        break;
                }
            }
            // this handler handles any wrong format exceptions
            catch (InputMismatchException e) {
                System.out.println("Wrong format entered. Please try again.\n");
                sc.nextLine();
            }
        }
        while (choice < 1 || choice > 7);
    }

    // this method is used for displaying the menu of which table will be displayed
    private static void displayMenu() {
        System.out.println("\n------ Menu ------" +
                "\n1. Display Reserved Rooms" +
                "\n2. Display Customer Records" +
                "\n3. Display Rooms" +
                "\n4. Display Room Services" +
                "\n5. Display Employee List" +
                "\n6. Display Reservation Records" +
                "\n7. Exit");
    }

    // this method is used for getting the choice of the user of which table they chose to display
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    // this method is used for displaying the reservations table this also handles some exception
    private static void displayReservedRooms() throws SQLException {
        System.out.println("------ Reserved Rooms ------");

        // the following statements below are accessing the data in the reservations table
        try {
            // preparing an SQL code statement for accessing the table
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".reservations";

            // establishing the statement connection
            Statement statement = connection.createStatement();

            // the resultset will be used to execute the following SQL statement to access the following variables
            ResultSet resultSet = statement.executeQuery(sql);

            // the following statement will be executed if there are no data found inside the table
            if (!resultSet.next()) {
                System.out.println("No reservations found.");
            }
            // else the resultset will get the information inside the table and the data will be shown
            else {
                while (resultSet.next()) {
                    reservationID = resultSet.getInt("reservation_id");
                    userID = resultSet.getInt("user_id");
                    roomID = resultSet.getInt("room_id");
                    startDate = resultSet.getDate("check_in_date");
                    endDate = resultSet.getDate("check_out_date");
                    reservationDate = resultSet.getDate("reservation_date");
                    reservationPrice = resultSet.getDouble("reservationprice");
                    payment = resultSet.getDouble("payment");

                    System.out.println("Reservation ID: " + reservationID + "\nCustomer ID: " + userID + "\nRoom ID: " + roomID + "\nCheck-in Date: " + startDate + "\nCheck-out Date: " + endDate + "\nReservation Date: " + reservationDate + "\nReservation Price: " + reservationPrice + "\nAmount Paid: " + payment);

                    System.out.println();
                }
            }

            // closing the resultset and the statement
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Reservations are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a reservation? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateReservation(reservationID, userID, roomID, startDate, endDate, reservationDate, reservationPrice, payment);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    // this method is used for displaying the reservations table this also handles some exception
    private static void displayCustomerRecords() throws SQLException {
        System.out.println("------ Customer Records ------");

        // the following statements below are accessing the data in the reservations table
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("No customer records found.");
            }
            else {
                while (resultSet.next()) {
                    customerUserID = resultSet.getInt("customer_user_id");
                    customerName = resultSet.getString("customer_name");
                    phoneNumber = resultSet.getString("phone_number");
                    createdAt = resultSet.getTimestamp("created_at");
                    System.out.println("Customer ID: " + customerUserID + "\nCustomer Name: " + customerName + "\nPhone Number: " + phoneNumber + "\nCreated At: " + createdAt);
                }
            }

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Reservations are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a customer information? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateCustomers(customerUserID, customerName, phoneNumber, createdAt);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void displayRooms() throws SQLException{
        System.out.println("------ Rooms ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("Rooms are empty.");
            }
            else {
                while (resultSet.next()) {
                    roomID = resultSet.getInt("room_id");
                    floor = resultSet.getInt("floor");
                    roomNumber = resultSet.getString("room_number");
                    capacity = resultSet.getInt("capacity");
                    roomService = resultSet.getString("room_service");
                    isAvailable = resultSet.getBoolean("is_available");
                    System.out.println("Room ID: " + roomID + "\nFloor: " + floor + "\nRoom Number: " + roomNumber + "\nCapacity: " + capacity + "\nRoom Service: " + roomService + "Availability: " + isAvailable);
                    System.out.println();
                }
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Rooms are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a customer information? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateRooms(roomID, floor, roomNumber, capacity, roomService, isAvailable);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void displayRoomServices() throws SQLException {
        System.out.println("------ Room Services ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".room_services";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("Services are empty.");
            }
            else {
                while (resultSet.next()) {
                    serviceID = resultSet.getInt("service_id");
                    serviceName = resultSet.getString("service_name");
                    servicePrice = resultSet.getDouble("price");
                    floor = resultSet.getInt("floor");

                    System.out.println("Service ID: " + serviceID + "\nService Name: " + serviceName + "\nService Price: " + servicePrice + "\nFloor: " + floor);
                    System.out.println();
                }
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Services are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a customer information? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateServices(serviceID, serviceName, servicePrice, floor);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void displayEmployeeList() throws SQLException {
        System.out.println("------ Employees ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".employees";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("Employees are empty.");
            }
            else {
                while (resultSet.next()) {
                    employeeID = resultSet.getInt("employeeid");
                    employeeName = resultSet.getString("employeename");
                    employeePosition = resultSet.getString("position");

                    System.out.println("Employee ID: " + employeeID + "\nEmployee Name: " + employeeName + "\nPosition: " + employeePosition);
                    System.out.println();
                }
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Employees are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a customer information? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateEmployees(employeeID, employeeName, employeePosition);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void displayReservedRecords() throws SQLException {
        System.out.println("------ Reservation Records ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".room_management";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("Reserved Records are empty.");
            }
            else {
                while (resultSet.next()) {
                    reservationID = resultSet.getInt("reservation_id");
                    customerUserID = resultSet.getInt("customer_id");
                    roomID = resultSet.getInt("room_id");
                    startDate = resultSet.getDate("check_in_date");
                    endDate = resultSet.getDate("check_out_date");

                    System.out.println("Reservation ID: " + reservationID + "\nCustomer ID: " + customerUserID + "\nRoom ID: " + roomID + "\nCheck-in Date: " + startDate + "\nCheck-out Date: " + endDate);
                    System.out.println();
                }
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Reserved Records are empty.");
        }

        boolean confirmation = false;

        while (!confirmation) {
            System.out.print("Do you want to add or remove a customer information? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateReservedRecords(reservationID, customerUserID, roomID, startDate, endDate);
                confirmation = true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void updateReservation(int reservationID, int userID, int roomID, Date newStartDate, Date newEndDate, Date newReservationDate, double newReservationPrice, double newPayment) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".reservations SET check_in_date = ?, check_out_date = ?, reservation_date = ?, reservationprice = ?, payment = ? WHERE reservation_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            if (newStartDate != null) {
                statement.setDate(1, new java.sql.Date(newStartDate.getTime()));
            } else {
                statement.setNull(1, Types.DATE);
            }

            if (newEndDate != null) {
                statement.setDate(2, new java.sql.Date(newEndDate.getTime()));
            } else {
                statement.setNull(2, Types.DATE);
            }

            if (newReservationDate != null) {
                statement.setDate(3, new java.sql.Date(newReservationDate.getTime()));
            } else {
                statement.setNull(3, Types.DATE);
            }

            statement.setDouble(4, newReservationPrice);
            statement.setDouble(5, newPayment);
            statement.setInt(6, reservationID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully.");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }

    private static void updateCustomers(int customerUserID, String newCustomerName, String newPhoneNumber, Timestamp newCreatedAt) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".users SET customer_name = ?, phone_number = ?, created_at = ? WHERE customer_user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newCustomerName);
            statement.setString(2, newPhoneNumber);
            statement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            statement.setInt(4, customerUserID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }

    private static void updateRooms(int roomID, int floor, String roomNumber, int capacity, String roomService, boolean newIsAvailable) throws SQLException {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".rooms SET room_id = ?, floor = ?, room_number = ?, capacity = ?, room_service = ?, is_available = ? WHERE room_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, roomID);
            statement.setInt(2, floor);
            statement.setString(3, roomNumber);
            statement.setInt(4, capacity);
            statement.setString(5, roomService);
            statement.setBoolean(6, newIsAvailable);
            statement.setInt(7, roomID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Rooms updated successfully");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }

    private static void updateServices(int serviceID, String newServiceName, double newServicePrice, int newFloor) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".room_services SET service_id = ?, service_name = ?, price = ?, floor = ? WHERE service_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, serviceID);
            statement.setString(2, newServiceName);
            statement.setDouble(3, newServicePrice);
            statement.setInt(4, newFloor);
            statement.setInt(5, serviceID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Services updated successfully");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }

    private static void updateEmployees(int employeeID, String newEmployeeName, String newEmployeePosition) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".employees SET employeeid = ?, employeename = ?, position = ? WHERE employeeid = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeID);
            statement.setString(2, newEmployeeName);
            statement.setString(3, newEmployeePosition);
            statement.setInt(4, employeeID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employees updated successfully");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }

    private static void updateReservedRecords(int reservationID, int customerUserID, int roomID, Date newStartDate, Date newEndDate) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".room_management SET reservation_id = ?, customer_id = ?, room_id = ?, check_in_date = ?, check_out_date = ? WHERE reservation_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, reservationID);
            statement.setInt(2, customerUserID);
            statement.setInt(3, roomID);

            if (newStartDate != null) {
                statement.setDate(4, new java.sql.Date(newStartDate.getTime()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            if (newEndDate != null) {
                statement.setDate(5, new java.sql.Date(newEndDate.getTime()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            statement.setInt(6, reservationID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employees updated successfully");
                main(null);
            }
            else {
                System.out.println("Failed to update reservation or nothing to be updated");
                main(null);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Error: Accessing a certain column for updating failed");
        }
    }
}
