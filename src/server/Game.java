package server;


public class Game {

    private int[][] map;
    private transient Connection firstPlayerConnection;
    private transient Connection secondPlayerConnection;
    private static final int FREE = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    private Account firstPlayerAccount;
    private Account secondPlayerAccount;

    public Game(int size, Connection firstPlayerConnection, Connection secondPlayerConnection) {
        map = new int[size][size];
        this.firstPlayerConnection = firstPlayerConnection;
        this.secondPlayerConnection = secondPlayerConnection;
    }

    public Connection getFirstPlayerConnection() {
        return firstPlayerConnection;
    }

    public Connection getSecondPlayerConnection() {
        return secondPlayerConnection;
    }

    public void setFirstPlayerConnection(Connection firstPlayerConnection) {
        this.firstPlayerConnection = firstPlayerConnection;
    }

    public void setSecondPlayerConnection(Connection secondPlayerConnection) {
        this.secondPlayerConnection = secondPlayerConnection;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Account getFirstPlayerAccount() {
        return firstPlayerAccount;
    }

    public void setFirstPlayerAccount(Account firstPlayerAccount) {
        this.firstPlayerAccount = firstPlayerAccount;
    }

    public Account getSecondPlayerAccount() {
        return secondPlayerAccount;
    }

    public void setSecondPlayerAccount(Account secondPlayerAccount) {
        this.secondPlayerAccount = secondPlayerAccount;
    }
}
