import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;





/**
 * Created by vetoshkin-av on 28.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Propertie {
    
    
    private Properties properties = new Properties();
    
    
    public Propertie(Properties properties) {
        this.properties = properties;
    }
    
    
    public Propertie(InputStream stream) throws IOException {
        properties.load(stream);
    }
    
    
    public Propertie(Path pathToFile) throws IOException {
        try (InputStream stream = Files.newInputStream(pathToFile)) {
            properties.load(stream);
        }
    }
    
    
    public Propertie(String resourceFile) throws IOException {
        try (InputStream stream = this.getClass().getResourceAsStream(resourceFile)) {
            properties.load(stream);
        }
    }
    
    
    public Integer getIntValue(String key) {
        String value = properties.getProperty(key);
        return Parser.parseInt(value, null);
    }
    
    
    public Integer getIntValue(String key, Integer defaultValue) {
        String value = properties.getProperty(key);
        return Parser.parseInt(value, defaultValue);
    }
    
    
    public Double getDoubleValue(String key) {
        String value = properties.getProperty(key);
        return Parser.parseDouble(value, null);
    }
    
    
    public Double getDoubleValue(String key, Double defaultValue) {
        String value = properties.getProperty(key);
        return Parser.parseDouble(value, defaultValue);
    }
    
    
    public Long getLongValue(String key) {
        String value = properties.getProperty(key);
        return Parser.parseLong(value, null);
    }
    
    
    public Long getLongValue(String key, Long defaultValue) {
        String value = properties.getProperty(key);
        return Parser.parseLong(value, defaultValue);
    }
    
    
    public Byte getByteValue(String key) {
        String value = properties.getProperty(key);
        return Parser.parseByte(value, null);
    }
    
    
    public Byte getByteValue(String key, Byte defaultValue) {
        String value = properties.getProperty(key);
        return Parser.parseByte(value, defaultValue);
    }
    
    
    public Boolean getBooleanValue(String key) {
        String value = properties.getProperty(key);
        return Parser.parseBoolean(value, null);
    }
    
    
    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return Parser.parseBoolean(value, defaultValue);
    }
    
    
    public String getStringValue(String key) {
        return properties.getProperty(key);
    }
    
}
