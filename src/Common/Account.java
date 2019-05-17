package Common;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String passWord;
    private static ArrayList<Account> accounts = new ArrayList<>();

    public Account(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

    }

    public static boolean validateLogInAccount(Account account) {
        for (Account account1 : accounts) {
            if (account.userName.equals(account1.userName) && account.passWord.equals(account1.passWord))
                return true;
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}
