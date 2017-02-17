import java.sql.SQLException;





/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    
    
    public static void main(String[] args) throws InterruptedException, SQLException {
        
        Configurator configurator = new Configurator("localhost", 3306, "root", "acef13da09", "footballl");
        Connect connect = new Connect(DataBase.MY_SQL, configurator);
        connect.createConnection();
    }
    
}
