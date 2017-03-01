import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;





/**
 * Created by vetoshkin-av on 01.03.2017.
 * vetoshkin-av@dartit.ru
 */
public class Connector implements AutoCloseable  {
    
    private static final String CHECK = "SELECT 1";
    private final java.sql.Connection connection;
    private final int timeOut;
    private long stamp = System.currentTimeMillis();
    
    
    public Connector(Connection connection, int timeOut) {
        this.connection = connection;
        this.timeOut = timeOut;
    }
    
    
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }
    
    public PreparedStatement preparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
    
    public void checkAvailable() throws AvailableException {
        long now = System.currentTimeMillis();
        if (now - stamp >= timeOut) {
            try (Statement statement = createStatement()) {
                statement.executeQuery(CHECK);
                stamp = System.currentTimeMillis();
            } catch (SQLException e) {
                throw new AvailableException(e);
            }
        }
    }
    
    @Override
    public void close() throws Exception {
        PoolConnection pool = PoolConnection.getInstance();
        pool.addConnector(this);
    }
}
