import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Server {

    public static void main(String[] args) {
        Channel channel = new Channel(3);
        int port = 0;
        port = Integer.valueOf(args[0]);
        Host host = new Host(port, channel);
        host.start();
        ThreadPool threadPool = new ThreadPool(3);
        Dispatcher dispatcher = new Dispatcher(channel, threadPool);
        dispatcher.start();
    }
}