package netUtils;

import concurrentUtils.Channel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 29.03.2017.
 */
public class Host implements Runnable {
    private static ServerSocket serverSocket;
    private Channel channel;
    private Thread thread;

    public Host(int port, Channel channel) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (NumberFormatException nfe) {
            System.out.println("Неверный формат номера порта!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
    }

    @Override
    public void run() {
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
            channel.put(session);
            try {
                dataOutStream.writeUTF("Доступ разрешен!");
            } catch (IOException e) {
                System.out.println("Связь с клиентом потеряна!");
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }
}