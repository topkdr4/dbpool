import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    
    private static final Object SYNC = new Object();
    private static final Logger logger = LogManager.getLogger(PoolConnection.class);
    private final String name;
    private DataSource source;
    private BlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
    private volatile int poolSize = 0;
    private int timeOut = 1000 * 60 * 60;
    private Configuration config;
    private String CHECK_QUERY;
    
    
    public PoolConnection(String file, String name) throws IOException {
        this.config = new Configuration(file, name);
        this.name = name;
    }
    
    
    public PoolConnection(Path path, String name) throws IOException {
        this.config = new Configuration(path, name);
        this.name = name;
    }
    
    
    public void createConnections() throws IOException, SQLException {
        this.source = config.getDataSource();
        this.poolSize = config.getPoolSize();
        this.timeOut = config.getTimeOut();
        this.CHECK_QUERY = config.getCHECK_QUERY();
    }
    
    
    public void setDataSource(DataSource source, int poolSize) throws SQLException {
        this.source = source;
        
        if (poolSize < 1)
            throw new IllegalArgumentException("pool size can't be less than 1");
        
        this.poolSize = poolSize;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void addConnector(Connection connection) {
        this.pool.add(connection);
    }
    
    
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = pool.poll(1500, TimeUnit.MILLISECONDS);
            
            try {
                //Если соединение доступно то вернем его
                if (connection == null) {
                    logger.info("Pool is empty");
                    throw new InterruptedException("null");
                }
                
                checkAvailable(connection);
                logger.info("Connection returned from pool");
                return connection;
            } catch (UnavailableException e) {
                //Соединение мертво
                logger.warn("Dead connection");
                synchronized (SYNC) {
                    logger.info("Create and return new Connection");
                    poolSize--;
                    return new Connection(source.getConnection(), this);
                }
            }
            
        } catch (InterruptedException e) {
            //Пул пустой, пытаемся создать новое соединение
            logger.info("Pool is empty, try create new Connection");
            if (poolSize > 0) {
                synchronized (SYNC) {
                    if (poolSize > 0) {
                        poolSize--;
                        logger.info("Returned new Connection");
                        return new Connection(source.getConnection(), this);
                    }
                }
            }
            
            //Соединения исчерпаны, возможно врнулось одно из соединений
            logger.warn("possible back one of the connection");
            try {
                return pool.poll(1500, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.warn(e1);
                throw new SQLException(e1);
            }
            
        }
    }
    
    private void checkAvailable(Connection connection) throws UnavailableException{
        long now = System.currentTimeMillis();
        long stamp = connection.getStamp();
        if (now - stamp >= timeOut) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(CHECK_QUERY);
                connection.setStamp(System.currentTimeMillis());
            } catch (SQLException e) {
                throw new UnavailableException(e);
            }
        }
    }
    
}
