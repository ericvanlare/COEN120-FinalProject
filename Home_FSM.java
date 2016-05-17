public class Home_FSM implements Runnable {

	//STATE VARIABLES
	private volatile int currentState;
	final int NAVIGATE = 0;
	final int BACKUP_LEFT = 1;
	final int BACKUP_RIGHT = 2;
	final int SPIN_LEFT = 3;
	final int SPIN_RIGHT = 4;
	final int FORWARD = 5;

	//EVENT VARIABLES
	public volatile int currentEvent;
	final int IR_LEFT = 0;
	final int IR_RIGHT = 1;
	final int NULL = 3;
	

	public Home_FSM(Navigator navigator, Localizer localizer, int homeX, int homeY) {
		System.out.println("hey this is goin");
		currentState = NAVIGATE;
		currentEvent = NULL;
	}

	public void run() {
		while (position != home) {
			switch (state) {
				case NAVIGATE:
					
					break;
				case BACKUP_LEFT:
					backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case BACKUP_RIGHT:
					backup();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case SPIN_LEFT:
					spinleft();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case SPIN_RIGHT:
					spinright();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
				case FORWARD:
					forward();
					try {
						Thread.sleep(1000);
					} catch (InterruptException e) {}
					break;
			}
		}
	}
}