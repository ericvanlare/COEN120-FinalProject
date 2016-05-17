import com.ridgesoft.robotics.Servo;

public class TimedForward implements Runnable {
	private Servo mLeftServo;
	private Servo mRightServo;
	private DirectionListener mLeftListener;
	private DirectionListener mRightListener;
	private int mDuration;

	public TimedForward(Servo leftServo, Servo rightServo, DirectionListener leftListener,
						DirectionListener rightListener, int duration) {
		mLeftServo = leftServo;
		mRightServo = rightServo;
		mLeftListener = leftListener;
		mRightListener = rightListener;
		mDuration = duration;
	}

	public void run() {
		try {
			//forward
			mLeftServo.setPosition(100);
			if (mLeftListener != null)
				mLeftListener.updateDirection(true);
			mRightServo.setPosition(0);
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
		return "Timed Forward";
	}
}