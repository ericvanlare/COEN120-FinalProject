/* thread that implements the state machine for Home: runs inside of the Home_Behavior thread */

public class Home_FSM implements Runnable {
    
    private static final float PI = 3.14159f;
	private static final float TWO_PI = PI * 2.0f;
	private static final float PI_OVER_2 = PI / 2.0f;
    private static final float THREE_PI_OVER_2 = (3*PI)/ 2.0f;
    
    //Physical Peripherals
    Navigator mNavigator;
    Localizer mLocalizer;
    
    Pose mCurrentPose;
    Pose mHome;//destination

	//STATE VARIABLES
	private volatile int currentState;
	final static int NAVIGATE = 0;
	final static int BACKUP_SPIN_LEFT = 1;
	final static int BACKUP_SPIN_RIGHT = 2;
	final static int FORWARD = 3;
    final static int STOP = 4;

	//EVENT VARIABLES
	public volatile int currentEvent;
	final static int IR_LEFT = 0;
	final static int IR_RIGHT = 1;
	final static int NULL = 2;
    final static int COMPLETE = 3;
    
    //AVOIDANCE VARS
    final float BACKUP_LENGTH = 10;
    final float FORWARD_LENGTH = 10;

	public Home_FSM(Navigator navigator, Localizer localizer, Pose home) {
        //setting physical peripheral control and destination
        mNavigator = navigator;
        mLocalizer = localizer;
        mCurrentPose = mLocalizer.getPose();//get current pos
        mHome = home;
        
        //setting initial state and event
		currentState = NAVIGATE;
		currentEvent = NULL;
	}

	public void run() {
		while (true) {
			switch (currentState) {
				case NAVIGATE:
                    //go towards the end point
                    mNavigator.moveTo(mHome.x, mHome.y, true);//float x, float y, boolean wait
					break;
				case BACKUP_SPIN_LEFT:
                    //avoid the obstacle that is on your left
					//backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					break;
				case BACKUP_SPIN_RIGHT:
                    //avoid the obstacle that is on your right
					//backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					break;
				case FORWARD:
                    //moving forward to make the avoidance procedure more effect
					//forward();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					break;
                case STOP:
                    //stop all moving activities
                    mNavigator.stop();
                    break;
			}
            mCurrentPose = mLocalizer.getPose();//updating the current position
		}
	}
    
    public synchronized void dispatch(int event){
        switch(event){
            case IR_LEFT:
                //change to backup left routine
                currentState = BACKUP_SPIN_LEFT;
                break;
            case IR_RIGHT:
                //change to backup right routine
                currentState = BACKUP_SPIN_RIGHT;
                break;
            case COMPLETE:
                //robot is at the destination it needs to be at
                currentState = STOP;
                break;
        }
        currentEvent = event;//current event always changes
    }
    
    //Activity and behavior methods will be defined here
    public synchronized void getCurrentPose(){
        return mCurrentPose;
    }
    
    private void reverse(float distance){
        float rev_x = mCurrentPose.x - (distance * (float)Math.cos(((double)mCurrentPose.heading)));
        float rev_y = mCurrentPose.y - (distance * (float)Math.sin(((double)mCurrentPose.heading)));
        mNavigator.moveTo(rev_x,rev_y, true);
    }
    
    public void backupAndSpinLeft(){
        //backup portion
        
        
        //move straight 
        
        //if you want to turn left you must then turn right, since you have flipped yourself
        //move forward again to avoid the obstacle
        //continue towards the destination
        
    }
    
    public void backupAndSpinRight(){
        //spin the heading 180 degrees
        //move straight 
        //if you want to turn right you must then turn left, since you have flipped yourself
        //move forward again to avoid the obstacle
        //continue towards the destination
        
    }
    
}