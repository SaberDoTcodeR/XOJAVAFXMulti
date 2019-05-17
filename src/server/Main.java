package server;


import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {


    private static ArrayList<Connection> connections = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);
        while (true) {

            Socket socket = serverSocket.accept();
            Connection connection=new Connection(socket);
            System.out.println("connection created..");
            connections.add(connection);
        }



    }
}
