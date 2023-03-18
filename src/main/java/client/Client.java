package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private static BufferedReader reader;
    static String word = "";
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost", 0002);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))){
            reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println(in.readLine());
                word = reader.readLine();
                if (word.equals("exit")){
                    out.write(word + "\n");
                    out.flush();
                    clientSocket.close();
                    break;
                }
                else{
                    out.write(word + "\n");
                    out.flush();
                    System.out.println(in.readLine());
                    System.out.println(in.readLine());
                    word = reader.readLine();
//                    out.write(word + "\n");
//                    out.flush();
                }
            }


        }catch (IOException e){
        }
    }
}