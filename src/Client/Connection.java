package Client;

import com.google.gson.Gson;
import server.Main;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable {
    private boolean running;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private volatile boolean closedManual;

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
        while (running && !closedManual) {
            try {
                String string = (String) inputStream.readObject();
                Gson gson = new Gson();
                if (string.contains("loggingIn")) {
                    AccountPacket accountPacket = gson.fromJson(string, AccountPacket.class);
                    Account account = accountPacket.getAccount();

                    boolean x = accountPacket.isSuccess();
                    if (x) {
                        Account.setCurrentAccount(account);
                        ClientGame.makeMainMenu(account);
                    } else if (accountPacket.isLoggingIn()) {
                        ClientGame.wrongLogIn();
                    } else {
                        ClientGame.wrongSingUp();
                    }


                } else if (string.contains("game")) {
                    GamePacket gamePacket = gson.fromJson(string, GamePacket.class);
                    boolean x = gamePacket.isCreatingGame();
                    if (x) {
                        if (gamePacket.isSuccess()) {
                            Account.getCurrentAccount().setGame(gamePacket.getGame());
                            ClientGame.makeGame(gamePacket.getGame());
                        } else {
                            ClientGame.wrongOpponent();
                        }
                    } else {
                        Account.getCurrentAccount().setGame(gamePacket.getGame());
                        ClientGame.makeGame(gamePacket.getGame());
                        if (gamePacket.getGame().getFinished() != 0)
                            ClientGame.whoWinError(gamePacket.getGame());
                    }
                } else if (string.contains("MSG")) {
                    ClientGame.printOppMessage(string.substring(3));
                } else if (string.equals("quit battle")) {
                    Account.getCurrentAccount().setGame(null);
                    ClientGame.makeMainMenu(Account.getCurrentAccount());
                }
            } catch (EOFException | SocketException e) {
                while (!closedManual) {
                    try {
                        ClientGame.socket = new Socket("localhost", 5555);
                        ClientGame.connection = new Connection(ClientGame.socket);
                        System.out.println("connection client ..");
                        ClientGame.loginSceneManage(true);
                        break;
                    } catch (ConnectException e1) {
                        // ClientGame.connectionError();
                    } catch (IOException e1) {
                        e.printStackTrace();
                    }
                }
                break;
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
        } catch (EOFException | SocketException e) {
            ClientGame.connectionError();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {

        try {
            inputStream.close();
            outputStream.close();
            closedManual = true;
        } catch (EOFException | SocketException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
