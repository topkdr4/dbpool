import java.util.concurrent.*;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class Test {
    
    public static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    public static Semaphore semaphore = new Semaphore(Integer.MAX_VALUE);
    public static final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    public static void main(String[] args) throws InterruptedException {
    
        for (int i = 0; i < 100; i++) {
            queue.add(random.nextInt(9000));
        }
    
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(()->{
                
                String name = Thread.currentThread().getName();
                
                while (true) {
                    try {
                        int value = get();
                        System.out.println("[" + name + "] получил: " + value);
                        Thread.sleep(3000);
                        push(value);
                        System.out.println("[" + name + "] вернул значение");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i));
            t.setDaemon(true);
            t.start();
        }
        
        Thread.sleep(5000);
        stop();
        Thread.sleep(15000);
        go();
        Thread.sleep(5000);
        
    }
    
    
    public static void push(int value) throws InterruptedException {
        semaphore.tryAcquire(1, TimeUnit.HOURS);
        queue.add(value);
        System.out.println("РАЗМЕР ПУЛА - " + queue.size());
    }
    
    public static int get() throws InterruptedException {
        semaphore.tryAcquire(1, TimeUnit.HOURS);
        int value = queue.poll(1, TimeUnit.SECONDS);
        System.out.println("РАЗМЕР ПУЛА - " + queue.size());
        return value;
    }
    
    public static void stop() {
        semaphore = new Semaphore(0);
        System.out.println("СТОП");
    }
    
    public static void go() {
        semaphore.release();
        semaphore = new Semaphore(Integer.MAX_VALUE);
        System.out.println("Продолжаем");
    }
    
    
}
