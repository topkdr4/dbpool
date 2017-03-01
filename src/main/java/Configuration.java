import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public final class Configuration {
    
    private Propertie configuration;
    private final String resourcesFile;
    
    
    public Configuration(String resourcesFile) {
        this.resourcesFile = resourcesFile;
    }
    
    
    public DataSource getDataSource() throws IOException {
        configuration = new Propertie(resourcesFile);
        String bdType = configuration.getStringValue("main.Type");
        switch (bdType) {
            case "my_sql": {
                MysqlDataSource source = new MysqlDataSource();
                source.setDatabaseName(configuration.getStringValue("main.DatabaseName"));
                source.setServerName(configuration.getStringValue("main.ServerName"));
                source.setUser(configuration.getStringValue("main.User"));
                source.setPort(configuration.getIntValue("main.Port"));
                source.setPassword(configuration.getStringValue("main.Password"));
                return source;
            }
            
            default: throw new IllegalArgumentException("Unknown data base type");
        }
    }
    
    
    public Propertie getConfiguration() {
        return configuration;
    }
    
    
    public int getPoolSize() {
        return configuration.getIntValue("main.Size");
    }
    
    
    public int getTimeOut() {
        return configuration.getIntValue("main.Available");
    }
}
