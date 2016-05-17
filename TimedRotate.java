import com.ridgesoft.robotics.Servo;

public class TimedRotate implements Runnable {
	private Servo mLeftServo;
	private Servo mRightServo;
	private DirectionListener mLeftListener;
	private DirectionListener mRightListener;
	private int mDuration;

	public TimedRotate(Servo leftServo, Servo rightServo, DirectionListener leftListener,
						DirectionListener rightListener, int duration) {
		mLeftServo = leftServo;
		mRightServo = rightServo;
		mLeftListener = leftListener;
		mRightListener = rightListener;
		mDuration = duration;
	}

	public void run() {
		try {
			//rotate
			mLeftServo.setPosition(45);
			if (mLeftListener != null)
				mLeftListener.updateDirection(false);
			mRightServo.setPosition(45);
			if (mRightListener != null)
				mRightListener.updateDirection(true);
			Thread.sleep(mDuration);

			//stop
			mLeftServo.setPosition(50);
			mRightServo.setPosition(50);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String toString() {
		return "Timed Rotate";
	}
}