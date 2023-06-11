import java.util.Scanner;
import java.sql.*;

public class HotelRoomManagement {
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String TABLE_ROOM_MANAGEMENT = "room_management";
    private static final String TABLE_ROOM_SERVICES = "room_services";
    private static final String TABLE_USERS = "users";
    private Connection connection;
    private static Scanner sc = new Scanner(System.in);

    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";


    public HotelRoomManagement(Connection connection) {
        this.connection = connection;
    }
    public static void main(String[] args) {


        boolean exit = false;
        while (!exit) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:

            }
        }
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

    private void displayReservedRooms() {

    }

    private void displayCustomerRecords() {

    }

    private void displayRooms() {

    }
    private void displayRoomServices() {

    }

    private void displayEmployeeList() {

    }

    private void displayReservedRecords() {

    }




}
