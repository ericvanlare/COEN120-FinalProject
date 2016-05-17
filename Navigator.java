public interface Navigator {
	public void moveTo(float x, float y, boolean wait);
	public void moveTo(float x, float y,
					NavigatorListener listener);
	public void turnTo(float heading, boolean wait);
	public void turnTo(float heading,
					NavigatorListener listener);
	public void go(float heading);
	public void stop();
}