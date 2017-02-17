/**
 * Created by vetoshkin-av on 17.02.2017.
 * vetoshkin-av@dartit.ru
 */
public class Main {
    
    
    
    public static void main(String[] args) throws InterruptedException {
        ScheduledTask.execute(2, Main::ololo);
        
        Thread.sleep(60000);
    }
    
    
    private static void ololo() {
        System.out.println("OLOLO");
    }
    
}
