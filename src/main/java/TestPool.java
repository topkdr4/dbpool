import java.util.concurrent.*;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class TestPool {
    
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static TestPool pool = new TestPool();
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private Semaphore semaphore = new Semaphore(Integer.MAX_VALUE);
    
    
    private TestPool() {
        for (int i = 0; i < 5000; i++) {
            queue.add(random.nextInt());
        }
    }
    
    
    public static TestPool getInstance() {
        return pool;
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(() -> {
                TestPool pool = TestPool.getInstance();
                Thread cur = Thread.currentThread();
                while (true) {
                    try {
                        System.out.println("Поток [" + cur.getName() + "] получил - " + pool.getConnect());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        Thread.sleep(random.nextInt(3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    pool.setConnect(random.nextInt());
                    
                }
                
            }, String.valueOf(i));
            thread.setDaemon(true);
            thread.start();
        }
        
        Thread.sleep(10000);
        
        System.out.println("УСЫПЛЯЕМ");
        TestPool pp = TestPool.getInstance();
        pp.stop();
        Thread.sleep(10000);
        pp.continues();
        
    }
    
    
    public int getConnect() throws InterruptedException {
        while (!semaphore.tryAcquire()){}
        return queue.poll(3000, TimeUnit.SECONDS);
    }
    
    
    public void setConnect(int value) {
        queue.add(value);
    }
    
    
    public void stop() throws InterruptedException {
        //semaphore.
    }
    
    
    public void continues() throws InterruptedException {
        semaphore.acquire(Integer.MAX_VALUE);
    }
    
    
}
