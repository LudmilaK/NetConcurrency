import java.util.LinkedList;

/**
 * Created by Людмила on 24.03.2017.
 */
public class ThreadPool {

    private LinkedList<WorkerThread> allWorkers; // все воркеры
    private final static Object lock = new Object();
    Channel freeWorkers;
    int maxSize;

    public ThreadPool(int max) {
        maxSize = max;
        allWorkers = new LinkedList<WorkerThread>();
        freeWorkers = new Channel(max);
        //WorkerThread workerThread = new WorkerThread(this);
        //allWorkers.addLast(workerThread);
        //freeWorkers.put(workerThread);
    }

    // проверяем на пустоту freeWorkers, если пуст, значит все воркеры заняты
    // пока размер не равен maxSize, создаем новый workerThread
    // нужна синхронизация

    public void execute(Runnable x) {
        synchronized (lock) {
            if (freeWorkers.getSize() == 0) {

                while (allWorkers.size() >= maxSize) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                WorkerThread workerThread = new WorkerThread(this);
                allWorkers.addLast(workerThread);
                freeWorkers.put((Runnable) workerThread);
            }
        }
        WorkerThread worker = (WorkerThread) freeWorkers.take();
        worker.execute(x);
    }

    public void onTaskCompleted(WorkerThread workerThread) {
        synchronized (lock) {
            freeWorkers.put((Runnable) workerThread);
            lock.notifyAll();
        }
    }
}
