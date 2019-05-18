package Client;


public class Game {
    private int[][] map;
    private static final int FREE = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    private Account firstPlayerAccount;
    private Account secondPlayerAccount;

    public Game(int size, Account firstPlayerAccount, Account secondPlayerAccount) {
        map = new int[size][size];
        this.firstPlayerAccount = firstPlayerAccount;
        this.secondPlayerAccount = secondPlayerAccount;
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
