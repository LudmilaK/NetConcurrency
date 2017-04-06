package app;
import netUtils.MessageHandler;
import netUtils.MessageHandlerFactory;

/**
 * Created by Людмила on 31.03.2017.
 */
public class PrintMessageHandlerFactory implements MessageHandlerFactory {

    @Override
    public MessageHandler create() {
        PrintMessageHandler printMessageHandler = new PrintMessageHandler();
        return printMessageHandler;
    }
}
