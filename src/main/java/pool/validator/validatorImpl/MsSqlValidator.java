package pool.validator.validatorImpl;
import pool.Connection;
import pool.validator.ConnectionValidator;

import java.sql.SQLException;
import java.sql.Statement;





/**
 * Created by vetoshkin-av on 02.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class MsSqlValidator implements ConnectionValidator {
    private static final String QUERY = "SELECT 1";
    
    
    @Override
    public boolean isValid(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(QUERY);
        } catch (SQLException e) {
            return false;
        }
        
        return true;
    }
}
