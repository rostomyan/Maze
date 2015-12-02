
import java.util.LinkedList;
import java.util.List;

public class Room {

    public Wall north, east, south, west;
    public int row, col;
    public List<Room> roomList;
    public int roomNum;
    public Room prev;

    public Room(int x, int y) {
        this.row = x;
        this.col = y;
        roomList = new LinkedList<>();
        prev = null;
        roomNum = 0;
    }

    public int getRoomNum() {
        return roomNum++;
    }
}
