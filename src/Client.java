import java.io.*;
import java.net.Socket;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Client {
    public static void main(String[] args) {
        int port = 0;
        String host = args[1];
        try {
            port = Integer.valueOf(args[0]);
            Socket socket = new Socket(host, port); // подключение к серверу по локальному хосту
            OutputStream socketOutputStream = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            DataInputStream dataInStream = new DataInputStream(in);
            String message = "";
            message = dataInStream.readUTF();
            if (message.equals("Отказано в доступе!")) {
                System.out.println(message);
                socket.close();
            } else {
                while (true) {
                    System.out.print("Отправить: ");
                    message = bufferedReader.readLine();
                    dataOutputStream.writeUTF(message);
                    System.out.println("Отправлено!");
                    dataOutputStream.flush();
                    if (message.equals("bye")) {
                        socket.close();
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Сервер не доступен!");
        }
        catch (NumberFormatException nfe){
            System.out.println("Неверный формат номера порта!");
        }
    }

}
