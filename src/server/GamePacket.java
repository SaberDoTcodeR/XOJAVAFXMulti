package server;

public class GamePacket {
    private Game game;
    private boolean creatingGame;
    private boolean isSuccess;
    public GamePacket(Game game, boolean creatingGame) {
        this.game = game;
        this.creatingGame = creatingGame;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isCreatingGame() {
        return creatingGame;
    }

    public void setCreatingGame(boolean creatingGame) {
        this.creatingGame = creatingGame;
    }
}
