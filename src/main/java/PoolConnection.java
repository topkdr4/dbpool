import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
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
    private static PoolConnection poolConnection = new PoolConnection();
    private BlockingQueue<Connector> pool;
    private volatile int poolSize = 0;
    private int timeOut = 1000 * 60 * 60;
    
    
    private PoolConnection() {
    }
    
    
    public static PoolConnection getInstance() {
        return poolConnection;
    }
    
    public void createConnections() throws IOException, SQLException {
        Configuration config = new Configuration("config.properties");
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
        /*
        * Sync
        * */
        this.pool.add(connector);
    }
    
    public Connector getConnection() throws SQLException {
        
        /*
        * Sync
        * */
        
        try {
            Connector connector = pool.poll(1500, TimeUnit.MILLISECONDS);
            
            try {
                //Если соединение доступно то вернем его
                if (connector == null) {
                    logger.info("Pool is empty");
                    throw new AvailableException("null");
                }
                    
                connector.checkAvailable();
                logger.info("Connection returned");
                return connector;
            } catch (AvailableException e) {
                //Соединение мертво
                logger.warn("Dead connection");
                synchronized (SYNC) {
                    logger.info("Connection returned");
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
                        logger.info("Connection returned");
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
