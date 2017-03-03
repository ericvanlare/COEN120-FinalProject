public class NavigateFigureEight implements Runnable {
	private Navigator mNavigator;
	private float mHeight;
	private float mWidth;
	public NavigateFigureEight(Navigator navigator,
								float height, float width) {
		mNavigator = navigator;
		mHeight = height;
		mWidth = width;
	}

	public void run() {
		mNavigator.moveTo(mHeight, mWidth, true);
		mNavigator.moveTo(mHeight, 0.0f, true);
		mNavigator.moveTo(0.0f, mWidth, true);
		mNavigator.moveTo(0.0f, 0.0f, true);
		mNavigator.turnTo(0.0f, true);
	}

	public String toString() {
		return "Figure Eight";
	}
}