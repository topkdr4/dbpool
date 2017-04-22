package pool;
import utils.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class Pools {
    
    private static final Map<String, PoolConnection> POOL_CONNECTION_MAP = new ConcurrentHashMap<>();
    private static final Pools pools = new Pools();
    
    
    
    private Pools() {
    }
    
    
    public static Pools getInstance() {
        return pools;
    }
    
    
    public Connection getConnection(String poolName) throws SQLException, TimeoutException {
        return POOL_CONNECTION_MAP.get(poolName).getConnection();
    }
    
    
    public void init(Path target) throws IOException, SQLException {
        java.util.Properties prop = new java.util.Properties();
        try (InputStream stream = Files.newInputStream(target)) {
            prop.load(stream);
        }
        Properties config = new Properties(prop);
        Set<String> result = new HashSet<>();
        for (String key : prop.stringPropertyNames()) {
            int position = key.indexOf(".");
            result.add(key.substring(0, position));
        }
        for (String s : result) {
            POOL_CONNECTION_MAP.put(s, new PoolConnection(config, s));
        }
    }
    
}
