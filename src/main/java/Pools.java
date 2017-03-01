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
    
    public PoolConnection getPool(String poolName) {
        return POOL_CONNECTION_MAP.get(poolName);
    }
    
    public void addPool(String name, PoolConnection pool) {
        POOL_CONNECTION_MAP.put(name, pool);
    }
    
}
