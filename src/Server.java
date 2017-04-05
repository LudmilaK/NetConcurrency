import concurrentUtils.Channel;
import concurrentUtils.Dispatcher;
import concurrentUtils.ThreadPool;
import netUtils.Host;
import netUtils.MessageHandler;
import netUtils.MessageHandlerFactory;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Server {
    public Server() throws ClassNotFoundException {
    }

    // может запускать несколько хостов
    public static void main(String[] args) {
        Channel channel = new Channel(3);
        int port = 0;
        port = Integer.valueOf(args[0]);
        Host host = new Host(port, channel);
        host.start();
        ThreadPool threadPool = new ThreadPool(3);
        Dispatcher dispatcher = new Dispatcher(channel, threadPool);
        dispatcher.start();
    }

  /*  MessageHandlerFactory mHF = null;
    Class classFactory = Class.forName("app.PrintMessageFactory");

    mHF = (MessageHandler) classFactory.newInstance();*/
}

// фабрику прокидываем в хост, через аргумент конструктора хоста передаем реализацию фабрики.
//В хосте конструктор принимает интерфейс.
// В конструктор сессиии передаем конкретный хендлер MHF.createHandler
// Логику сообщений выносим