/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Connection implements AutoCloseable {
    
    private final java.sql.Connection connection;
    
    public Connection(java.sql.Connection connection){
        this.connection = connection;
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
