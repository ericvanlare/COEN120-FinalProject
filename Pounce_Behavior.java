/* Thread run from MyBot.java to initialize Pounce Behavior*/

public class Pounce_Behavior implements Runnable{

    BlockingQueue buffer;
    
    public Pounce_Behavior(){
        buffer = new BlockingQueue();
    }
    
    public void run(){
        
    }
    
    public String toString() {
        return "Pouncing Behavior"
    }

}