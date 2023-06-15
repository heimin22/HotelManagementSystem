import javax.print.attribute.standard.RequestingUserName;
import javax.sound.midi.Soundbank;
import java.util.*;
import java.sql.*;
import java.util.regex.*;


public class HotelReservation {
    private static final Scanner sc = new Scanner(System.in);
    private static String customerName, phoneNumber;
    private static int customerID;
    private static Timestamp createdAt;
    private static Connection connection;
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";


    // constructor for the connection
    public HotelReservation(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("---Welcome to STI Hotel!---");

        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

    }

    private static int generateUniqueUserID () throws SQLException{
        try {
            String existingIDs = "0123456789";
            int idLength = 7;

            String sql = "SELECT MAX (customer_user_id) FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int maxUserID = 0;
            if (resultSet.next()) {
                maxUserID = resultSet.getInt(1);
            }

            int newUserID = maxUserID + 1;

            statement.close();
            resultSet.close();

            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < idLength; i++) {
                int index = random.nextInt(existingIDs.length());
                sb.append(existingIDs.charAt(index));
            }

            String uniqueIDString = sb.toString();
            int uniqueID = Integer.parseInt(uniqueIDString);

            return uniqueID;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void userRegistration() throws SQLException {
        System.out.println("---Welcome to Hotel!---");

        boolean userRegistered = false;

        while (!userRegistered) {
            try {
                System.out.print("Please enter your name: ");
                customerName = sc.nextLine();

                System.out.print("Please enter your phone number: ");
                phoneNumber = sc.nextLine();

                customerID = generateUniqueUserID();

                createdAt = new Timestamp(System.currentTimeMillis());

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
}
