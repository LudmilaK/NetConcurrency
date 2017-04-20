package concurrentUtils;

/**
 * Created by Людмила on 17.03.2017.
 */
public class Dispatcher implements Stoppable {
    public static Channel channel_;
    volatile boolean isActive;
    Thread thread;
    ThreadPool threadPool;

    public Dispatcher(Channel ch, ThreadPool th) {
        channel_ = ch;
        threadPool = th;
        isActive = true;
    }

    public void run() {
        while (isActive) {
            Stoppable session = channel_.take();
            threadPool.execute(session);
        }
    }

    public void start() {
        thread = new Thread((Runnable) this);
        thread.start();
    }

    @Override
    public void stop() {
        if (isActive) {
            isActive = false;
            if (channel_.getSize() != 0) {
                for (int i = 0; i < channel_.getSize(); i++) {
                    channel_.take().stop();
                }
            }
        }
        // если размер channel_ > 0, то проходим по всем таскам  из channel_ и останавливаем их
    }
}

