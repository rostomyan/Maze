
public class UnionFind {

    private final int[] set;

    public UnionFind(int elem) {
        set = new int[elem];

        for (int i = 0; i < set.length; ++i) {
            set[i] = -1;
        }
    }

    public int find(int r) {
        if (set[r] < 0)
            return r;         
        else
            return set[r] = find(set[r]);        
    }

    public void unionRooms(int roomA, int roomB) {
        if (set[roomB] < set[roomA]) {
            set[roomA] = roomB;
        } 
        else {
            if (set[roomA] == set[roomB]) {
                set[roomA]--;
            }
            set[roomB] = roomA;
        }
    }
}
