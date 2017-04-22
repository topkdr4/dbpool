package utils;
/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Properties {
    
    
    private final java.util.Properties properties;
    
    
    public Properties(java.util.Properties properties) {
        this.properties = properties;
    }
    
    
    
    public Integer getInteger(String key) {
        return Parser.parseInt(getString(key), null);
    }
    
    
    public Integer getInteger(String key, Integer defaultValue) {
        return Parser.parseInt(getString(key), defaultValue);
    }
    
    
    public Double getDouble(String key) {
        return Parser.parseDouble(getString(key), null);
    }
    
    
    public Double getDouble(String key, Double defaultValue) {
        return Parser.parseDouble(getString(key), defaultValue);
    }
    
    
    public Long getLong(String key) {
        return Parser.parseLong(getString(key), null);
    }
    
    
    public Long getLong(String key, Long defaultValue) {
        return Parser.parseLong(getString(key), defaultValue);
    }
    
    
    public Boolean getBoolean(String key) {
        return Parser.parseBoolean(getString(key), null);
    }
    
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return Parser.parseBoolean(getString(key), defaultValue);
    }
    
    
    public String getString(String key) {
        return properties.getProperty(key);
    }
    
}
