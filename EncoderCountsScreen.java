import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.ShaftEncoder;

public class EncoderCountsScreen implements Screen {
	private ShaftEncoder mLeftEncoder;
	private ShaftEncoder mRightEncoder;

	public EncoderCountsScreen(ShaftEncoder leftEncoder, ShaftEncoder rightEncoder) {
		mLeftEncoder = leftEncoder;
		mRightEncoder = rightEncoder;
	}

	public void update(Display display) {
		int leftCounts = mLeftEncoder.getCounts();
		int rightCounts = mRightEncoder.getCounts();
		display.print(0, "L enc: " + leftCounts);
		display.print(1, "R enc: " + rightCounts);
	}
}