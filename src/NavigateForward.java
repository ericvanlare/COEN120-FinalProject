public class NavigateForward implements Runnable {
	private Navigator mNavigator;
	private float mDistance;

	public NavigateForward(Navigator navigator, float distance) {
		mNavigator = navigator;
		mDistance = distance;
	}

	public void run() {
		mNavigator.moveTo(mDistance, 0.0f, true);
	}

	public String toString() {
		return "Navigate Forward";
	}
}