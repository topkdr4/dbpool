import pool.Pools;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeoutException;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    public static final Random r = new Random();
    public static volatile int count = 0;
    
    
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Pools pools = Pools.getInstance();
        pools.init(Paths.get("C:\\training\\dbpool\\dbpool\\src\\main\\resources\\config.properties"));
        
        for (int i = 0; i < 25; i++) {
            Thread t = new Thread(() -> {
                String name = Thread.currentThread().getName();
                while (true) {
                    try {
                        try (java.sql.Connection connection = pools.getConnection("main")) {
                            System.out.println("[" + name + "] получил соединение");
                            Thread.sleep(r.nextInt(4000));
                        } catch (SQLException e) {
                            //
                        } catch (TimeoutException e) {
                            count++;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i));
            t.setDaemon(true);
            t.start();
        }
        
        Thread.sleep(150000);
        System.out.println(count);
        
    }
    
}
