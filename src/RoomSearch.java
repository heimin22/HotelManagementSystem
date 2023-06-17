import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomSearch {
    private List<Room> rooms;
    private Connection connection;

    public RoomSearch(Connection connection) {
        this.connection = connection;
        rooms = new ArrayList<>();
        initializeRooms();
    }

    private void initializeRooms() {
        try {
            String sql = "SELECT * FROM \"hotelReservationOfficial\".\"hotelSchema\".rooms";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            do {
                int roomNumber = resultSet.getInt("room_number");
                int floorNumber = resultSet.getInt("floor_number");
                String service = resultSet.getString("room_service");
                boolean occupied = resultSet.getBoolean("is_available");
                Room room = new Room(roomNumber, floorNumber, occupied, service);
                rooms.add(room);
            }
            while (resultSet.next());
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
            if (!room.isOccupied() && room.getService().equalsIgnoreCase(service) && room.getFloorNumber() == floorNumber) {
                availableRooms.add(room);
            }
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

    public boolean reserveRoom(int roomNumber, int days, int nights) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null && !room.isOccupied()) {
            room.setOccupied(true);
            String reservationId = generateReservationID();
            System.out.println("Reservation successful!" + "\nReservation ID: " + reservationId);
            return true;
        }
        else {
            System.out.println("Room " + roomNumber + " is not available for reservation.");
            return false;
        }
    }

    private Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private String generateReservationID() {

    }
}