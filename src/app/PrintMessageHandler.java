package app;

import netUtils.MessageHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Людмила on 31.03.2017.
 */
public class PrintMessageHandler implements MessageHandler {

    @Override
    public void handle(Socket socket) {
        try {
            InputStream socketInputStream = socket.getInputStream(); // байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            String message = "";
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println("Получено: " + message);
                if (message.equals("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Один из клиентов отключен!");
        }
    }
}
