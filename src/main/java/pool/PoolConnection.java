package pool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class PoolConnection {
    
    private static final Logger logger = LogManager.getLogger(PoolConnection.class);
    private final String name;
    private DataSource source;
    private BlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
    private Configuration config;
    private String CHECK_QUERY;
    private int capacity;
    private int waiting;
    private int checkTime;
    
    
    public PoolConnection(String file, String name) throws IOException, SQLException {
        this.config = new Configuration(file, name);
        this.name = name;
        loadPoolConfiguration();
    }
    
    
    public PoolConnection(Path path, String name) throws IOException, SQLException {
        this.config = new Configuration(path, name);
        this.name = name;
        loadPoolConfiguration();
    }
    
    
    private void loadPoolConfiguration() throws IOException, SQLException {
        this.source = config.getDataSource();
        this.capacity = config.getCapacity();
        this.waiting = config.getWait();
        this.checkTime = config.getCheckTime();
        this.CHECK_QUERY = config.getCHECK_QUERY();
    }
    
    
    public String getName() {
        return name;
    }
    
    
    void addConnection(Connection connection) {
        this.pool.add(connection);
        logger.info("connection returned");
    }
    
    synchronized Connection getConnection() throws SQLException {
        Connection connection;
        try {
            connection = pool.poll(waiting, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SQLException(e);
        }
        if (connection == null) {
            if (capacity > 0) {
                capacity--;
                logger.info("new pool.Connection");
                return new Connection(source.getConnection(), this);
            }
            
            throw new SQLException("pool.Connection not found");
        }
    
        
        if (!checkAvailable(connection)) {
            connection.close();
            logger.info("dead connection, new connection");
            return new Connection(source.getConnection(), this);
        }
    
        return connection;
    }
    
    
    private boolean checkAvailable(Connection connection) {
        long now = System.currentTimeMillis();
        long stamp = connection.getStamp();
        if (now - stamp >= checkTime) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(CHECK_QUERY);
                connection.setStamp(System.currentTimeMillis());
            } catch (SQLException e) {
                return false;
            }
        }
        
        return true;
    }
    
}
