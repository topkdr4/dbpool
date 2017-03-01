/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public final class Parser {
    
    private Parser() {
        
    }
    
    
    public static Integer parseInt(String stringValue, Integer defaultValue) {
        
        if (stringValue == null)
            return defaultValue;
        try {
            return Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        
    }
    
    
    public static Double parseDouble(String stringValue, Double defaultValue) {
        
        if (stringValue == null)
            return defaultValue;
        try {
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        
    }
    
    
    public static Long parseLong(String stringValue, Long defaultValue) {
        
        if (stringValue == null)
            return defaultValue;
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        
    }
    
    
    
    public static Boolean parseBoolean(String stringValue, Boolean defaultValue) {
        if (stringValue == null)
            return defaultValue;
        
        if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("on"))
            return Boolean.TRUE;
        
        if (stringValue.equalsIgnoreCase("false") || stringValue.equalsIgnoreCase("off"))
            return Boolean.FALSE;
        
        try {
            return Integer.parseInt(stringValue) != 0;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    
    public static Byte parseByte(String stringValue, Byte defaultValue) {
        
        if (stringValue == null)
            return defaultValue;
        
        try {
            return Byte.parseByte(stringValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        
    }
    
}
