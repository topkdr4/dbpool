/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public final class Pool {
    
    private static Pool pool;
    private static final Object sync = new Object();
    
    
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
    
    private Pool() {}
    
    private static void checkAvailable() {
        
    }
}
