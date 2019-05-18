package server;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String passWord;
    transient private static ArrayList<Account> accounts = new ArrayList<>();

    public Account(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

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
