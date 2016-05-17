public class NavigateTriangle implements Runnable {
	private Navigator mNavigator;
	private float mSize;
	public NavigateTriangle(Navigator navigator, float size) {
		mNavigator = navigator;
		mSize = size;
	}

	public void run() {
		mNavigator.moveTo(mSize, 0.0f, true);
		mNavigator.moveTo(0.0f, -mSize, true);
		mNavigator.moveTo(0.0f, 0.0f, true);
		mNavigator.turnTo(0.0f, true);
	}

	public String toString() {
		return "Triangle";
	}
}