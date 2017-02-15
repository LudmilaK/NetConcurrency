import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = Integer.valueOf(args[0]);
        try {
            serverSocket = new ServerSocket(port); //поднимаем сервер (открываем порт)
            Socket socket = serverSocket.accept(); //accept - возвращает экземпляр клиента, который подключился к серверу
            InputStream socketInputStream = socket.getInputStream(); // байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            String message = "";
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println("Получено: " + message);
                if (message.equals("bye")){
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}