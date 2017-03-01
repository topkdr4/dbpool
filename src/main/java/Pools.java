import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class Pools {
    
    private static final Map<String, PoolConnection> POOL_CONNECTION_MAP = new ConcurrentHashMap<>();
    private static Pools pools = new Pools();
    
    
    public static Pools getInstance() {
        return pools;
    }
    
    public Connection getConnection(String poolName) throws SQLException {
        return POOL_CONNECTION_MAP.get(poolName).getConnection();
    }
    
    public void addPool(PoolConnection pool) throws IOException, SQLException {
        pool.createConnections();
        POOL_CONNECTION_MAP.put(pool.getName(), pool);
        
    }
    
}
