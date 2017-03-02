package pool.validator;





import pool.Connection;





/**
 * Created by vetoshkin-av on 02.03.2017.
 * vetoshkin-av@dartit.ru
 */
public interface ConnectionValidator {
    
    public boolean isValid(Connection connection);
    
}
