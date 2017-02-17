import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;





/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public final class Pool {
    
    private static final Object sync = new Object();
    private static final String CHECK_CONNECTION = "SELECT 1";
    private static Pool pool;
    private final BlockingQueue<Connection> queue = new LinkedBlockingQueue<>();
    private Connect connect;
    
    private Pool(Connect connect) {
        this.connect = connect;
    }
    
    private Pool() {
        
    }
    
    
    public static Pool getInstance(Connect connect) {
        if (pool == null) {
            synchronized (sync) {
                if (pool == null) {
                    pool = new Pool(connect);
                    return pool;
                }
            }
        }
        
        return pool;
    }
    
    public static Pool getInstance() {
        if (pool == null) {
            synchronized (sync) {
                if (pool == null) {
                    pool = new Pool();
                    return pool;
                }
            }
        }
        return pool;
    }
    
    
    
    
    private void addNewSession() throws SQLException {
        queue.add(connect.openNewSession());
    }
    
    
    private void checkAvailable() {
        synchronized (queue) {
            List<Connection> tempList = new ArrayList<>();
            
            for (Connection connection : queue) {
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(CHECK_CONNECTION)) {
                    int res = 0;
                    if (resultSet.first()) {
                        res = resultSet.getInt("1");
                    }
                    
                    if (res != 0)
                        tempList.add(connection);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    public Connection getConnection() throws InterruptedException {
        return queue.poll(3, TimeUnit.SECONDS);
    }
    
    
    public void addConnection(Connection connection) {
        this.queue.add(connection);
    }
}
