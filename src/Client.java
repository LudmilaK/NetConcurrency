import java.io.*;
import java.net.Socket;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Client {
    public static void main(String[] args) {
        String host = args[1];
        int port = Integer.valueOf(args[0]);
        try {
            Socket socket = new Socket(host, port); // подключение к серверу по локальному хосту
            OutputStream socketOutputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
            String message = "";
            while (true) {
                System.out.print("Отправить: ");
                message = bufferedReader.readLine();
                dataOutputStream.writeUTF(message);
                System.out.println("Отправлено!");
                dataOutputStream.flush();
                if(message.equals("bye")){
                    socket.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
