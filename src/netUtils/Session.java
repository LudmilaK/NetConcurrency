package netUtils;

import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Runnable {

    static Socket socket_;
    MessageHandler messageHandler;

    public Session(Socket socket, MessageHandler messageHandler) {

        this.socket_ = socket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        messageHandler.handle(socket_);
    }
}
