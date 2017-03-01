import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public final class Configuration {
    
    private Propertie configuration;
    
    
    public Configuration(String resourcesFile) throws IOException {
        this.configuration = new Propertie(resourcesFile);
    }
    
    public Configuration(Path path) throws IOException {
        this.configuration = new Propertie(path);
    }
    
    
    public DataSource getDataSource() throws IOException {
        String bdType = configuration.getStringValue("main.type");
        switch (bdType) {
            case "mysql": {
                MysqlDataSource source = new MysqlDataSource();
                source.setDatabaseName(configuration.getStringValue("main.databaseName"));
                source.setServerName(configuration.getStringValue("main.serverName"));
                source.setUser(configuration.getStringValue("main.user"));
                source.setPort(configuration.getIntValue("main.port"));
                source.setPassword(configuration.getStringValue("main.password"));
                return source;
            }
            
            default: throw new IllegalArgumentException("Unknown data base type");
        }
    }
    
    
    public Propertie getConfiguration() {
        return configuration;
    }
    
    
    public int getPoolSize() {
        return configuration.getIntValue("main.size");
    }
    
    
    public int getTimeOut() {
        return configuration.getIntValue("main.timeout");
    }
}
