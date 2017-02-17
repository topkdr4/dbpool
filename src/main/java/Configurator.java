/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Configurator {
    
    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private final String dbname;
    
    
    public Configurator(String host, int port, String login, String password, String dbname) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.dbname = dbname;
    }
    
    
    public String getHost() {
        return host;
    }
    
    
    public int getPort() {
        return port;
    }
    
    
    public String getLogin() {
        return login;
    }
    
    
    public String getPassword() {
        return password;
    }
    
    
    public String getDbname() {
        return dbname;
    }
}
