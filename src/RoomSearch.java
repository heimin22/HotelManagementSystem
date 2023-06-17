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
            }
            while (resultSet.next());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}