import org.postgresql.util.PSQLException;

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
    private static Scanner sc = new Scanner(System.in);

    // this variable is for the choices on what table will be displayed
    private static int choice;

    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";

    // the following variables that represents the int columns in the reservations table
    private static int reservationID, roomID, userID, reservationPrice, payment, customerUserID;

    // the following variables that represents the date type columns in the reservations table
    private static Date startDate, endDate, reservationDate;

    private static String customerName, phoneNumber;

    private static Timestamp createdAt;

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
                        // a handler if the table doesn't exist
                        catch (PSQLException e) {
                            System.out.println("The following table doesn't exist");
                        }
                        // a handler for any sql errors
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
                        displayRooms();
                        break;
                    case 4:
                        displayRoomServices();
                        break;
                    case 5:
                        displayEmployeeList();
                        break;
                    case 6:
                        displayReservedRecords();
                        break;
                    case 7:
                        sc.close();
                        System.out.println("Exiting Hotel Room Reservation Management...");
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
        } while (choice < 1 || choice > 7);
    }

    private static void displayMenu() {
        System.out.println("------ Menu ------" +
                "\n1. Display Reserved Rooms" +
                "\n2. Display Customer Records" +
                "\n3. Display Rooms" +
                "\n4. Display Room Services" +
                "\n5. Display Employee List" +
                "\n6. Display Reservation Records" +
                "\n7. Exit");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    private static void displayReservedRooms() throws SQLException {
        System.out.println("------ Reserved Rooms ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".reservations";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("No reservations found.");
            }
            else {
                while (resultSet.next()) {
                    reservationID = resultSet.getInt("reservation_id");
                    userID = resultSet.getInt("user_id");
                    roomID = resultSet.getInt("room_id");
                    startDate = resultSet.getDate("check_in_date");
                    endDate = resultSet.getDate("check_out_date");
                    reservationDate = resultSet.getDate("reservation_date");
                    reservationPrice = resultSet.getInt("reservationprice");
                    payment = resultSet.getInt("payment");

                    System.out.println("Reservation ID: " + reservationID + "\nCustomer ID: " + userID + "\nRoom ID: " + roomID + "\nCheck-in Date: " + startDate + "\nCheck-out Date: " + endDate + "\nReservation Date: " + reservationDate + "\nReservation Price: " + reservationPrice + "\nAmount Paid: " + payment);
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
            System.out.print("Do you want to add or remove a reservation? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                updateReservation(reservationID, userID, roomID, startDate, endDate, reservationDate, reservationPrice, payment);
            }
            else if (choice.equalsIgnoreCase("N")) {
                main(null);
            }
            else {
                System.out.println("Please choose the correct input.");
            }
        }
    }

    private static void displayCustomerRecords() throws SQLException {
        System.out.println("------ Customer Records ------");

        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("No reservations found.");
            }
            else {
                while (resultSet.next()) {
                    reservationID = resultSet.getInt("reservation_id");
                    userID = resultSet.getInt("user_id");
                    roomID = resultSet.getInt("room_id");
                    startDate = resultSet.getDate("check_in_date");
                    endDate = resultSet.getDate("check_out_date");
                    reservationDate = resultSet.getDate("reservation_date");
                    reservationPrice = resultSet.getInt("reservationprice");
                    payment = resultSet.getInt("payment");

                    System.out.println("Reservation ID: " + reservationID + "\nCustomer ID: " + userID + "\nRoom ID: " + roomID + "\nCheck-in Date: " + startDate + "\nCheck-out Date: " + endDate + "\nReservation Date: " + reservationDate + "\nReservation Price: " + reservationPrice + "\nAmount Paid: " + payment);
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
    }

    private static void displayRooms() {
        System.out.println("------ Rooms ------");
    }

    private static void displayRoomServices() {
        System.out.println("------ Room Services ------");
    }

    private static void displayEmployeeList() {
        System.out.println("------ Employees ------");
    }

    private static void displayReservedRecords() {
        System.out.println("------ Reservation Records ------");
    }

    private static void updateReservation(int reservationID, int userID, int roomID, Date newStartDate, Date newEndDate, Date newReservationDate, int newReservationPrice, int newPayment) {
        try {
            String sql = "UPDATE \"hotelReservationOfficial\".\"hotelSchema\".reservations SET check_in_date = ?, check_out_date = ?, reservation_date = ?, reservationprice = ?, payment = ? WHERE reservation_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date( newStartDate.getTime()));
            statement.setDate(2, new java.sql.Date( newEndDate.getTime()));
            statement.setDate(3, new java.sql.Date( newReservationDate.getTime()));
            statement.setInt(4, newReservationPrice);
            statement.setInt(5, newPayment);
            statement.setInt(6, reservationID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully.");
            }
            else {
                System.out.println("Failed to update reservation");
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
