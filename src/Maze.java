
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Maze extends JPanel {

    private final Room[][] rooms;
    private final ArrayList<Wall> walls;
    private Random rand;
    private final int height;
    private final int width;
    private int incrementor;
    private UnionFind unionFind;
    public static Maze maze;

    private int x_cord;
    private int y_cord;
    private int roomSize;
    private int randomWall;

    public Maze(int h, int w) {
        this.height = h;
        this.width = w;
        rooms = new Room[height][width];
        walls = new ArrayList<>((height - 1) * (width - 1));
        generateRandomMaze();        
    }

    private void generateRandomMaze() {
        generateInitialRooms();
        unionFind = new UnionFind(width * height);
        rand = new Random();
        incrementor = width * height;

        while (incrementor > 1) {
            randomWall = rand.nextInt(walls.size());
            Wall temp = walls.get(randomWall);

            int roomA = temp.currentRoom.col + temp.currentRoom.row * width;
            int roomB = temp.nextRoom.col + temp.nextRoom.row * width;

            if (unionFind.find(roomA) != unionFind.find(roomB)) {
                walls.remove(randomWall);
                unionFind.unionRooms(unionFind.find(roomA), unionFind.find(roomB));
                temp.isGone = true;
                temp.currentRoom.roomList.add(temp.nextRoom);
                temp.nextRoom.roomList.add(temp.currentRoom);
                --incrementor;
            }
        }
    }

    private int roomNumber = 0;

    private void generateInitialRooms() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                rooms[i][j] = new Room(i, j);
                if (i == 0) {
                    rooms[i][j].north = new Wall(rooms[i][j]);
                } 
                else {
                    rooms[i][j].north = new Wall(rooms[i - 1][j], rooms[i][j]);
                    walls.add(rooms[i][j].north);
                }
                if (i == height - 1) {
                    rooms[i][j].south = new Wall(rooms[i][j]);
                }
                if (j == 0) {
                    rooms[i][j].west = new Wall(rooms[i][j]);
                } 
                else {
                    rooms[i][j].west = new Wall(rooms[i][j - 1], rooms[i][j]);
                    walls.add(rooms[i][j].west);
                }
                if (j == width - 1) {
                    rooms[i][j].east = new Wall(rooms[i][j]);
                }
                rooms[i][j].roomNum = roomNumber++;
            }
        }
        rooms[0][0].west.isGone = true;
        rooms[0][0].roomNum = 0;
        rooms[height - 1][width - 1].south.isGone = true;
        rooms[height - 1][width - 1].roomNum = (height * width);
    }

    @Override
    public void paintComponent(Graphics g) {
        x_cord = 40;
        y_cord = 40;
        roomSize = (width - x_cord) / width + 7;

        int x = x_cord;
        int y = y_cord;

        for (int i = 0; i <= height - 1; ++i) {
            for (int j = 0; j <= width - 1; ++j) {
                if (!(rooms[i][j].north.isGone)) {
                    g.drawLine(x, y, x + roomSize, y);                    
                }

                if (rooms[i][j].west.isGone == false) {
                    g.drawLine(x, y, x, y + roomSize);                    
                }

                if ((i == height - 1) && rooms[i][j].south.isGone == false) {
                    g.drawLine(x, y + roomSize, x + roomSize, y + roomSize);
                }
                
                if ((j == width - 1) && rooms[i][j].east.isGone == false) {                 
                    g.drawLine(x + roomSize, y, x + roomSize, y + roomSize);                   
                }
                x += roomSize;
            }
            x = x_cord;
            y += roomSize;
        }
    }

    public static void main(String[] args) {
        int m = 90, n = 100;

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        
        JFrame frame = new JFrame();
        
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 100);
        frame.setResizable(false);
        
        JLabel bottomPan = new JLabel("Maze by Viol & Gor");
        bottomPan.setFont(new Font("Serif", Font.BOLD, 23));
        frame.add(bottomPan, BorderLayout.SOUTH);
        bottomPan.setForeground(Color.RED);
        
        maze = new Maze(m, n);
        frame.getContentPane().add(maze, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(800, 800);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        frame.add(buttonPanel, BorderLayout.NORTH);
        
        JButton resetButton = new JButton("New Maze");
        resetButton.setPreferredSize(new Dimension(300, 50));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 40));
        buttonPanel.add(resetButton);       
        
        resetButton.addActionListener((final ActionEvent e) -> {
            frame.remove(maze);
            System.gc();
            
            maze = new Maze(m, n);
            frame.getContentPane().add(maze, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        });
        
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 40));
        buttonPanel.add(exitButton);    
        
        exitButton.addActionListener((final ActionEvent e) -> {
            System.exit(0);
        });
    }
}
