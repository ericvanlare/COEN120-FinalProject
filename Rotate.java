public class Rotate implements Runnable {
	private Navigator mNavigator;

	public Rotate(Navigator navigator) {
		mNavigator = navigator;
	}

	public void run() {
		mNavigator.turnTo((float)Math.toRadians(180), true);
	}

	public String toString() {
		return "Rotate";
	}
}