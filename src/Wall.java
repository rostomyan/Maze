
public class Wall {

    public Room currentRoom, nextRoom;
    public boolean isGone = false;

    public Wall(Room a, Room b) {
        currentRoom = a;
        nextRoom = b;
    }

    public Wall(Room r) {
        currentRoom = r;
        nextRoom = null;
    }
}
