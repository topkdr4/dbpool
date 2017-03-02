package pool;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import pool.validator.ConnectionValidator;
import pool.validator.validatorImpl.MsSqlValidator;
import pool.validator.validatorImpl.MySqlValidator;
import pool.validator.validatorImpl.OracleValidator;
import pool.validator.validatorImpl.PGValidator;
import utils.DataSourceProperties;
import utils.Properties;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
final class PoolConnection {
    
    private static final Logger logger = LogManager.getLogger(PoolConnection.class);
    private final DataSourceProperties prop;
    private final BlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
    private ConnectionValidator validator;
    private DataSource source;
    private int capacity;
    private int timeOut;
    private int checkTime;
    
    
    PoolConnection(Properties properties, String name) throws IOException, SQLException {
        this.prop = new DataSourceProperties(properties, name);
        setConfiguration();
    }
    
    void returnConnection(Connection connection) {
        this.pool.add(connection);
        logger.info("connection returned");
    }
    
    synchronized Connection getConnection() throws SQLException, TimeoutException {
        Connection connection;
        try {
            connection = pool.poll(timeOut, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SQLException(e);
        }
        if (connection == null) {
            if (capacity > 0) {
                capacity--;
                logger.info("new Connection");
                return new Connection(source.getConnection(), this);
            }
            
            throw new TimeoutException("Oops");
        }
    
        long now = System.currentTimeMillis();
        
        if (now - connection.getStamp() >= checkTime) {
            if (!validator.isValid(connection)) {
                connection.close();
                logger.info("dead connection, new connection");
                return new Connection(source.getConnection(), this);
            }
            connection.setStamp(now);
        }
        
    
        return connection;
    }
    
    private void setConfiguration() throws IOException, SQLException {
        String bdType = prop.getType();
        switch (bdType) {
            case "mysql": {
                MysqlDataSource source = new MysqlDataSource();
                source.setDatabaseName(prop.getDatabaseName());
                source.setServerName(prop.getServerName());
                source.setUser(prop.getUser());
                source.setPort(prop.getPort());
                source.setPassword(prop.getPassword());
                
                this.source = source;
                this.capacity = prop.getCapacity();
                this.timeOut = prop.getTimeOut();
                this.checkTime = prop.getCheckTime();
                this.validator = new MySqlValidator();
                break;
            }
            
            case "oracle": {
                OracleDataSource source = new OracleDataSource();
                source.setDatabaseName(prop.getDatabaseName());
                source.setServerName(prop.getServerName());
                source.setUser(prop.getUser());
                source.setPortNumber(prop.getPort());
                source.setPassword(prop.getPassword());
                source.setDriverType(prop.getDriverType());
    
                this.source = source;
                this.capacity = prop.getCapacity();
                this.timeOut = prop.getTimeOut();
                this.checkTime = prop.getCheckTime();
                this.validator = new OracleValidator();
                break;
            }
            
            case "mssql": {
                SQLServerDataSource source = new SQLServerDataSource();
                source.setDatabaseName(prop.getDatabaseName());
                source.setServerName(prop.getServerName());
                source.setUser(prop.getUser());
                source.setPortNumber(prop.getPort());
                source.setPassword(prop.getPassword());
    
                this.source = source;
                this.capacity = prop.getCapacity();
                this.timeOut = prop.getTimeOut();
                this.checkTime = prop.getCheckTime();
                this.validator = new MsSqlValidator();
                break;
            }
            
            case "pgsql": {
                Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
                source.setDatabaseName(prop.getDatabaseName());
                source.setServerName(prop.getServerName());
                source.setUser(prop.getUser());
                source.setPortNumber(prop.getPort());
                source.setPassword(prop.getPassword());
    
                this.source = source;
                this.capacity = prop.getCapacity();
                this.timeOut = prop.getTimeOut();
                this.checkTime = prop.getCheckTime();
                this.validator = new PGValidator();
                break;
            }
            
            default:
                throw new IllegalArgumentException("Unknown data base type");
        }
    }
    
    
}
