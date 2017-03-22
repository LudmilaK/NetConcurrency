/**
 * Created by Людмила on 17.03.2017.
 */
public class Dispatcher implements Runnable {
    // Канал - поле класса диспетчер
    public static Channel channel_;

    public Dispatcher(Channel ch) {
        channel_ = ch;
    }

    public void run() {
        while (true) {
            Runnable session = channel_.take();
            // Запускаем поток
            Thread thread = new Thread(session);
            thread.start();
        }
    }
}

