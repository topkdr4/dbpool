/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        PoolConnection pool = PoolConnection.getInstance();
        pool.createConnections();
        
        try(Connector connection = pool.getConnection()) {
            System.out.println(connection.connection.isValid(3));
            Thread.sleep(5000);
        }
    }
    
}
