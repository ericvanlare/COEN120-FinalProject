/* thread that implements the state machine for Pounce: runs inside of the Pounce_Behavior thread */

/*
 *
 *The robot sits in one place until an object comes close and triggers off its IR sensors. 
 *The robot then moves full speed ahead until it collides with the object.
 *
 */
public class Pounce_FSM implements Runnable {
    
    private static final float PI = 3.14159f;
	private static final float TWO_PI = PI * 2.0f;
	private static final float PI_OVER_2 = PI / 2.0f;
    private static final float THREE_PI_OVER_2 = (3*PI)/ 2.0f;

    //Physical Peripherals
    Navigator mNavigator;
    Localizer mLocalizer;
    Pose mCurrentPose;
    
    //STATE VARIABLES
	private volatile int currentState;
	final int SIT = 0;
    final int POUNCE_LEFT = 1;
    final int POUNCE_RIGHT = 2;

	//EVENT VARIABLES
	final static int IR_LEFT = 0;
	final static int IR_RIGHT = 1;
    
    public Pounce_FSM(Navigator navigator, Localizer localizer){
        //setting physical peripheral control and destination
        mNavigator = navigator;
        mLocalizer = localizer;
        mCurrentPose = mLocalizer.getPose();//get current pos
        
        //setting initial state and event
		currentState = SIT;
    }
    

    public void run(){
		float rev_x, rev_y;
        while(true){
            switch(currentState){
                case SIT:
                    break;
                case POUNCE_LEFT:
                    //drive towards the left in direction of object
					mNavigator.turnTo(mLocalizer.getPose().heading+(PI/6.0f), true);// turns 30 deg
					mNavigator.forward();//pounce forward
					currentState = SIT;
                    break;
                case POUNCE_RIGHT:
                    //drive towards the right direction of object
					mNavigator.turnTo(mLocalizer.getPose().heading-(PI/6.0f), true);// turns 30 deg
					mNavigator.forward();//pounce forward
					currentState = SIT;
                    break;
            }
        }
    }

    public synchronized void dispatch(int event){
        switch(event){
            case IR_LEFT:
                //change to backup left routine
				if (currentState == SIT)
					currentState = POUNCE_LEFT;
                break;
            case IR_RIGHT:
                //change to backup right routine
                if (currentState == SIT)
					currentState = POUNCE_RIGHT;
                break;
			default:
				break;
        }
    }
    
    //Activity and behavior methods will be defined here

}