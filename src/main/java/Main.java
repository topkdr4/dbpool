import java.sql.SQLException;

/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {


    public static void main(String[] args) throws SQLException {
        Configurator configurator = new Configurator("localhost", 3306, "root", "", "football");
        Connect connect = new Connect(DataBase.MY_SQL, configurator);

        Pool pool = Pool.createPool(connect);
        try (Connection connection = pool.getConnection()) {
        }

    }

}
