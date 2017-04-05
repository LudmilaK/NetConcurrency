package concurrentUtils;

import concurrentUtils.Channel;

import java.util.LinkedList;

/**
 * Created by Людмила on 24.03.2017.
 */
public class ThreadPool {

    private LinkedList<WorkerThread> allWorkers = new LinkedList<WorkerThread>(); // все воркеры
    private final Object lock = new Object();
    Channel freeWorkers;
    int maxSize;

    public ThreadPool(int maxSize) {
        this.maxSize = maxSize;
        freeWorkers = new Channel(maxSize);
        WorkerThread workerThread = new WorkerThread(this);
        allWorkers.addLast(workerThread);
        freeWorkers.put(workerThread);
    }

    // проверяем на пустоту freeWorkers, если пуст, значит все воркеры заняты
    // пока размер не равен maxSize, создаем новый workerThread
    // нужна синхронизация

    public void execute(Runnable task) {
        synchronized (lock) {
            if (freeWorkers.getSize() == 0) {
                if (maxSize > allWorkers.size()) {
                    WorkerThread workerThread = new WorkerThread(this);
                    allWorkers.addLast(workerThread);
                    freeWorkers.put((Runnable) workerThread);
                }
            }
        }
        WorkerThread worker = (WorkerThread) freeWorkers.take();
        worker.execute(task);
    }

    public void onTaskCompleted(WorkerThread workerThread) {
        freeWorkers.put((Runnable) workerThread);
    }
}
