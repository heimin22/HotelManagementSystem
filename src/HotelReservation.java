import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;
import java.util.regex.*;
import java.text.DecimalFormat;

// main class
public class HotelReservation {
    private static final Scanner sc = new Scanner(System.in);
    private static String customerName, phoneNumber, serviceName, service;
    private static int customerID, floor, floorNumber, serviceID;
    private static double servicePrice;
    private static Timestamp createdAt;
    private static Connection connection;
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";
    private static RoomSearch roomSearch;

    // constructor for the connection

    public static void main(String[] args) throws SQLException {
        System.out.println("---Welcome to STI Hotel!---");

        // establishing the connection for the database
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        roomSearch = new RoomSearch(connection);

        // calling the user registration method
        userRegistration();

        displayServices();

        searchAvailableRooms();


    }

    // generating unique ID method
    private static int generateUniqueUserID () throws SQLException{
        try {
            // this is the set of the following numbers in a string type
            String existingIDs = "0123456789";
            // maximum id length is 7
            int idLength = 7;

            // accessing the customer ID
            String sql = "SELECT MAX (customer_user_id) FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            // establishing the statement connection
            Statement statement = connection.createStatement();

            // // the resultset will be used to execute the following SQL statement to access the following variables
            ResultSet resultSet = statement.executeQuery(sql);

            // 0 is the default value
            int maxUserID = 0;
            // if there's an available slot for the customer id then the statement below will be executed
            if (resultSet.next()) {
                maxUserID = resultSet.getInt(1);
            }

            // increment it to 1 to grant the system to generate a unique ID
            int newUserID = maxUserID + 1;

            statement.close();
            resultSet.close();

            // creating a StringBuilder object
            StringBuilder sb = new StringBuilder();
            // random object
            Random random = new Random();

            // generating a unique ID until it reaches the limit
            for (int i = 0; i < idLength; i++) {
                int index = random.nextInt(existingIDs.length());
                sb.append(existingIDs.charAt(index));
            }

            // the following unique ID that is an integer before will now be converted into a String
            String uniqueIDString = sb.toString();
            int uniqueID = Integer.parseInt(uniqueIDString);

            return uniqueID;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // return 0 if an error occurs
        return 0;
    }

    // user registration method
    private static void userRegistration() throws SQLException {

        boolean userRegistered = false;

        while (!userRegistered) {
            try {
                System.out.print("Please enter your name: ");
                customerName = sc.nextLine();

                System.out.print("Please enter your phone number: ");
                phoneNumber = sc.nextLine();

                // generate a unique customer ID
                customerID = generateUniqueUserID();

                // get the current timestamp as the creation date
                createdAt = new Timestamp(System.currentTimeMillis());

                // insert the information into the database
                String sql = "INSERT INTO \"hotelReservationOfficial\".\"hotelSchema\".users (customer_user_id, customer_name, phone_number, created_at)" + "VALUES (?, ?, ?,?)";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, customerID);
                statement.setString(2, customerName);
                statement.setString(3, phoneNumber);
                statement.setTimestamp(4, createdAt);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("You have been registered!");
                    System.out.println("Your Customer User ID is: " + customerID + "\nCreated at: " + createdAt);
                    userRegistered = true;
                }
                else {
                    System.out.println("Failed to register user. Please try again");
                    userRegistration();
                }

                statement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            catch (NullPointerException ex1) {
                System.out.println("Error accessing a certain column for user registration");
            }
            catch (InputMismatchException ex2) {
                System.out.println("Please enter the correct format");
                sc.nextLine();
            }
        }
    }

    private static void displayServices() throws SQLException {
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".room_services";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("Services are empty.");
            }
            else {
                do {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    serviceName = resultSet.getString("service_name");
                    servicePrice = resultSet.getDouble("price");
                    floor = resultSet.getInt("floor");
                    String formattedServicePrice = decimalFormat.format(servicePrice);

                    System.out.println("\nService Name: " + serviceName + "\nService Price: " + formattedServicePrice+ "\nFloor: " + floor);
                }
                while (resultSet.next());
            }

