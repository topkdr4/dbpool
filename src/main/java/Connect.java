import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Connect {

    private static final Object LOCK = new Object();
    private final DataBase dataBase;
    private final Configurator config;
    private String url;
    private int wait_timeout;
    private int max_connections;
    private boolean isOpen = false;
    private Connection connection;


    public Connect(DataBase dataBase, Configurator config) {
        this.dataBase = dataBase;
        this.config = config;
    }

    public Connect(Connect other) {
        this.dataBase = other.dataBase;
        this.config = other.config;
        this.url = other.url;
        this.wait_timeout = other.wait_timeout;
        this.max_connections = other.max_connections;
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

            String wait_timeout = "SELECT variable_value FROM information_schema.SESSION_VARIABLES WHERE VARIABLE_NAME = 'wait_timeout'";
            String max_connections = "SELECT variable_value FROM information_schema.SESSION_VARIABLES WHERE VARIABLE_NAME = 'max_connections'";

            ResultSet resultSet = statement.executeQuery(wait_timeout);
            if (resultSet.first()) {
                this.wait_timeout = Integer.parseInt(resultSet.getString("variable_value"));
            }

            resultSet = statement.executeQuery(max_connections);
            if (resultSet.first()) {
                this.max_connections = Integer.parseInt(resultSet.getString("variable_value"));
            }

            resultSet.close();
        }

    }

    public void openNewMySQLSession() throws SQLException {
        isOpen = true;
        connection = new Connection(DriverManager.getConnection(url, config.getLogin(), config.getPassword()));
    }

    public Connection getConnection() throws SQLException {
        switch (dataBase) {
            case MY_SQL: {
                openNewMySQLSession();
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

        return connection;
    }

    public int getConnect_timeout() {
        return wait_timeout;
    }

    public int getMax_connections() {
        return max_connections;
    }

}
