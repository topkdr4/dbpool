package pool;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import utils.DataSourceProperties;

import javax.sql.DataSource;
import java.sql.SQLException;





/**
 * Created by vetoshkin-av on 02.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class DataSourceFactory {
    
    private DataSourceFactory() {
        
    }
    
    
    static DataSource getMySQL(DataSourceProperties prop) {
        MysqlDataSource source = new MysqlDataSource();
        source.setDatabaseName(prop.getDatabaseName());
        source.setServerName(prop.getServerName());
        source.setUser(prop.getUser());
        source.setPort(prop.getPort());
        source.setPassword(prop.getPassword());
        return source;
    }
    
    static DataSource getOracle(DataSourceProperties prop) throws SQLException {
        OracleDataSource source = new OracleDataSource();
        source.setDatabaseName(prop.getDatabaseName());
        source.setServerName(prop.getServerName());
        source.setUser(prop.getUser());
        source.setPortNumber(prop.getPort());
        source.setPassword(prop.getPassword());
        source.setDriverType(prop.getDriverType());
        return source;
    }
    
    static DataSource getMsSQL(DataSourceProperties prop) {
        SQLServerDataSource source = new SQLServerDataSource();
        source.setDatabaseName(prop.getDatabaseName());
        source.setServerName(prop.getServerName());
        source.setUser(prop.getUser());
        source.setPortNumber(prop.getPort());
        source.setPassword(prop.getPassword());
        return source;
    }
    
    static DataSource getPGSQL(DataSourceProperties prop) {
        Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
        source.setDatabaseName(prop.getDatabaseName());
        source.setServerName(prop.getServerName());
        source.setUser(prop.getUser());
        source.setPortNumber(prop.getPort());
        source.setPassword(prop.getPassword());
        return source;
    }
    
}
