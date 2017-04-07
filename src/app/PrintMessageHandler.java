package app;

import netUtils.MessageHandler;

/**
 * Created by Людмила on 31.03.2017.
 */
public class PrintMessageHandler implements MessageHandler {

    @Override
    public void handle(String message) {
        System.out.println("Получено: " + message);
    }
}