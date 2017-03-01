import java.sql.SQLException;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class AvailableException extends Exception {
    
    private static final long serialVersionUID = 2361235536265916410L;
    
    public AvailableException(String string) {
        super(string);
    }
    
    
    public AvailableException(SQLException e) {
        super(e);
    }
}
