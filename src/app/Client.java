package app;

import model.ChatHistoryMessage;
import netUtils.ServerAnswerCd;
import netUtils.ServerAnswerHelp;
import netUtils.ServerAnswerLs;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Client {
    static String enter = "> ";

    public static void main(String[] args) {
        int port = 0;
        String host = args[1];
        try {
            port = Integer.valueOf(args[0]);
            Socket socket = new Socket(host, port); // подключение к серверу по локальному хосту
            OutputStream socketOutputStream = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));
            // проверять if- м, если есть, что читать - читаем, если есть, что передать - передаем
            // или запустить поток, который будет с какой-то периодичностью запрашивать, жив ли сервер
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            DataInputStream dataInStream = new DataInputStream(in);
            String message1 = "";
            message1 = dataInStream.readUTF();
            if (message1.equals("Отказано в доступе!")) {
                System.out.println(message1);
                socket.close();
            } else {
                System.out.println("Чтобы узнать о доступных командах введите \"help\"");
                while (true) {
                    System.out.print(enter);
                    String message2 = bufferedReader.readLine();
                    dataOutputStream.writeUTF(message2);
                    dataOutputStream.flush();
                    message2 = dataInStream.readUTF();
                    if (message2.equals("exit")) {
                        socket.close();
                        break;
                    }
                    if (message1.equals("Socket is closed")) {
                        System.out.println(message1);
                        break;
                    }
                    if (message2.equals("helpAnswer")) {
                        FileInputStream fileInputStream = new FileInputStream("answer.out");
                        ObjectInputStream ois = new ObjectInputStream(fileInputStream);
                        ServerAnswerHelp mes = null;
                        try {
                            mes = (ServerAnswerHelp) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        for (String str : mes.getArrayList()) {
                            System.out.println(str);
                        }
                    } else if (message2.equals("cdAnswer")) {
                        FileInputStream fileInputStream = new FileInputStream("answer.out");
                        ObjectInputStream ois = new ObjectInputStream(fileInputStream);
                        ServerAnswerCd mes = null;
                        try {
                            mes = (ServerAnswerCd) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(enter.equals(mes.getAnswer()  + "> ")){
                            System.out.println(mes.getAnswer());
                        }
                        enter = mes.getAnswer() + "> ";
                    }
                    else if (message2.equals("rmAnswerSuccess")){
                        continue;
                    }
                    else if(message2.equals("rmAnswerErr")){
                        System.out.println("Папка не пуста!");
                    }
                    else if(message2.equals("lsAnswer")){
                        FileInputStream fileInputStream = new FileInputStream("answer.out");
                        ObjectInputStream ois = new ObjectInputStream(fileInputStream);
                        ServerAnswerLs mes = null;
                        try {
                            mes = (ServerAnswerLs) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        for (String str : mes.getArrayList()) {
                            System.out.println(str);
                        }
                    }
                    else {
                        System.out.println(message2);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Сервер не доступен!");
        } catch (NumberFormatException nfe) {
            System.out.println("Неверный формат номера порта!");
        }
    }

}
