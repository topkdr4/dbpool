import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;





/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Connection implements AutoCloseable {
    
    private final java.sql.Connection connection;
    
    public Connection(java.sql.Connection connection){
        this.connection = connection;
    }
    
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }
    
    public PreparedStatement preparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
    
    @Override
    public void close() throws Exception {
        Pool pool = Pool.getInstance();
        pool.addConnection(this);
    }
}
