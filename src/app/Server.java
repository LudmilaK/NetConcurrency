package app;

import concurrentUtils.Channel;
import concurrentUtils.Dispatcher;
import concurrentUtils.ThreadPool;
import netUtils.Host;
import netUtils.MessageHandlerFactory;

/**
 * Created by Людмила on 13.02.2017.
 */
public class Server {
    public Server() throws ClassNotFoundException {
    }

    // может запускать несколько хостов
    public static void main(String[] args) {
        Class classMessageHandlerFactory = null;
        MessageHandlerFactory messageHandlerFactory = null;
        try {
            classMessageHandlerFactory = Class.forName("app.PrintMessageHandlerFactory"); // рефлексия
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            messageHandlerFactory = (MessageHandlerFactory) classMessageHandlerFactory.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Channel channel = new Channel(3);
        int port = 0;
        port = Integer.valueOf(args[0]);
        Host host = new Host(port, channel, messageHandlerFactory);
        host.start();
        ThreadPool threadPool = new ThreadPool(3);
        Dispatcher dispatcher = new Dispatcher(channel, threadPool);
        dispatcher.start();
        // Нужно остановить Dispatcher и Host, оповестить и остановить всех клиентов (остановить ThreadPool)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { // создаем инстанс рантайма (getRuntime());
            host.stop();
            dispatcher.stop();
            threadPool.stop();
        }));
    }
}

// фабрику прокидываем в хост, через аргумент конструктора хоста передаем реализацию фабрики.
//В хосте конструктор принимает интерфейс.
// В конструктор сессиии передаем конкретный хендлер MHF.createHandler
// Логику сообщений выносим