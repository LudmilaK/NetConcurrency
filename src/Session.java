import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Runnable {

    Socket socket_;
    int countMax_;
    public static volatile int count_ = 0;

    public Session() {

    }

    public Session(Socket socket, int countMax) {
        this.socket_ = socket;
        this.countMax_ = countMax;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = Integer.valueOf(args[0]);
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                OutputStream out = socket.getOutputStream(); // байтовый поток
                DataOutputStream dataOutStream = new DataOutputStream(out);
                Session session = new Session(socket, 5);
                if (Session.count_ < session.countMax_) {
                    dataOutStream.writeUTF("Доступ разрешен!");
                    Thread thread = new Thread(session);
                    thread.start();
                    Session.count_++;
                    System.out.println("Количество подключенных клиентов: " + Session.count_);
                }
                else{
                    dataOutStream.writeUTF("Отказано в доступе!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    Session.count_--;
                    System.out.println("Количество подключенных клиентов: " + Session.count_);
                    socket_.close();
                    break;
                }

            }
        } catch (IOException e) {
            count_--;
            System.out.println("Количество подключенных клиентов: " + Session.count_);
        }
    }
}
