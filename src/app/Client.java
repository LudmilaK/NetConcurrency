package app;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Client {
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
            String message = "";
            message = dataInStream.readUTF();
            if (message.equals("Отказано в доступе!")) {
                System.out.println(message);
                socket.close();
            } else {
                while (true) {
                    System.out.print("Отправить: ");
                    message = bufferedReader.readLine();
                    dataOutputStream.writeUTF(message);
                    System.out.println("Отправлено!");
                    dataOutputStream.flush();
                    if (message.equals("bye")) {
                        socket.close();
                        break;
                    }
                    if(message.equals("Socket is closed")){
                        System.out.println(message);
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Сервер не доступен!");
        }
        catch (NumberFormatException nfe){
            System.out.println("Неверный формат номера порта!");
        }
    }

}
