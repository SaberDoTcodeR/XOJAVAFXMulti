package Common;

import java.io.Serializable;

public class AccountPacket implements Serializable {
    private static final long serialVersionUID = 5950169519310163574L;
    private boolean loggingIn;
    private boolean success;
    private transient Account account;


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

    public boolean isSuccess() {
        return success;
    }

    public Account getAccount() {
        return account;
    }
}
