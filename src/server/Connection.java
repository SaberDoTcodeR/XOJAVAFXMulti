package server;

import com.google.gson.Gson;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable {
    private boolean running;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Account loggedInAccount;
    private transient Game game;

    public Connection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                String string = ((String) inputStream.readObject());
                Gson gson = new Gson();

                outputStream.reset();
                if (string.contains("loggingIn")) {
                    AccountPacket accountPacket = gson.fromJson(string, AccountPacket.class);

                    Account account = accountPacket.getAccount();
                    if (accountPacket.isLoggingIn()) {
                        boolean x = Account.validateLogInAccount(account);
                        if (x) {
                            accountPacket.setSuccess(true);
                            loggedInAccount = account;
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        } else {
                            accountPacket.setSuccess(false);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        }
                    } else {

                        boolean x = Account.validateSignUpAccount(account);
                        if (x) {
                            Account.addAccount(account);
                            loggedInAccount = account;
                            accountPacket.setSuccess(true);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        } else {
                            accountPacket.setSuccess(false);
                            outputStream.writeObject(gson.toJson(accountPacket));
                            outputStream.flush();
                        }

                    }
                } else if (string.contains("game")) {
                    GamePacket gamePacket = gson.fromJson(string, GamePacket.class);
                    boolean x = gamePacket.isCreatingGame();
                    if (x) {
                        boolean y = Account.validateOpponent(gamePacket.getGame(), this);
                        if (y) {
                            GamePacket gamePacket2 = new GamePacket(game, true);
                            gamePacket2.setSuccess(true);
                            outputStream.writeObject(gson.toJson(gamePacket2));
                        } else {
                            GamePacket gamePacket2 = new GamePacket(gamePacket.getGame(), true);
                            gamePacket2.setSuccess(false);
                            outputStream.writeObject(gson.toJson(gamePacket2));
                        }
                    }
                }

            } catch (EOFException | SocketException e) {
                running = false;
                Main.getConnections().remove(this);
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
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (EOFException | SocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
