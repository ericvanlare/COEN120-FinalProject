/* thread that implements the state machine for Pounce: runs inside of the Pounce_Behavior thread */

/*
 *
 *The robot sits in one place until an object comes close and triggers off its IR sensors. 
 *The robot then moves full speed ahead until it collides with the object.
 *
 */
public class Pounce_FSM implements Runnable {
    
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
                    //sitting state
					System.out.println("Sit");
                    break;
                case POUNCE_LEFT:
                    //drive towards the left in direction of object
					System.out.println("Pounce Left");
					rev_x = mCurrentPose.x + (5 * (float)Math.cos(((double)mCurrentPose.heading)+0.5));
        			rev_y = mCurrentPose.y + (5 * (float)Math.sin(((double)mCurrentPose.heading)+0.5));
        			mNavigator.moveTo(rev_x,rev_y, true);
					currentState = SIT;
                    break;
                case POUNCE_RIGHT:
                    //drive towards the right direction of object
					System.out.println("Pounce Right");
					rev_x = mCurrentPose.x + (5 * (float)Math.cos(((double)mCurrentPose.heading)-0.5));
        			rev_y = mCurrentPose.y + (5 * (float)Math.sin(((double)mCurrentPose.heading)-0.5));
        			mNavigator.moveTo(rev_x,rev_y, true);
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