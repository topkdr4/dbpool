import pool.PoolConnection;
import pool.Pools;

import java.io.IOException;
import java.sql.*;
import java.util.Random;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    public static final Random r = new Random();
    
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Pools pools = Pools.getInstance();
        PoolConnection main = new PoolConnection("config.properties", "main");
        pools.addPool(main);
    
        for (int i = 0; i < 13; i++) {
            Thread t = new Thread(()-> {
                String name = Thread.currentThread().getName();
                while (true) {
                    try {
                        try (java.sql.Connection connection = pools.getConnection("main")) {
                            System.out.println("[" + name + "] получил соединение");
                            Thread.sleep(r.nextInt(40000));
                        } catch (SQLException e) {
                            //
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i));
            t.setDaemon(true);
            t.start();
        }
        
        Thread.sleep(400000);
        
    }
    
}
