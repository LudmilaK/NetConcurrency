/**
 * Created by Людмила on 24.03.2017.
 */
public class WorkerThread implements Runnable{

    Thread thread;
    ThreadPool threadPool;
    Runnable currentTask = null;
    private final static Object lock = new Object();

    public WorkerThread(ThreadPool thPool){
        threadPool = thPool;
        thread = new Thread(this);
        thread.start();
    }


     public void execute(Runnable task){
         synchronized (lock) {
             currentTask = task;
             lock.notify();
         }
     }

    public void run() {
        // если нет задачи для выполнения, ждем
        // если есть, выполняем
        // сообщаем threadPool-у, что рабочий поток свободен

        synchronized (lock) {
            while (currentTask == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentTask.run();
            threadPool.onTaskCompleted(this);
            currentTask = null;
        }
    }
}
