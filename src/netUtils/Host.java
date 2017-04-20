package netUtils;

import concurrentUtils.Channel;
import concurrentUtils.Stoppable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 29.03.2017.
 */
public class Host implements Stoppable {
    private static ServerSocket serverSocket;
    volatile boolean isActive;
    private Channel channel;
    private Thread thread;
    private MessageHandlerFactory messageHandlerFactory;

    public Host(int port, Channel channel, MessageHandlerFactory messageHandlerFactory) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (NumberFormatException nfe) {
            System.out.println("Неверный формат номера порта!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
        this.messageHandlerFactory = messageHandlerFactory;
        isActive = true;
    }

    @Override
    public void run() {
        while (isActive) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Клиент не принят!");
            }
            MessageHandler messageHandler = messageHandlerFactory.create();
            Session session = new Session(socket, messageHandler);
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

    @Override
    public void stop() {
        if(isActive){
            isActive = false;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // закрываем серверсокет и пишем сообщение, что сервер остановился
    }
}