            statement.close();
            resultSet.close();


            String choice = getUserChoice();
            boolean confirmation = false;
            do {
                switch (choice.toUpperCase()) {
                    case "A" :
                        System.out.println("Single Rooms" + "\nRoom Rate per day: PHP 8,000.00" + "\nRoom Services: " + "\nLaundry" + "\nBuffet");
                        confirmation = true;
                        getUserChoice();
                        break;
                    case "B" :
                        System.out.println("Twin or Double Rooms" + "\nRoom Rate per day: PHP 15,000.00" + "\nRoom Services: " + "\nLaundry" + "\nBuffet" + "\nSwimming Pool Access");
                        confirmation = true;
                        getUserChoice();
                        break;
                    case "C" :
                        System.out.println("Studio Rooms" + "\nRoom Rate per day: PHP 25,000.00" + "\nRoom Services: " + "\nLaundry" + "\nBuffet" + "\nSwimming Pool Access" + "\nMini Bar" + "\nGym");
                        confirmation = true;
                        getUserChoice();
                        break;
                    case "D" :
                        System.out.println("Deluxe Rooms" + "\nRoom Rate per day: PHP 40,000.00" + "\nRoom Services: " + "\nLaundry" + "\nBuffet" + "\nSwimming Pool Access" + "\nMini Bar" + "\nSpa" + "\nGym");
                        confirmation = true;
                        getUserChoice();
                        break;
                    case "E" :
                        System.out.println("President Suite" + "\nRoom Rate per day: PHP 55,000.00" + "\nRoom Services: " + "\nLaundry" + "\nBuffet" + "\nSwimming Pool Access" + "\nMini Bar" + "\nSpa" + "\nGym" + "\nGolf Course");
                        confirmation = true;
                        getUserChoice();
                        break;
                    case "F" :
                        searchAvailableRooms();
                        confirmation = true;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        displayServices();
                }
            } while (!confirmation);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex1) {
            System.out.println("Services are empty.");
        }
        catch (InputMismatchException ex2) {
            System.out.println("Wrong input, please try again");
        }
    }

    private static void searchAvailableRooms() throws SQLException {
        System.out.println("\nAvailable Room Search");

        System.out.print("Enter the preferred service: ");
        service = sc.next();
        switch (service.toUpperCase()) {
            case "A" :
                service = "Single Rooms";
                break;
            case "B" :
                service = "Twin or Double Rooms";
                break;
            case "C" :
                service = "Studio Rooms";
                break;
            case "D" :
                service = "Deluxe Rooms";
                break;
            case "E" :
                service = "Presidential Suite";
                break;
            case "F" :
                displayServices();
                break;
            default :
                System.out.println("Please select a valid choice ");
        }
        sc.nextLine();

        List<Room> availableRooms = roomSearch.searchAvailableRooms(service);

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms found.");
        }
        else {
            System.out.println("Available Rooms: ");
            for (Room room : availableRooms) {
                System.out.println(room);
            }

            System.out.print("\nSelect the room number you want to reserve: ");
            int roomNumber = sc.nextInt();
            sc.nextLine();

            System.out.print("How many days do you want to stay: ");
            int days = sc.nextInt();
            sc.nextLine();

            System.out.print("From (YYYY-MM-DD): ");
            String startDate = sc.next();

            LocalDate checkInDate = LocalDate.parse(startDate);
            LocalDate checkOutDate = checkInDate.plusDays(days);

            boolean proceed = false;
            while (!proceed) {
                try {
                    System.out.print("\nAre you sure with the following purchase? (Y/N): ");
                    String confirmation = sc.next();

                    if (confirmation.equalsIgnoreCase("Y")) {
                        BigDecimal roomPrice = calculateRoomPrice(availableRooms, roomNumber, days);

                        System.out.println("\nTotal Price: " + roomPrice);
                        System.out.print("Please enter your payment: ");
                        BigDecimal payment = sc.nextBigDecimal();

                        BigDecimal change = payment.subtract(roomPrice);

                        System.out.println("Change: " + change);

                        int userID = customerID;
                        String userName = customerName;
                        String recPhoneNumber = phoneNumber;
                        int reservationID = roomSearch.generateReservationID();
                        String serviceReserved = service;
                        int nights = days - 1;
                        int floor = getFloorNumber(floorNumber);
                        checkOutDate = checkInDate.plusDays(days);
                        BigDecimal totalPrice = roomPrice;
                        BigDecimal amountPaid = payment;

                        roomSearch.reserveRoom(roomNumber, days, nights, userID, amountPaid);

                        createReceipt(userID, userName, recPhoneNumber, reservationID, serviceReserved, floor, roomNumber, checkInDate, checkOutDate, totalPrice, amountPaid);
                        proceed = true;
                    }
                    else if (confirmation.equalsIgnoreCase("N")) {
                        displayServices();
                        proceed = true;
                    }
                    else {
                        System.out.println("Please enter a valid answer.");
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println("Please enter a valid answer");
                }
            }
        }
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static BigDecimal calculateRoomPrice(List<Room> availableRooms, int roomNumber, int numDays) {
        for (Room room : availableRooms) {
            if (room.getRoomNumber() == roomNumber) {
                BigDecimal roomPrice = room.getPrice();
                return roomPrice.multiply(BigDecimal.valueOf(numDays));
            }
        }
        return BigDecimal.ZERO;
    }

    private static void createReceipt (int userID, String userName, String phoneNumber, int reservationID, String serviceReserved, int floor, int roomNumber, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalPrice, BigDecimal amountPaid) {
        Timestamp dateCreated = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringTimestamp = dateFormat.format(dateCreated);
        try {
            String directoryPath = "D:\\Hotel Management System\\Receipts";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filename = directoryPath + File.separator + "receipt_" + stringTimestamp.replace(':', '-') + "_"+ reservationID + ".txt";
            FileWriter fileWriter = new FileWriter(filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("Reservation Receipt");
            printWriter.println("-------------------");
            printWriter.println("Reservation ID: " + reservationID);
            printWriter.println("User ID: " + userID);
            printWriter.println("User Name: " + userName);
            printWriter.println("Phone Number: " + phoneNumber);
            printWriter.println("Service: " + serviceReserved);
            printWriter.println("Floor Number: " + floor);
            printWriter.println("Room Number: " + roomNumber);
            printWriter.println("Check-in Date: " + checkInDate);
            printWriter.println("Check-out Date: "  + checkOutDate);
            printWriter.println("Total Price: " + totalPrice);
            printWriter.println("Amount Paid: " + amountPaid);
            printWriter.println("Date Created: " + LocalDateTime.now());

            printWriter.close();
            System.out.println("Receipt created successfully. Please check on the file: " + filename);
            System.exit(0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUserChoice() {
        System.out.println("\nSelect the following number/services to view the details of the service or click \"F\" for available room search: ");
        System.out.println("A) Single Rooms\n" + "B) Twin or Double Rooms\n" + "C) Studio Rooms\n" + "D) Deluxe Rooms\n" + "E) Presidential Suite\n" + "F) Available Room Search\n");
        System.out.print("Response: ");
        String choice = sc.next();
        sc.nextLine();
        return choice;
    }


    private static int getUserID() throws SQLException {
        int userID = 0;
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                userID = resultSet.getInt("customer_user_id");
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    private static String getUserName() throws SQLException {
        String userName = "";
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                userName = resultSet.getString("customer_name");
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    private static String getPhoneNumber() throws SQLException {
        String phoneNumber = "";
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                phoneNumber = resultSet.getString("phone_number");
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    private static int getFloorNumber(int roomNumber) throws SQLException {
            String sql = "SELECT floor FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms WHERE room_number = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(roomNumber));

            ResultSet resultSet = statement.executeQuery();
            int floorNumberhere = 0;

            if (resultSet.next()) {
                floorNumberhere = resultSet.getInt("floor");
            }

            resultSet.close();
            statement.close();

        return floorNumberhere;
    }
}
