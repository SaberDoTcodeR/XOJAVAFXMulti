package Client;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String passWord;

    public Account(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;

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
