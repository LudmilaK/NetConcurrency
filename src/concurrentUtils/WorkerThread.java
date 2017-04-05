package concurrentUtils;

import concurrentUtils.ThreadPool;

/**
 * Created by Людмила on 24.03.2017.
 */
public class WorkerThread implements Runnable {

    private Thread thread;
    private ThreadPool threadPool;
    private Runnable currentTask = null;
    private final Object lock = new Object();

    public WorkerThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
        thread = new Thread(this);
        thread.start();
    }


    public void execute(Runnable task) {
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
            while (true) {
                while (currentTask == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
}
