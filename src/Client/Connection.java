package Client;

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
                String string = (String) inputStream.readObject();
                Gson gson = new Gson();
                if (string.contains("account")) {
                    AccountPacket accountPacket = gson.fromJson(string, AccountPacket.class);
                    Account account = accountPacket.getAccount();

                    boolean x = accountPacket.isSuccess();
                    if (x) {
                        System.out.println("in main menu");//todo get to mai menu
                        //ClientGame.makeMainMenu(Account);
                    } else if (accountPacket.isLoggingIn()) {
                        ClientGame.wrongLogIn();
                    } else {
                        ClientGame.wrongSingUp();
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(String string) {
        try {
            outputStream.reset();
            outputStream.writeObject(string);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
