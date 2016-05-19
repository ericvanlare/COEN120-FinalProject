/* Thread run from MyBot.java to initialize Pounce Behavior*/

public class Pounce_Behavior implements Runnable{

    //the two sensor threads we need
    private IRSensor irSensor;
    private Pounce_FSM pounceFSM;
    
    //variables to communicate between threads and IR setup logistics
    final int SAMPLE_RATE = 100;//milliseconds 1 second is too much, 0.5 a second still seems too much
    final float SENSOR_THRESHOLD = 10.0f;//inches :better farther than closer, especially if these things are sensitive
    private volatile BlockingQueue buffer;
    
    public Pounce_Behavior(){
        buffer = new BlockingQueue();
        //need a new ir sensor thread for pounce
    }
    
    public void run(){
        
    }
    
    public String toString() {
        return "Pouncing Behavior"
    }

}