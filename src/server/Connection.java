package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Runnable {
    private boolean running;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Connection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                String string = ((String) inputStream.readObject());
                Gson gson = new Gson();

                outputStream.reset();
                if (string.contains("account")) {
                    AccountPacket accountPacket = gson.fromJson(string, AccountPacket.class);
                    if (accountPacket.isLoggingIn()) {
                        Account account = accountPacket.getAccount();
                        boolean x = Account.validateLogInAccount(account);
                        if (x) {
                            accountPacket.setSuccess(true);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        } else {
                            accountPacket.setSuccess(false);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        }
                    } else {

                        Account account = accountPacket.getAccount();
                        boolean x = Account.validateSignUpAccount(account);
                        if (x) {
                            Account.addAccount(account);
                            accountPacket.setSuccess(true);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        } else {
                            accountPacket.setSuccess(false);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(Object object) {
        try {

            outputStream.reset();
            outputStream.reset();
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
