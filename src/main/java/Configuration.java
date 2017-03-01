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
    private final String name;
    private String CHECK_QUERY;
    
    
    public Configuration(String resourcesFile, String name) throws IOException {
        this.configuration = new Propertie(resourcesFile);
        this.name = name;
    }
    
    public Configuration(Path path, String name) throws IOException {
        this.configuration = new Propertie(path);
        this.name = name;
    }
    
    
    public DataSource getDataSource() throws IOException {
        String bdType = configuration.getStringValue(name + ".type");
        switch (bdType) {
            case "mysql": {
                CHECK_QUERY = "SELECT 1";
                MysqlDataSource source = new MysqlDataSource();
                source.setDatabaseName(configuration.getStringValue(name + ".databaseName"));
                source.setServerName(configuration.getStringValue(name + ".serverName"));
                source.setUser(configuration.getStringValue(name + ".user"));
                source.setPort(configuration.getIntValue(name + ".port"));
                source.setPassword(configuration.getStringValue(name + ".password"));
                return source;
            }
            
            default: throw new IllegalArgumentException("Unknown data base type");
        }
    }
    
    
    public Propertie getConfiguration() {
        return configuration;
    }
    
    
    public int getPoolSize() {
        return configuration.getIntValue(name + ".size");
    }
    
    
    public int getTimeOut() {
        return configuration.getIntValue(name + ".timeout");
    }
    
    
    public String getCHECK_QUERY() {
        return CHECK_QUERY;
    }
}
