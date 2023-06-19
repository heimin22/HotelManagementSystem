import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomSearch {
    private List<Room> rooms;
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";
    private static Connection connection;

    public RoomSearch(Connection connection) {
        this.connection = connection;
        rooms = new ArrayList<>();
        initializeConnection();
        initializeRooms();
    }

    private void initializeConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeRooms() {
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) { // Move the cursor to the next row
                int roomID = resultSet.getInt("room_id");
                int roomNumber = resultSet.getInt("room_number");
                int floor = resultSet.getInt("floor");
                String service = resultSet.getString("room_service");
                boolean occupied = resultSet.getBoolean("is_available");
                Room room = new Room(roomID, roomNumber, occupied, service, floor);
                rooms.add(room);
            }

            statement.close();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Room> searchAvailableRooms(String service, int floorNumber) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (!room.isOccupied() && room.getService().equalsIgnoreCase(service) && room.getFloor() == floorNumber) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Room> searchAvailableRooms(String service) {
        List<Room> availableRooms = new ArrayList<>();
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms " + "WHERE room_service = ? AND is_available = true";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,service);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int roomID = resultSet.getInt("room_id");
                int roomNumber = resultSet.getInt("room_number");
                boolean occupied = resultSet.getBoolean("is_available");
                int floor = resultSet.getInt("floor");
                Room room = new Room(roomID, roomNumber, occupied, service, floor);
                availableRooms.add(room);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return availableRooms;
    }

    public List<Room> getAllAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (!room.isOccupied()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public boolean reserveRoom(int roomNumber, int days, int nights, int userID, BigDecimal payment) throws SQLException {
        Room room = getRoomByNumber(roomNumber);
        if (room != null && !room.isOccupied()) {
            room.setOccupied(true);

            // calculate check-in and check-out dates
            LocalDate checkInDate = LocalDate.now();
            LocalDate checkOutDate = checkInDate.plusDays(days + nights);

            BigDecimal roomPrice = calculateRoomPrice(room, payment);
            // save reservation to the database
            try {
                String sql = "INSERT INTO \"hotelReservationOfficial\".\"hotelSchema\".reservations" + "room_id, user_id, check_in_date, check_out_date, reservation_date, reservationprice, payment) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, room.getRoomID());
                statement.setInt(2, userID);
                statement.setDate(3, java.sql.Date.valueOf(checkInDate));
                statement.setDate(4, java.sql.Date.valueOf(checkOutDate));
                statement.setTimestamp(5, java.sql.Timestamp.from(java.time.Instant.now()));
                statement.setBigDecimal(6, roomPrice);
                statement.setBigDecimal(7, payment);

                statement.executeUpdate();
                statement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            int reservationID = generateReservationID();
            System.out.println("Reservation successful!" + "\nReservation ID: " + reservationID);
            return true;
        }
        else {
            System.out.println("Room " + roomNumber + " is not available for reservation.");
            return false;
        }
    }

    private BigDecimal calculateRoomPrice(Room room, BigDecimal payment) {
        BigDecimal roomPrice = BigDecimal.ZERO;
        String roomType = room.getService();

        if (roomType.equalsIgnoreCase("Single Rooms")) {
            roomPrice = BigDecimal.valueOf(8000);
        }
        else if (roomType.equalsIgnoreCase("Twin or Double Rooms")) {
            roomPrice = BigDecimal.valueOf(15000);
        }
        else if (roomType.equalsIgnoreCase("Studio Rooms")) {
            roomPrice = BigDecimal.valueOf(25000);
        }
        else if (roomType.equalsIgnoreCase("Deluxe Rooms")) {
            roomPrice = BigDecimal.valueOf(40000);
        }
        else if (roomType.equalsIgnoreCase("Presidential Suite")) {
            roomPrice = BigDecimal.valueOf(55000);
        }

        BigDecimal numDays = payment.divide(roomPrice, RoundingMode.DOWN);

        return roomPrice.multiply(numDays);
    }

    private Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private static int generateReservationID() throws SQLException {
        try {
        // this is the set of the following numbers in a string type
        String existingIDs = "0123456789";
        // maximum id length is 4
        int idLength = 4;

        // accessing the customer ID
        String sql = "SELECT MAX (room_id) FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms";

        // establishing the statement connection
        Statement statement = connection.createStatement();

        // // the resultset will be used to execute the following SQL statement to access the following variables
        ResultSet resultSet = statement.executeQuery(sql);

        // 0 is the default value
        int maxRoomID = 0;
        // if there's an available slot for the customer id then the statement below will be executed
        if (resultSet.next()) {
            maxRoomID = resultSet.getInt(1);
        }

        // increment it to 1 to grant the system to generate a unique ID
        int newUserID = maxRoomID + 1;

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
}