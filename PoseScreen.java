import com.ridgesoft.io.Display;

public class PoseScreen implements Screen {
	private Localizer mLocalizer;

	public PoseScreen(Localizer localizer) {
		mLocalizer = localizer;
	}

	public void update(Display display) {
		Pose pose = mLocalizer.getPose();
		display.print(0, Integer.toString((int)pose.x) + ", " + (int)pose.y);
		display.print(1, Integer.toString((int)Math.toDegrees(pose.heading)));
	}
}