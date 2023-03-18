package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    int counter;
    BufferedWriter out = null;
    BufferedReader in = null;
    static String whatYouWant = "Что хотите сделать? 1)Отправить файл на сервер -> (put + <filename>) 2)Получить файл с сервера -> (get + <filename||ID>) 3)Удалить файл с сервера -> (del + <filename||ID>)";
    public ClientHandler(Socket socket, int counter)
    {
        this.clientSocket = socket;
        this.counter = counter;
    }
    Random rnd = new Random();

    public void run(){
        try {
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            int counter = 0;
            int schet = 0;
            int noCiklName = 0;
            int noCiklId = 0;
            String word = "";
            String nameOfFile = "";
            String idOfFile = "";
            String[] words = new String[3];
            String[] ID = new String[3];
            String[][] libraryServer = new String[100][2];
            String pathToServer = "C:\\Users\\senya\\IdeaProjects\\se-lab04-tmp2223\\src\\main\\java\\client\\data";
            String pathToClient = "C:\\Users\\senya\\IdeaProjects\\se-lab04-tmp2223\\src\\main\\java\\client\\data\\folder";


            out.write(whatYouWant + "\n");
            out.flush();
            word = in.readLine();
            System.out.println(word);
            word = word.trim();
            while (!word.equals("exit")){
                System.out.println("Прошли проверку exit");
                if (word.length() >= 5){
                    System.out.println("Прошли проверку на длину");
                    if (word.startsWith("put")){
                        System.out.println("Получили put");
                        words = word.split(" ");
                        nameOfFile = words[1];
                        File ForDel = new File(pathToClient + "\\" + nameOfFile);
                        try {
                            FileInputStream fis = new FileInputStream(pathToClient + "\\" + nameOfFile);
                            BufferedInputStream bis= new BufferedInputStream(fis);
                            int size = bis.available();
                            byte buffer[]= new byte[size];
                            bis.read(buffer);
                            System.out.println("Чтение прошло успешно");
                            int ident = rnd.nextInt(1000,9999);
                            if (libraryServer[0][0] != null){
                                for (int i = 0; i < schet ; i++){
                                    if(ID[i].equals(String.valueOf(ident))){
                                        ident = rnd.nextInt(1000,9999);
                                        i = 0;
                                    }
                                }
                            }
                            libraryServer[schet][0] = nameOfFile;
                            libraryServer[schet][1] = String.valueOf(ident);
                            schet++;
                            ID[counter] = String.valueOf(ident);
                            nameOfFile = ID[counter] + " " + nameOfFile;
                            FileOutputStream fos = new FileOutputStream(pathToServer + "\\" + nameOfFile);
                            fos.write(buffer);
                            System.out.println("запись успешна");
                            fis.close();
                            bis.close();
                            fos.close();
                            boolean isdeleted = ForDel.delete();
                            System.out.println("удаление файла - " + isdeleted);
                            out.write(200 + " " + ID[counter] + "\n");
                            out.flush();
                            out.write(whatYouWant + "\n");
                            out.flush();
                            word = in.readLine();
                        } catch (IOException e) {
                            out.write(403 + "\n");
                            out.flush();
                        }
                    }
                    else if (word.startsWith("get")){
                        System.out.println("Получили get");
                        words = word.split(" ");
                        nameOfFile = words[1];
                            for (int i = 0; i < schet; i++){
                                try {
                                    if (libraryServer[i][0].equals(nameOfFile)){
                                        System.out.println("Нашли по имени");
                                        noCiklName++;
                                        nameOfFile = libraryServer[i][1] + " " + libraryServer[i][0];
                                        libraryServer[i][1] = "";
                                        libraryServer[i][0] = "";
                                        break;
                                    }
                                    else if(libraryServer[i][1].equals(nameOfFile)){
                                        System.out.println("Нашли по Id");
                                        nameOfFile = libraryServer[i][1] + " " + libraryServer[i][0];
                                        libraryServer[i][1] = "";
                                        libraryServer[i][0] = "";
                                        noCiklId++;
                                        break;
                                    }
                                    else if((schet - i == 1)&(noCiklName == 0)&(noCiklId == 0)){
                                        System.out.println("Файл не найден");
                                        out.write(404 + "\n");
                                        out.flush();
                                        out.write(whatYouWant + "\n");
                                        out.flush();
                                    }
                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        try{
                            File ForDel = new File(pathToServer + "\\" + nameOfFile);
                            FileInputStream fis = new FileInputStream(pathToServer + "\\" + nameOfFile);
                            BufferedInputStream bis= new BufferedInputStream(fis);
                            int size = bis.available();
                            byte buffer[]= new byte[size];
                            bis.read(buffer);
                            System.out.println("Чтение прошло успешно");
                            words = nameOfFile.split(" ");
                            nameOfFile = words[1];
                            FileOutputStream fos = new FileOutputStream(pathToClient + "\\" + nameOfFile);
                            fos.write(buffer);
                            System.out.println("запись успешна");
                            fis.close();
                            bis.close();
                            fos.close();
                            boolean isdeleted = ForDel.delete();
                            System.out.println("удаление файла - " + isdeleted);
                            out.write(200 + " " + nameOfFile + "\n");
                            out.flush();
                            out.write(whatYouWant + "\n");
                            out.flush();
                            word = in.readLine();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                    }
                    else if (word.startsWith("del")){
                        System.out.println("Получили del");
                        words = word.split(" ");
                        nameOfFile = words[1];
                        for (int i = 0; i < schet; i++){
                            try {
                                if (libraryServer[i][0].equals(nameOfFile)){
                                    System.out.println("Нашли по имени");
                                    noCiklName++;
                                    nameOfFile = libraryServer[i][1] + " " + libraryServer[i][0];
                                    break;
                                }
                                else if(libraryServer[i][1].equals(nameOfFile)){
                                    System.out.println("Нашли по Id");
                                    nameOfFile = libraryServer[i][1] + " " + libraryServer[i][0];
                                    noCiklId++;
                                    break;
                                }
                                else if((schet - i == 1)&(noCiklName == 0)&(noCiklId == 0)){
                                    System.out.println("Файл не найден");
                                    out.write(404 + "\n");
                                    out.flush();
                                    out.write(whatYouWant + "\n");
                                    out.flush();
                                }
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        File ForDel = new File(pathToServer + "\\" + nameOfFile);
                        boolean isdeleted = ForDel.delete();
                        System.out.println("удаление файла - " + isdeleted);
                        if (isdeleted){
                            out.write("200 " + nameOfFile + "\n");
                            out.flush();
                            out.write(whatYouWant + "\n");
                            out.flush();
                            word = in.readLine();
                        }
                        else{
                            System.out.println("Файл не найден");
                            out.write(405 + "\n");
                            out.flush();
                            out.write(whatYouWant + "\n");
                            out.flush();
                            word = in.readLine();
                        }
                    }
                    else {
                        System.out.println("Пользователь питонист, русский не понимает. DEL ЛИБО GET ЛИБО PUT СКАЗАНО ТЕБЕ ВАФЁЛ!");
                        out.write("Але другалек, я же тебе гайд написал как писать команду, мозги не мни" + "\n");
                        out.flush();
                    }
                }
                else{
                    System.out.println("Короткое слово");
                    out.write("Суета cetnf" + "\n");
                    out.flush();
                    out.write(whatYouWant + "\n");
                    out.flush();
                    word = in.readLine();
                }
            }
            clientSocket.close();
            System.exit(0);
        }catch (IOException e){
        }


    }

}