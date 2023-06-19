import java.math.BigDecimal;

public class Room {
    private int roomNumber, floor, roomID;
    private boolean occupied;
    private String service;
    private BigDecimal price;



    public Room(int roomID, int roomNumber, boolean occupied, String service, int floor, BigDecimal price) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.occupied = occupied;
        this.service = service;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return "Room: " + roomNumber + ", Floor: " + floor + ", Occupied: " + occupied + ", Service: " + service;
    }
}
