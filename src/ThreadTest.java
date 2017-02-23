/**
 * Created by Людмила on 17.02.2017.
 */
public class ThreadTest implements Runnable {

    int threadNumber_;

    public ThreadTest(int threadNumber) {
        this.threadNumber_ = threadNumber;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                System.out.println("thread №" + threadNumber_);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
