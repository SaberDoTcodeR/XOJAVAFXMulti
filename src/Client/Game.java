package Client;


import com.google.gson.Gson;

public class Game {
    private int[][] map;
    private static final int FREE = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    private Account firstPlayerAccount;
    private Account secondPlayerAccount;
    private int size;
    private int whoseTurn = PLAYER_ONE;
    private int finished = 0;

    public Game(int size, Account firstPlayerAccount, Account secondPlayerAccount) {
        map = new int[size][size];
        this.size = size;
        this.firstPlayerAccount = firstPlayerAccount;
        this.secondPlayerAccount = secondPlayerAccount;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getSize() {
        return size;
    }

    public boolean isMyTurn() {
        if (this.getWhoseTurn() == 1) {
            if (this.getFirstPlayerAccount().getUserName().equals(Account.getCurrentAccount().getUserName()))
                return true;
            else
                return false;
        } else {
            if (this.getFirstPlayerAccount().getUserName().equals(Account.getCurrentAccount().getUserName()))
                return false;
            else
                return true;
        }
    }

    public void doMouseClick(double x, double y) {
        if (!this.isMyTurn())
            return;

        if (this.finished != 0) {
         /*
            if (state.gameEndedInTie || myID == state.winner)
                connection.send("newgame");  // Start a new game.
            return;*/
        }
        int row = (int) (y / 62);
        int col = (int) (x / 62);

        if (row >= 0 && row < this.size && col >= 0 && col < this.size && this.getMap()[row][col] == 0) {
            Gson gson = new Gson();
            ClientGame.connection.sendPacket(gson.toJson(new int[]{row, col}));
        }
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public void setSize(int size) {
        this.size = size;
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
