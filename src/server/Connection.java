package server;

import Common.Account;
import Common.AccountPacket;

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
                Object object = inputStream.readObject();
                System.out.println("recieved");
                if (object.getClass().getSimpleName().equals("AccountPacket")) {
                    if (((AccountPacket) object).isLoggingIn()) {
                        Account account = ((AccountPacket) object).getAccount();
                        boolean x = Account.validateLogInAccount(account);
                        if (x) {
                            outputStream.writeObject(object);
                            outputStream.flush();
                        } else {
                            ((AccountPacket) object).setSuccess(false);
                            outputStream.writeObject(object);
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
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
