package netUtils;

import concurrentUtils.Stoppable;
import model.ChatHistoryMessage;

import java.io.*;
import java.net.*;


/**
 * Created by Людмила on 17.02.2017.
 */
public class Session implements Stoppable {

    String enter = "";
    Socket socket_;
    MessageHandler messageHandler;

    public Session(Socket socket, MessageHandler messageHandler) {

        this.socket_ = socket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            InputStream socketInputStream = socket_.getInputStream(); // байтовый поток
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            String message = "";
            while (true) {
                message = dataInputStream.readUTF();
                messageHandler.handle(message);
                DataOutputStream dataOutputStream = null;
                dataOutputStream = new DataOutputStream(socket_.getOutputStream());
                if(message.split(" ").length == 0){
                    dataOutputStream.writeUTF("Команда \"" + message + "\" не поддерживается программой!");
                    dataOutputStream.flush();
                }
                else if (message.equals("exit")) {
                    dataOutputStream.writeUTF("exit");
                    dataOutputStream.flush();
                    System.out.println("Один из клиентов отключен!");
                    break;
                }
                else if (message.equals("help")) {
                    FileOutputStream out = new FileOutputStream("answer.out");
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    ServerAnswerHelp serverAnswerHelp = new ServerAnswerHelp();
                    oos.writeObject(serverAnswerHelp);
                    oos.flush();
                    oos.close();
                    dataOutputStream.writeUTF("helpAnswer");
                    dataOutputStream.flush();
                } else if (message.equals("cd")) {
                    FileOutputStream out = new FileOutputStream("answer.out");
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    ServerAnswerCd serverAnswerCd = new ServerAnswerCd(enter);
                    oos.writeObject(serverAnswerCd);
                    oos.flush();
                    oos.close();
                    dataOutputStream.writeUTF("cdAnswer");
                    dataOutputStream.flush();
                } else if (message.equals("rm")) {
                    File file = new File(enter);
                    if (!file.exists()) {
                        dataOutputStream.writeUTF("Неверно указан путь");
                        dataOutputStream.flush();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                            dataOutputStream.writeUTF("rmAnswerSuccess");
                        } else {
                            dataOutputStream.writeUTF("rmAnswerErr");
                        }
                        dataOutputStream.flush();
                    }
                } else if (message.equals("ls")) {
                    File file = new File(enter);
                    if (!file.exists()) {
                        dataOutputStream.writeUTF("Неверно указан путь");
                    } else {
                        if (file.listFiles().length == 0) {
                            dataOutputStream.writeUTF("Папка пуста");
                        } else {
                            String[] files = file.list();
                            FileOutputStream out = new FileOutputStream("answer.out");
                            ObjectOutputStream oos = new ObjectOutputStream(out);
                            ServerAnswerLs serverAnswerLs = new ServerAnswerLs();
                            for (String s : files) {
                                serverAnswerLs.getArrayList().add(s);
                            }
                            oos.writeObject(serverAnswerLs);
                            oos.flush();
                            oos.close();
                            dataOutputStream.writeUTF("lsAnswer");
                        }
                    }
                    dataOutputStream.flush();
                } else {
                    String[] array = message.split(" ");
                    if (array.length < 2) {
                        dataOutputStream.writeUTF("Команда \"" + message + "\" не поддерживается программой!");
                        dataOutputStream.flush();
                    } else {
                        if (!array[1].endsWith("\\") && !array[1].endsWith(":")) {
                            array[1] = array[1] + "\\";
                        }
                        File file1 = new File(enter + array[1]);
                        File file2 = new File(array[1]);
                        if (!file1.exists() && !file2.exists() || array[1].endsWith(":")) {
                            dataOutputStream.writeUTF("Путь к файлу не найден");
                            dataOutputStream.flush();
                        } else {
                            String enter1 = enter;
                            File file = null;
                            if (file1.exists()) {
                                file = file1;
                                enter = enter + array[1];
                            } else {
                                file = file2;
                                enter = array[1];
                            }
                            if (array[0].equals("cd")) {
                                FileOutputStream out = new FileOutputStream("answer.out");
                                ObjectOutputStream oos = new ObjectOutputStream(out);
                                if (file.canExecute() && !file.isDirectory()) {
                                    Runtime.getRuntime().exec(file.getPath());
                                    ServerAnswerCd serverAnswerCd = new ServerAnswerCd(enter1);
                                    oos.writeObject(serverAnswerCd);
                                    enter = enter1;
                                } else {
                                    ServerAnswerCd serverAnswerCd = new ServerAnswerCd(enter);
                                    oos.writeObject(serverAnswerCd);
                                }
                                oos.flush();
                                oos.close();
                                dataOutputStream.writeUTF("cdAnswer");
                                dataOutputStream.flush();
                            } else if (array[0].equals("rm")) {
                                if (file.listFiles().length == 0) {
                                    file.delete();
                                    enter = enter1;
                                    dataOutputStream.writeUTF("rmAnswerSuccess");
                                    dataOutputStream.flush();
                                } else {
                                    enter = enter1;
                                    dataOutputStream.writeUTF("rmAnswerErr");
                                    dataOutputStream.flush();
                                }
                            } else if (array[0].equals("ls")) {
                                String[] files = file.list();
                                FileOutputStream out = new FileOutputStream("answer.out");
                                ObjectOutputStream oos = new ObjectOutputStream(out);
                                ServerAnswerLs serverAnswerLs = new ServerAnswerLs();
                                for (String s : files) {
                                    serverAnswerLs.getArrayList().add(s);
                                }
                                enter = enter1;
                                oos.writeObject(serverAnswerLs);
                                oos.flush();
                                oos.close();
                                dataOutputStream.writeUTF("lsAnswer");
                                dataOutputStream.flush();
                            } else {
                                dataOutputStream.writeUTF("Команда \"" + message + "\" не поддерживается программой!");
                                dataOutputStream.flush();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Один из клиентов отключен!");
        }

    }

    @Override
    public void stop() {
        if (socket_ != null) {
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(socket_.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataOutputStream.writeUTF("Socket is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
