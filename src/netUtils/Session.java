package netUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Runnable {

    Socket socket_;
    MessageHandler messageHandler;

    public Session(Socket socket, MessageHandler messageHandler) {

        this.socket_ = socket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            InputStream socketInputStream = socket_.getInputStream(); // байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            String message = "";
            while (true) {
                message = dataInputStream.readUTF();
                messageHandler.handle(message);
                if (message.equals("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Один из клиентов отключен!");
        }

    }
}
