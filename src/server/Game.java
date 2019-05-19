package server;


public class Game {

    private int[][] map;
    private transient Connection firstPlayerConnection;
    private transient Connection secondPlayerConnection;
    private static final int FREE = 0, PLAYER_ONE = 1, PLAYER_TWO = 2;
    private Account firstPlayerAccount;
    private Account secondPlayerAccount;
    private int size;
    private int whoseTurn;
    private int finished = 0;//0 no  1  2  3 draw

    public Game(int size, Connection firstPlayerConnection, Connection secondPlayerConnection) {
        map = new int[size][size];
        this.size = size;
        this.firstPlayerConnection = firstPlayerConnection;
        this.secondPlayerConnection = secondPlayerConnection;
    }


    public void updateMap(int[] data) {
        int row = data[0];
        int col = data[1];
        this.map[row][col] = this.whoseTurn;
        if (this.checkFinish(this.whoseTurn)) {
            this.finished = this.whoseTurn;
        } else if (this.checkDraw()) {
            this.finished = 3;
        }
        if (this.whoseTurn == 1)
            this.whoseTurn++;
        else this.whoseTurn--;
    }

    public boolean checkDraw() {
        for (int row = 0; row < this.size; row++)
            for (int col = 0; col < this.size; col++)
                if (this.map[row][col] == 0)
                    return false;


        return false;
    }

    public boolean checkFinish(int x) {
        int N;
        if (this.size > 3)
            N = 4;
        else
            N = 3;
        int counter;
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size - (N - 1); col++) {
                counter = 0;
                while (this.map[row][col] == x) {
                    col++;
                    counter++;
                    if (counter == N)
                        return true;
                }
            }
        }
        for (int col = 0; col < this.size; col++) {
            for (int row = 0; row < this.size - (N - 1); row++) {
                counter = 0;
                while (this.map[row][col] == x) {
                    row += 1;
                    counter++;
                    if (counter == N)
                        return true;
                }
            }
        }
        for (int row = 0; row < this.size - (N - 1); row++) {
            for (int col = 0; col < this.size - (N - 1); col++) {
                counter = 0;
                while (this.map[row][col] == x) {
                    col++;
                    row++;
                    counter++;
                    if (counter == N)
                        return true;
                }
            }
        }
        for (int col = this.size - 1; col > (N - 2); col--) {
            for (int row = 0; row < this.size - (N - 1); row++) {
                counter = 0;
                int colSave = col;
                int rowSave = row;
                while (this.map[row][col] == x) {
                    row++;
                    col--;
                    counter++;
                    if (counter == N)
                        return true;
                }
                row = rowSave;
                col = colSave;
            }
        }

        return false;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
