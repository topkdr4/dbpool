import java.sql.*;
import java.util.Objects;





/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Connect {
    
    private final DataBase dataBase;
    private final Configurator config;
    private String url;
    private int connect_timeout;
    private int max_connections;
    
    
    public Connect(DataBase dataBase, Configurator config) {
        this.dataBase = dataBase;
        this.config = config;
    }
    
    
    public void createConnection() throws SQLException {
        Objects.requireNonNull(dataBase, "Data base type not defined");
        switch (dataBase) {
            case MY_SQL: {
                url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDbname();
                testMysqlConnection();
                break;
            }
            
            case MS_SQL: {
                break;
            }
            
            case PG_SQL: {
                break;
            }
            
            case ORACLE: {
                break;
            }
        }
    }
    
    private void testMysqlConnection() throws SQLException {
        try (java.sql.Connection connection = DriverManager.getConnection(url, config.getLogin(), config.getPassword());
             Statement statement = connection.createStatement()) {
    
            String connect_timeout = "select variable_value from information_schema.SESSION_VARIABLES where VARIABLE_NAME = 'connect_timeout'";
            String max_connections = "select variable_value from information_schema.SESSION_VARIABLES where VARIABLE_NAME = 'max_connections'";
    
            ResultSet resultSet = statement.executeQuery(connect_timeout);
            if (resultSet.first()) {
                this.connect_timeout = Integer.parseInt(resultSet.getString("variable_value"));
            }
    
            resultSet = statement.executeQuery(max_connections);
            if (resultSet.first()) {
                this.max_connections = Integer.parseInt(resultSet.getString("variable_value"));
            }
            
            resultSet.close();
        }
        
    }
    
}
