import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public final class ScheduledTask {

    private static final ThreadFactory daemonThread = (r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    };

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10, daemonThread);


    private ScheduledTask() {
    }


    public static void execute(long time_stamp, long delay, Exec task) {
        executorService.scheduleAtFixedRate(new Task(task), delay, time_stamp, TimeUnit.SECONDS);
    }


    @FunctionalInterface
    public interface Exec {
        void exec();
    }


    public static class Task implements Runnable {

        private final Exec exec;

        public Task(Exec exec) {
            this.exec = exec;
        }

        @Override
        public void run() {
            exec.exec();
        }
    }
}
