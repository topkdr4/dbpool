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
        
        if (value == null)
            return null;
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public Double getDoubleValue(String key) {
        String value = properties.getProperty(key);
        
        if (value == null)
            return null;
        
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public Long getLongValue(String key) {
        String value = properties.getProperty(key);
        
        if (value == null)
            return null;
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public Byte getByteValue(String key) {
        String value = properties.getProperty(key);
        
        if (value == null)
            return null;
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public Boolean getBooleanValue(String key) {
        String value = properties.getProperty(key);
    
        if (value == null)
            return null;
    
        if (value.equalsIgnoreCase("true")  || value.equalsIgnoreCase("on"))
            return Boolean.TRUE;
    
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off"))
            return Boolean.FALSE;
    
        try {
            return Integer.parseInt(value) != 0;
        } catch (NumberFormatException e) {
            return null;
        }
        
    }
    
    
    public String getStringValue(String key) {
        return properties.getProperty(key);
    }
    
}
