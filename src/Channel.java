import java.util.LinkedList;

/**
 * Created by Людмила on 17.03.2017.
 */
public class Channel {
    //В канале все подключения
    // Кладем сессию

    private final LinkedList<Runnable> linkedList = new LinkedList<>();
    private final static Object lock = new Object();
    private final int maxCounter;

    public Channel(int max) {
        maxCounter = max;
    }

    void put(Runnable x) {
        synchronized (lock) {
            while (maxCounter <= linkedList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.addLast(x);
            lock.notifyAll();
        }

    }
    //В диспетчере

    Runnable take() {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notifyAll();
            return linkedList.removeFirst();
        }
    }
}
