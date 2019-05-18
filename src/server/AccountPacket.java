package server;

import java.io.Serializable;

public class AccountPacket implements Serializable {
    private boolean loggingIn;
    private boolean success;
    private Account account;

    public AccountPacket(Account account, boolean loggingIn) {
        this.account = account;
        this.loggingIn = loggingIn;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isLoggingIn() {
        return loggingIn;
    }

    public void setLoggingIn(boolean loggingIn) {
        this.loggingIn = loggingIn;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isSuccess() {
        return success;
    }

    public Account getAccount() {
        return account;
    }
}
