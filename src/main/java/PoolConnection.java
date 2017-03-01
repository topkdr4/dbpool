import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
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
    private DataSource source;
    private BlockingQueue<Connector> pool;
    private volatile int poolSize = 0;
    private int timeOut = 1000 * 60 * 60;
    private Configuration config;
    
    
    public PoolConnection(String file) throws IOException {
        this.config = new Configuration(file);
    }
    
    public PoolConnection(Path path) throws IOException {
        this.config = new Configuration(path);
    }
    
    
    private void createConnections() throws IOException, SQLException {
        source = config.getDataSource();
        poolSize = config.getPoolSize();
        timeOut = config.getTimeOut();
        pool = new LinkedBlockingQueue<>(poolSize);
    }
    
    public void setDataSource(DataSource source, int poolSize) throws SQLException {
        this.source = source;
        
        if (poolSize < 1)
            throw new IllegalArgumentException("pool size can't be less than 1");
        
        this.poolSize = poolSize;
        pool = new LinkedBlockingQueue<>();
    }
    
    public void addConnector(Connector connector) {
        this.pool.add(connector);
    }
    
    public Connector getConnection() throws SQLException {
        try {
            Connector connector = pool.poll(1500, TimeUnit.MILLISECONDS);
            
            try {
                //Если соединение доступно то вернем его
                if (connector == null) {
                    logger.info("Pool is empty");
                    throw new InterruptedException("null");
                }
                    
                connector.checkAvailable();
                logger.info("Connection returned from pool");
                return connector;
            } catch (UnavailableException e) {
                //Соединение мертво
                logger.warn("Dead connection");
                synchronized (SYNC) {
                    logger.info("Create and return new Connection");
                    poolSize--;
                    return new Connector(source.getConnection(), timeOut);
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
                        return new Connector(source.getConnection(), timeOut);
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
    
}
