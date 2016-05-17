import com.ridgesoft.robotics.ShaftEncoder;

public class OdometricLocalizer extends Thread implements Localizer {
	private static final float PI = 3.14159f;
	private static final float TWO_PI = PI * 2.0f;

	private ShaftEncoder mLeftEncoder;
	private ShaftEncoder mRightEncoder;
	private float mDistancePerCount;
	private float mRadiansPerCount;
	private float mX = 0.0f;
	private float mY = 0.0f;
	private float mHeading = 0.0f;
	private int mPeriod;
	private int mPreviousLeftCounts;
	private int mPreviousRightCounts;

	public synchronized void setPose(Pose pose) {
		mX = pose.x;
		mY = pose.y;
		mHeading = pose.heading;
	}

	public synchronized void setPose(float x, float y, float heading) {
		mX = x;
		mY = y;
		mHeading = heading;
	}

	public synchronized void setPosition(float x, float y) {
		mX = x;
		mY = y;
	}

	public synchronized void setHeading(float heading) {
		mHeading = heading;
	}

	public synchronized Pose getPose() {
		return new Pose(mX, mY, mHeading);
	}

	public void run() {
		try {
			//periodically sample the encoder and perform dead reckoning calculations
			long nextTime = System.currentTimeMillis();
			while (true) {
				//read encoders
				int leftCounts = mLeftEncoder.getCounts();
				int rightCounts = mRightEncoder.getCounts();
				//calculate change in pose
				int deltaLeft = leftCounts - mPreviousLeftCounts;
				int deltaRight = rightCounts - mPreviousRightCounts;
				float deltaDistance = 0.5f * (float)(deltaLeft + deltaRight) * mDistancePerCount;
				float deltaX = deltaDistance * (float)Math.cos(mHeading);
				float deltaY = deltaDistance * (float)Math.sin(mHeading);
				float deltaHeading = (float)(deltaRight - deltaLeft) * mRadiansPerCount;
				//update position and heading estimates
				synchronized(this) {
					mX += deltaX;
					mY += deltaY;
					mHeading += deltaHeading;

					//limit heading to -pi <= heading <= pi
					if (mHeading > PI)
						mHeading -=TWO_PI;
					else if (mHeading <= -PI)
						mHeading += TWO_PI;
				}
				mPreviousLeftCounts = leftCounts;
				mPreviousRightCounts = rightCounts;
				nextTime += mPeriod;
				Thread.sleep(nextTime - System.currentTimeMillis());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public OdometricLocalizer(ShaftEncoder leftEncoder, ShaftEncoder rightEncoder, float wheelDiameter,
								float trackWidth, int countsPerRevolution, int threadPriority, int period) {
		mLeftEncoder = leftEncoder;
		mRightEncoder = rightEncoder;
		mDistancePerCount = (PI * wheelDiameter) / (float)countsPerRevolution;
		mRadiansPerCount = PI* (wheelDiameter / trackWidth) / countsPerRevolution;
		mPeriod = period;
		mPreviousLeftCounts = leftEncoder.getCounts();
		mPreviousRightCounts = rightEncoder.getCounts();
		setDaemon(true);
		setPriority(threadPriority);
		start();
	}
}