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
    private Scanner sc;

    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";


    public HotelRoomManagement(Connection connection) {
        this.connection = connection;
    }
    public static void main(String[] args) {

    }

    private void displayMenu() {
        System.out.println("------ Menu ------" + "\n1. Display Reserved Rooms" + "\n2. Released Occupied Rooms" + "\n3. Room Service Rooms" + "\n4. Exit");
    }



}
