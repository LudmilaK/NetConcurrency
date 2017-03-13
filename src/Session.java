import java.io.*;
import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Runnable {

    Socket socket_;
    final static Object lock_ = new Object();
    int countMax_;
    public static int count_ = 0;

    public void closeSession() {
        synchronized (lock_) {
            count_--;
            System.out.println("Количество подключенных клиентов: " + count_);
            lock_.notifyAll();
        }
    }

    public Session() {
    }

    public Session(Socket socket, int countMax) {
        this.socket_ = socket;
        this.countMax_ = countMax;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = 0;
        try {
            port = Integer.valueOf(args[0]);
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Session session = new Session(socket, 5);
                synchronized (session.lock_) {
                    if (session.count_ == session.countMax_) {
                        session.lock_.wait();
                    }
                    OutputStream out = socket.getOutputStream(); // байтовый поток
                    DataOutputStream dataOutStream = new DataOutputStream(out);
                    dataOutStream.writeUTF("Доступ разрешен!");
                    Thread thread = new Thread(session);
                    thread.start();
                    session.count_++;
                    System.out.println("Количество подключенных клиентов: " + session.count_);
                }
            }
        } catch (BindException e) {
            System.out.println("Порт уже занят!");
        } catch (IOException e) {
            System.out.println("Невозможно принять клиента!");
        } catch (NumberFormatException nfe) {
            System.out.println("Неверный формат номера порта!");
        } catch (InterruptedException e) {
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
                    break;
                }

            }
        } catch (IOException e) {
            System.out.println("Один из клиентов отключен!");
        } finally {
            closeSession();
        }
    }
}
