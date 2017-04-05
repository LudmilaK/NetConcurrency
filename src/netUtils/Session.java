package netUtils;

import netUtils.Host;

import java.io.*;
import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Runnable {

    static Socket socket_;

    public Session(Socket socket) {
        this.socket_ = socket;
    }

    @Override
    public void run() {
        try {
            InputStream socketInputStream = socket_.getInputStream(); // байтовый поток
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
