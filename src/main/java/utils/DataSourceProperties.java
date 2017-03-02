package utils;
/**
 * Created by vetoshkin-av on 02.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class DataSourceProperties {
    
    private final Properties prop;
    private final String prefix;
    
    
    public DataSourceProperties(Properties prop, String prefix) {
        this.prop = prop;
        this.prefix = prefix;
    }
    
    
    public String getType() {
        return prop.getString(prefix + ".type");
    }
    
    
    public String getDatabaseName() {
        return prop.getString(prefix + ".databaseName");
    }
    
    
    public String getServerName() {
        return prop.getString(prefix + ".serverName");
    }
    
    
    public String getUser() {
        return prop.getString(prefix + ".user");
    }
    
    
    public Integer getPort() {
        return prop.getInteger(prefix + ".port");
    }
    
    
    public String getPassword() {
        return prop.getString(prefix + ".password");
    }
    
    
    public Integer getCapacity() {
        return prop.getInteger(prefix + ".capacity");
    }
    
    
    public Integer getWaitTime() {
        return prop.getInteger(prefix + ".timeOut");
    }
    
    
    public Integer getCheckTime() {
        return prop.getInteger(prefix + ".checkTime");
    }
    
    
    public String getDriverType() {
        return prop.getString(prefix + ".driverType");
    }
}
