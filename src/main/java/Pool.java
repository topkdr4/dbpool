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

    private static final Object SYNC = new Object();
    private static final String CHECK_CONNECTION = "SELECT 1";
    private static final BlockingQueue<Connect> UNUSED_CONNECTIONS = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Connection> USED_CONNECTIONS = new LinkedBlockingQueue<>();
    private static volatile boolean flag = false;
    private static Pool pool;

    private Pool() {

    }


    public static Pool createPool(Connect connect) throws SQLException {
        if (pool == null) {
            synchronized (SYNC) {
                if (pool == null) {
                    connect.createConnection();
                    pool = new Pool();
                    for (int i = 0; i < connect.getMax_connections(); i++) {
                        UNUSED_CONNECTIONS.add(new Connect(connect));
                    }
                    ScheduledTask.execute(connect.getConnect_timeout(), 10, Pool::checkAvailable);
                    return pool;
                }
            }
        }

        return pool;
    }

    public static Pool getInstance() {
        if (pool == null) {
            synchronized (SYNC) {
                if (pool == null) {
                    pool = new Pool();
                    return pool;
                }
            }
        }
        return pool;
    }

    public static void addConnection(Connection connection) {
        /**
         * Самый жесткий костыль всех времен
         * */
        while (flag) {
        }
        USED_CONNECTIONS.add(connection);
    }

    private static void checkAvailable() {
        flag = true;
        List<Connection> tempList = new ArrayList<>();
        System.out.println(USED_CONNECTIONS.size());
        for (Connection connection : USED_CONNECTIONS) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(CHECK_CONNECTION)) {

                tempList.add(connection);

            } catch (SQLException e) {
                //ignore
            }
        }
        USED_CONNECTIONS.clear();
        USED_CONNECTIONS.addAll(tempList);
        flag = false;
    }

    public synchronized Connection getConnection() throws SQLException {
        /**
         * Самый жесткий костыль всех времен
         * */
        while (flag) {
        }
        Connection connect;
        try {
            connect = USED_CONNECTIONS.poll(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new SQLException(e);
        }

        if (connect != null)
            return connect;

        try {
            return UNUSED_CONNECTIONS.poll(3, TimeUnit.SECONDS).getConnection();
        } catch (InterruptedException e) {
            throw new SQLException(e);
        }
    }
}
