package netUtils;

import concurrentUtils.Stoppable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Stoppable {

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

    @Override
    public void stop() {
        if(socket_ != null){
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(socket_.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataOutputStream.writeUTF("Socket is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
