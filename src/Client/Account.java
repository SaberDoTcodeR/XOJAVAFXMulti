package Client;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String passWord;
    private static Account currentAccount;
    private transient Game game;
    public Account(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public static void setCurrentAccount(Account currentAccount) {
        Account.currentAccount = currentAccount;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
