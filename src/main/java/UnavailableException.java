import java.sql.SQLException;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class UnavailableException extends Exception {
    
    private static final long serialVersionUID = 2361235536265916410L;
    
    public UnavailableException(String string) {
        super(string);
    }
    
    
    public UnavailableException(SQLException e) {
        super(e);
    }
}
