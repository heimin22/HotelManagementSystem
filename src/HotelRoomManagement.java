import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class HotelRoomManagement {
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String TABLE_ROOM_MANAGEMENT = "room_management";
    private static final String TABLE_ROOM_SERVICES = "room_services";
    private static final String TABLE_USERS = "users";
    private static Connection connection;
    private static Scanner sc = new Scanner(System.in);

    private static int choice;

    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";

    private static int reservationID, roomID, userID, reservationPrice, payment;

    private static Date startDate, endDate, reservationDate;


    public HotelRoomManagement(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        System.out.println("------ Hotel Room Management ------");
        boolean exit = false;

        do {
            try {
                displayMenu();
                choice = getUserChoice();
                switch (choice) {
                    case 1:
                        try {
                            displayReservedRooms();
                        }
                        catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 2:
                        displayCustomerRecords();
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
            String sql = "SELECT * FROM reservations";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);


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

            resultSet.close();
            statement.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Reservations are empty.");
        }

        System.out.println("Do you want to add or remove a reservation? (Y/N)");
        String choice = sc.next();

        if (choice.equalsIgnoreCase("Y")) {
            updateReservation(reservationID, userID, roomID, startDate, endDate, reservationDate, reservationPrice, payment);
        }
        else if (choice.equalsIgnoreCase("N")) {
            main(null);
        }
    }

    private static void displayCustomerRecords() {
        System.out.println("------ Customer Records ------");
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

    private static void updateReservation(int reservationID, int userID, int roomID, Date startDate, Date endDate, Date reservationDate, int reservationPrice, int payment) {

    }
}
