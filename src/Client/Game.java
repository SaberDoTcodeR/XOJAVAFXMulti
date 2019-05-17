package Client;

import javafx.scene.Group;

public class Game {
    private int[][] map;
    private static final int FREE = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    public Game(int size,String oppName){
        map=new int[size][size];

    }
}
