package netUtils;

import java.net.Socket;

/**
 * Created by Людмила on 31.03.2017.
 */
public interface MessageHandler { // продукт
    void handle(Socket socket);
}
