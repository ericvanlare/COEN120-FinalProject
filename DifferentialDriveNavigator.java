import com.ridgesoft.robotics.Motor;

public class DifferentialDriveNavigator extends Thread
										implements Navigator {

	private static final float PI = 3.14159f;
	private static final float TWO_PI = PI * 2.0f;
	private static final float PI_OVER_2 = PI / 2.0f;

	private static final int STOP = 0;
	private static final int GO = 1;
	private static final int MOVE_TO = 2;
	private static final int ROTATE = 3;

	private Motor mLeftMotor;
	private Motor mRightMotor;
	private Localizer mLocalizer;
	private int mDrivePower;
	private int mRotatePower;
	private int mPeriod;
	private int mState;
	private float mDestinationX;
	private float mDestinationY;
	private float mTargetHeading;
	private float mGain;
	private float mGoToThreshold;
	private float mRotateThreshold;
	private NavigatorListener mListener;

	public DifferentialDriveNavigator(
				Motor leftMotor, Motor rightMotor,
				Localizer localizer,
				int drivePower, int rotatePower,
				float gain,
				float goToThreshold, float rotateThreshold,
				int threadPriority, int period) {
		mLeftMotor = leftMotor;
		mRightMotor = rightMotor;
		mLocalizer = localizer;
		mDrivePower = drivePower;
		mRotatePower = rotatePower;
		mGain = gain;
		mGoToThreshold = goToThreshold;
		mRotateThreshold = rotateThreshold;
		mPeriod = period;
		mState = STOP;
		mListener = null;
		setPriority(threadPriority);
		setDaemon(true);
		start();
	}

	private void updateListener(boolean completed,
		NavigatorListener newListener) {
		if (mListener != null)
			mListener.navigationOperationTerminated(completed);

		mListener = newListener;
	}

	private float normalizeAngle(float angle) {
		while (angle < -PI)
			angle += TWO_PI;
		while (angle > PI)
			angle -= TWO_PI;
		return angle;
	}

	public void run() {
		try {
			while (true) {
				switch (mState) {
				case MOVE_TO:
					goToPoint();
					break;
				case GO:
					goHeading();
					break;
				case ROTATE:
					doRotate();
					break;
				default: // stopped
					break;
				}
				Thread.sleep(mPeriod);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private synchronized void goHeading() {
		Pose pose = mLocalizer.getPose();
		float error = mTargetHeading - pose.heading;
		if (error > PI)
			error -= TWO_PI;
		else if (error < -PI)
			error += TWO_PI;

		int differential = (int)(mGain * error + 0.5f);

		mLeftMotor.setPower(mDrivePower - differential);
		mRightMotor.setPower(mDrivePower + differential);
	}

	private synchronized void goToPoint() {
		Pose pose = mLocalizer.getPose();
		float xError = mDestinationX - pose.x;
		float yError = mDestinationY - pose.y;

		float absXError = (xError > 0.0f) ? xError : -xError;
		float absYError = (yError > 0.0f) ? yError : -yError;
		if ((absXError + absYError) < mGoToThreshold) {
			// stop
			mLeftMotor.setPower(Motor.STOP);
			mRightMotor.setPower(Motor.STOP);
			mState = STOP;

			// notify listener the operation is complete
			updateListener(true, null);

			// signal waiting thread we are at the destination
			notify();
		}
		else {
			// adjust heading and go that way
			mTargetHeading = (float)Math.atan2(yError, xError);
			goHeading();
		}
	}

	private synchronized void doRotate() {
		Pose pose = mLocalizer.getPose();
		float error = mTargetHeading - pose.heading;
		// choose the direction of rotation that results
		// in the smallest angle
		if (error > PI)
			error -= TWO_PI;
		else if (error < -PI)
			error += TWO_PI;
		float absError = (error >= 0.0f) ? error : -error;
		if (absError < mRotateThreshold) {
 			mLeftMotor.setPower(Motor.STOP);
			mRightMotor.setPower(Motor.STOP);
			mState = STOP;

			// notify listener the operation is complete
			updateListener(true, null);

			// signal waiting thread we are at the destination
			notify();
		}
		else if (error > 0.0f) {
			mLeftMotor.setPower(-mRotatePower);
			mRightMotor.setPower(mRotatePower);
		}
		else {
			mLeftMotor.setPower(mRotatePower);
			mRightMotor.setPower(-mRotatePower);
		}
	}

	private synchronized void moveTo(float x, float y, boolean wait,
									NavigatorListener listener) {
		updateListener(false, listener);
		mDestinationX = x;
		mDestinationY = y;
		mState = MOVE_TO;
		if (wait) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
	}

	public void moveTo(float x, float y, boolean wait) {
		moveTo(x, y, wait, null);
	}

	public void moveTo(float x, float y,
						NavigatorListener listener) {
		moveTo(x, y, false, listener);
	}
	
	private synchronized void help_backup(){
		mLeftMotor.setPower(Motor.MAX_REVERSE);
		mRightMotor.setPower(Motor.MAX_REVERSE);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){}
		mRightMotor.brake();
		mLeftMotor.brake();

		// notify listener the operation is complete
		updateListener(true, null);
	}

	public void backup(){
		help_backup();
	}
	
	private synchronized void help_forward(){
		mLeftMotor.setPower(Motor.MAX_FORWARD);
		mRightMotor.setPower(Motor.MAX_FORWARD);
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){}
		mRightMotor.brake();
		mLeftMotor.brake();

		// notify listener the operation is complete
		updateListener(true, null);
	}

	public void forward(){
		help_forward();
	}

	public synchronized void turnTo(float heading, boolean wait,
						NavigatorListener listener) {
		updateListener(false, listener);
		mTargetHeading = normalizeAngle(heading);
		mState = ROTATE;
		if (wait) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
	}

	public void turnTo(float heading, boolean wait) {
		turnTo(heading, wait, null);
	}

	public void turnTo(float heading, NavigatorListener listener) {
		turnTo(heading, false, listener);
	}

	public synchronized void go(float heading) {
		mTargetHeading = normalizeAngle(heading);
		mState = GO;
		updateListener(false, null);
	}

	public synchronized void stop() {
		mLeftMotor.setPower(Motor.STOP);
		mRightMotor.setPower(Motor.STOP);
		mState = STOP;
		updateListener(false, null);
	}
}