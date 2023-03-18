package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    static Socket clientSocket;

    static ServerSocket serverSocket = null;

    public static void main(String[] args) {
        int counter = 0;
        try {
            serverSocket = new ServerSocket(0002);
            serverSocket.setReuseAddress(true);
            System.out.println("Запустили сервер");
            while (true) {
                counter++;
                clientSocket = serverSocket.accept();
                ClientHandler clientSock = new ClientHandler(clientSocket, counter);
                new Thread(clientSock).start();
                System.out.println("К нам пришел клиент №" + counter);
            }
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }
}