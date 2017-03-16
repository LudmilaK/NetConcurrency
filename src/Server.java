import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Server {

    private final static Object lock_ = new Object();
    static int countMax_ = 2;
    public static int count_ = 0;

    public static void closeSession() {
        synchronized (lock_) {
            count_--;
            System.out.println("Количество подключенных клиентов: " + count_);
            lock_.notify();
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = 0;
        try {
            port = Integer.valueOf(args[0]);
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.out.println("Порт занят");
            }
            toWhile:
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("Клиент не принят!");
                }
                Session session = new Session(socket);
                OutputStream out = null; // байтовый поток
                try {
                    out = socket.getOutputStream();
                } catch (IOException e) {
                    System.out.println("Ошибка получения выходного потока!");
                }
                DataOutputStream dataOutStream = new DataOutputStream(out);

                synchronized (lock_) {
                    if (count_ == countMax_) {
                        lock_.wait();
                        continue toWhile;
                    }
                    count_++;
                    try {
                        dataOutStream.writeUTF("Доступ разрешен!");
                    } catch (IOException e) {
                        System.out.println("Связь с клиентом потеряна!");
                    }
                    System.out.println("Количество подключенных клиентов: " + count_);
                    Thread thread = new Thread(session);
                    thread.start();
                }

            }
        } catch (NumberFormatException nfe) {
            System.out.println("Неверный формат номера порта!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}