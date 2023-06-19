public class Room {
    private int roomNumber, floor, roomID;
    private boolean occupied;
    private String service;

    public Room(int roomID, int roomNumber, boolean occupied, String service, int floor) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.occupied = occupied;
        this.service = service;
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
