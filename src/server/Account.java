package server;


import com.google.gson.Gson;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String passWord;
    transient private boolean isPlaying = false;
    transient private static ArrayList<Account> accounts = new ArrayList<>();

    public Account(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public static boolean validateOpponent(Game game, Connection firstPlConnection) {
        for (Account account1 : accounts) {
            if (game.getSecondPlayerAccount().getUserName().equals(account1.userName) && !account1.isPlaying) {
                for (Connection connection : Main.getConnections()) {
                    if (connection.getLoggedInAccount() != null && connection.getLoggedInAccount().userName.equals(
                            game.getSecondPlayerAccount().getUserName())) {
                        connection.getLoggedInAccount().setPlaying(true);
                        account1.setPlaying(true);
                        game.setFirstPlayerConnection(firstPlConnection);
                        game.setSecondPlayerConnection(connection);
                        connection.setGame(game);
                        firstPlConnection.setGame(game);
                        GamePacket gamePacket = new GamePacket(game, true);
                        gamePacket.setSuccess(true);
                        Gson gson = new Gson();
                        connection.sendPacket(gson.toJson(gamePacket));
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public static boolean validateSignUpAccount(Account account) {
        for (Account account1 : accounts) {
            if (account.userName.equals(account1.userName))
                return false;
        }
        return true;
    }

    public static boolean validateLogInAccount(Account account) {

        for (Account account1 : accounts) {
            if (account.userName.equals(account1.userName) && account.passWord.equals(account1.passWord))
                return true;
        }
        return false;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public static void addAccount(Account account) {
        accounts.add(account);
    }
}
