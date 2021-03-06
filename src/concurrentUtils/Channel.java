package concurrentUtils;

import java.util.LinkedList;

/**
 * Created by Людмила on 17.03.2017.
 */
public class Channel {
    //В канале все подключения
    // Кладем сессию

    private final LinkedList<Object> linkedList = new LinkedList<>();
    private final static Object lock = new Object();
    private final int maxCounter;

    public Channel(int max) {
        maxCounter = max;
    }

    public void put(Runnable x) {
        synchronized (lock) {
            while (maxCounter <= linkedList.size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) { // выбрасывается блокирующими методами
                    e.printStackTrace();
                }
            }
            linkedList.addLast(x);
            lock.notifyAll();
        }

    }

    public int getSize(){
        synchronized (lock){
            return linkedList.size();
        }
    }

    Stoppable take() {
        synchronized (lock) {
            while (linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notifyAll();
            return (Stoppable) linkedList.removeFirst();
        }
    }
}
