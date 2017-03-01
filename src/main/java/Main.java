import java.io.IOException;
import java.sql.SQLException;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    public static void main(String[] args) throws IOException, SQLException {
        Pools pools = Pools.getInstance();
        PoolConnection main = new PoolConnection("config.properties", "main");
        pools.addPool(main);
        
        
        
    }
    
}
