package concurrentUtils;
/**
 * Created by Людмила on 24.03.2017.
 */
public class WorkerThread implements Stoppable {

    private volatile boolean isActive;
    private Thread thread;
    private ThreadPool threadPool;
    private Stoppable currentTask = null;
    private final Object lock = new Object();

    public WorkerThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
        thread = new Thread(this);
        thread.start();
        isActive = true;
    }


    public void execute(Stoppable task) {
        synchronized (lock) {
            if(currentTask != null){  // на сучай, если наш concurrentUtils.WorkerThread используется кем-то еще
                new IllegalStateException();
            }
            currentTask = task;
            lock.notifyAll();
        }
    }

    public void run() {
        // если нет задачи для выполнения, ждем
        // если есть, выполняем
        // сообщаем threadPool-у, что рабочий поток свободен

        synchronized (lock) {
            while (isActive) {
                while (currentTask == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) { // выбрасывается ошибка, так как засетили interrupt
                        if(!isActive){
                           return;
                        }
                    }
                }
                try{
                    currentTask.run(); // таска закончилась или упала
                }
                catch (RuntimeException e){
                    e.printStackTrace();
                }
                finally {
                    currentTask = null;
                    threadPool.onTaskCompleted(this);
                }
            }
        }
    }

    @Override
    public void stop() {
        if(isActive){
            isActive = false;
            thread.interrupt(); // устанавливаем флаг текущего потока на interrupted
        }
        if(currentTask != null){
            currentTask.stop();
        }
    }
}
