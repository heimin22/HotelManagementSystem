public class Room {
    private int roomNumber, floorNumber;
    private boolean occupied;
    private String service;

    public Room(int roomNumber, int floorNumber, boolean occupied, String service) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.occupied = occupied;
        this.service = service;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
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
        return "Room: " + roomNumber + ", Floor: " + floorNumber + ", Occupied: " + occupied + ", Service: " + service;
    }
}