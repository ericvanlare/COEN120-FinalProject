/*
 *
 *The robot sits in one place until an object comes close and triggers off its IR sensors. 
 *The robot then moves full speed ahead until it collides with the object.
 *
 */
public class Pounce_FSM implements Runnable {
    
    //STATE VARIABLES
	private volatile int currentState;
	final int SIT = 0;
    final int POUNCE_LEFT = 1;
    final int POUNCE_RIGHT = 2;
    final int END = 3;

	//EVENT VARIABLES
	public volatile int currentEvent;
	final int IR_LEFT = 0;
	final int IR_RIGHT = 1;
    final int STOP_BUTTON = 2;
    

    public void run(){
        while(currentState != END){
            switch(currentState){
                case SIT:
                    //sitting state
                    break;
                case POUNCE_LEFT:
                    //drive towards the left in direction of object
                    break;
                case POUNCE_RIGHT:
                    //drive towards the right direction of object
                    break;
            }
        }
    }

    public synchronized void dispatch(int event){
        switch(event){
            case STOP_BUTTON:
                //stop the robot program
                currentState = END;
                break;
            case IR_LEFT:
                //change to backup left routine
                currentState = POUNCE_LEFT;
                break;
            case IR_RIGHT:
                //change to backup right routine
                currentState = POUNCE_RIGHT;
                break;
        }
        currentEvent = event;//current event always changes
    }

}