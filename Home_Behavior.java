/* Thread run from MyBot.java to initialize Home Behavior*/

/*
NOTES: 
    
    Probably want to initialize a IR sensor in here and a Home FSM in here

*/

public class Home_Behavior implements Runnable {
    
    //Location that we want the robot to travel to
    Pose mDest;
    final float locX = 20;
    final float locY = 20;
    final float head = 0;
    
    //the two sensor threads we need
    private IRSensor irSensor;
    private Home_FSM homeFSM;
    
    //variables to communicate between threads and IR setup logistics
    final int SAMPLE_RATE = 100;//milliseconds 1 second is too much, 0.5 a second still seems too much
    final float SENSOR_THRESHOLD = 10.0f;//inches :better farther than closer, especially if these things are sensitive
    private volatile BlockingQueue buffer;
    Pose mCurrentPose;

    public Home_Behavior(Navigator navigator, Localizer localizer){
        mDest = new Pose(locX,locY,head);
        buffer = new BlockingQueue();//initialize the queue which takes care of the actions/events that take place
        irSensor = new IRSensor(buffer, SENSOR_THESHOLD, Thread.MAX_PRIORITY, SAMPLE_RATE);
        homeFSM = new Home_FSM(navigator, localizer, destination);
    }

    public void run(){
        //start the two threads we need
        Thread hFSM = new Thread(homeFSM);
        irSensor.start();
        homeFSM.start();
        
        //possibly while current position is not equal to destination
        while(!mCurrentPose.equals(mDest)){//some other conditional besides an infinite loop
            mCurrentPose = homeFSM.getCurrentPose();
            homeFSM.dispatch(buffer.get());//changes the event in the home FSM
        }
        homeFSM.dispatch(homeFSM.COMPLETE);//tell the state machine to stop doing things
    }
    
    public String toString() {
        return "Homing Behavior"
    }
}