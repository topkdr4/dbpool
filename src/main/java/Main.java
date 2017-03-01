import java.util.Random;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    public static final Random r = new Random();
    
    public static void main(String[] args) throws Exception {
        
        PoolConnection pool = PoolConnection.getInstance();
        pool.createConnections();
    
        for (int i = 0; i < 14; i++) {
            Thread t = new Thread(()->{
                while (true) {
                    PoolConnection connection = PoolConnection.getInstance();
                    try {
                        try (Connector connector = connection.getConnection()) {
                            Thread.sleep(r.nextInt(8000));
                        }
                    } catch (Exception e) {
                        //ignore
                    }
                }
                
            }, String.valueOf(i));
            t.setDaemon(true);
            t.start();
        }
        
        
        Thread.sleep(30000);
    }
    
}
