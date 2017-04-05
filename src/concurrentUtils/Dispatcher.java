package concurrentUtils;

import concurrentUtils.Channel;
import concurrentUtils.ThreadPool;

/**
 * Created by Людмила on 17.03.2017.
 */
public class Dispatcher implements Runnable {
    public static Channel channel_;
    //boolean isActive;
    Thread thread;
    ThreadPool threadPool;

    public Dispatcher(Channel ch, ThreadPool th) {
        channel_ = ch;
        threadPool = th;
      //  isActive =false;
    }

    public void run() {
        while (true) {
            Runnable session = channel_.take();
            threadPool.execute(session);
        }
    }

    public void start() {
        thread = new Thread((Runnable) this);
        thread.start();
      //  isActive = true;
    }
}

