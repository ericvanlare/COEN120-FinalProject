public class Home_FSM implements Runnable {

	//STATE VARIABLES
	private volatile int currentState;
	final int NAVIGATE = 0;
	final int BACKUP_SPIN_LEFT = 1;
	final int BACKUP_SPIN_RIGHT = 2;
	final int FORWARD = 3;

	//EVENT VARIABLES
	public volatile int currentEvent;
	final int IR_LEFT = 0;
	final int IR_RIGHT = 1;
	final int NULL = 2;
	

	public Home_FSM(Navigator navigator, Localizer localizer, int homeX, int homeY) {
		currentState = NAVIGATE;
		currentEvent = NULL;
	}

	public void run() {
		while (position != home) {
			switch (currentState) {
				case NAVIGATE:
                    //go towards the end point
					break;
				case BACKUP_SPIN_LEFT:
                    //avoid the obstacle that is on your left
					//backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case BACKUP_SPIN_RIGHT:
                    //avoid the obstacle that is on your right
					//backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case FORWARD:
                    //moving forward to make the avoidance procedure more effect
					//forward();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
			}
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
        }
        currentEvent = event;//current event always changes
    }
}